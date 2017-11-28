package sample.data.cassandra;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.UUID;


@Table(value = "race")
public class Race  implements Serializable{

	@PrimaryKey
 	private RacePK racePK;

	@Column(value = "cyclist_name")
	private String cyclistName;

	@Column(value = "location")
	private String location;

	@Column(value = "audiance_capacity")
	@CassandraType(type = DataType.Name.BIGINT)
	private BigInteger audianceCapacity;

	@Column(value = "any_accident")
	private boolean anyAccident;

	@Column(value = "ticket_price")
	private double ticketPrice;

	@Column(value = "transaction_id")
	private UUID transactionId;

	@Column(value = "shop_ip")
	private InetAddress ipAddress;

	public Race() {
	}

	public Race(int race_year, String race_name, int rank, String cyclistName, String location, boolean anyAccident, BigInteger audianceCapacity, double ticketPrice, UUID transactionId, InetAddress ipAddress ) {
		this.racePK = new RacePK(race_year, race_name, rank);
		this.cyclistName = cyclistName;
		this.location = location;
		this.audianceCapacity = audianceCapacity;
		this.anyAccident = anyAccident;
		this.ticketPrice = ticketPrice;
		this.transactionId = transactionId;
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return String.format("Race [race_year='%s', race_name = '%s', rank = '%s', cyclistName = '%s'," +
						" location = '%s', anyAccident = '%s', audianceCapacity = '%s', ticketPrice = '%s', transactionId = '%s', ipAddress = '%s']",
				this.racePK.getRace_year(), this.racePK.getRace_name(), this.racePK.getRank(), this.cyclistName, this.location, this.anyAccident, this.audianceCapacity, this.ticketPrice, this.transactionId, this.ipAddress);
	}

	public int getRank() {
		return this.racePK.getRank();
	}

	public String getCyclistName(){
		return cyclistName;
	}
}
