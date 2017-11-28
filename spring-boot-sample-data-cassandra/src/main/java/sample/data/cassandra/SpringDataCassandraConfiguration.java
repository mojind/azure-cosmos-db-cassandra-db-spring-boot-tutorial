package sample.data.cassandra;

import com.datastax.driver.core.SSLOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import java.io.File;
import java.util.Collections;
import java.util.List;
import com.datastax.driver.core.*;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class SpringDataCassandraConfiguration  extends AbstractCassandraConfiguration {

    // This Class Creates the key space and the table if they don't exist
    @Autowired
    private Environment environment;

    private File sslKeyStoreFile = null;
    private String sslKeyStorePassword = "changeit";

    @Bean
    @Override
    public CassandraCqlClusterFactoryBean cluster() {
        CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
        bean.setKeyspaceCreations(getKeyspaceCreations());
        bean.setContactPoints(environment.getProperty("spring.data.cassandra.contact-points"));
        bean.setUsername(environment.getProperty("spring.data.cassandra.username"));
        bean.setPassword(environment.getProperty("spring.data.cassandra.password"));
        bean.setPort(Integer.parseInt(environment.getProperty("spring.data.cassandra.port")));
        bean.setSslOptions(getSSLOptions());
        bean.setSslEnabled(true);
        return bean;
    }

    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("spring.data.cassandra.keyspace-name");
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification
                .createKeyspace(getKeyspaceName())
                .ifNotExists()
                .withNetworkReplication(DataCenterReplication.of ("datacenter1", 1)));
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE;
    }

    public SSLOptions getSSLOptions()
    {
        try {
        loadSSLDetails();

        final KeyStore keyStore = KeyStore.getInstance("JKS");
        try (final InputStream is = new FileInputStream(sslKeyStoreFile)) {
            keyStore.load(is, sslKeyStorePassword.toCharArray());
        }

        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(keyStore, sslKeyStorePassword.toCharArray());
        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                .getDefaultAlgorithm());
        tmf.init(keyStore);

        // Creates a socket factory for HttpsURLConnection using JKS contents.
        final SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
        JdkSSLOptions sslOptions = RemoteEndpointAwareJdkSSLOptions.builder()
                .withSSLContext(sc)
                .build();
        return sslOptions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void loadSSLDetails() throws Exception {

        String ssl_keystore_file_path =  environment.getProperty("ssl_keystore_file_path");
        String ssl_keystore_password = environment.getProperty("ssl_keystore_password");

        // If ssl_keystore_file_path, build the path using JAVA_HOME directory.
        if (ssl_keystore_file_path == null || ssl_keystore_file_path.isEmpty()) {
            String javaHomeDirectory = System.getenv("JAVA_HOME");
            if (javaHomeDirectory == null || javaHomeDirectory.isEmpty()) {
                throw new Exception("JAVA_HOME not set. If you are using IDE, please try relaunching IDE if you had set the JAVA_HOME after opening IDE");
            }
            ssl_keystore_file_path = new StringBuilder(javaHomeDirectory).append("/lib/security/cacerts").toString();
        }

        sslKeyStorePassword = (ssl_keystore_password != null && !ssl_keystore_password.isEmpty()) ?
                ssl_keystore_password : sslKeyStorePassword;

        sslKeyStoreFile = new File(ssl_keystore_file_path);

        if (!sslKeyStoreFile.exists() || !sslKeyStoreFile.canRead()) {
            throw new Exception(String.format("Unable to access the SSL Key Store file from %s. Please verify the path exists. Location is different for different certs. Configured path is for Java 1.9" , ssl_keystore_file_path));
        }
    }
}
