package sample.data.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CassandraCRUDTest implements CommandLineRunner {

    @Autowired
    private RaceRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(CassandraCRUDTest.class, args);
    }

    public void run(String... args) throws Exception {

        InsertQueries insert = new InsertQueries(repository);
        insert.insertRecords();

        SelectQueries select = new SelectQueries(repository);
        select.showRecords();

        UpdateQueries update = new UpdateQueries(repository);
        update.updateQueries();

        DeleteQueries delete = new DeleteQueries(repository);
        delete.deleteRecords();
    }
}
