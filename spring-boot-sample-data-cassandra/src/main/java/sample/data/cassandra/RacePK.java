package sample.data.cassandra;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import java.io.Serializable;


@PrimaryKeyClass
public class RacePK implements Serializable {

	@PrimaryKeyColumn(name = "race_year",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	private int race_year;

	@PrimaryKeyColumn(name = "race_name",ordinal = 1,type = PrimaryKeyType.PARTITIONED)
	private String race_name;

	@PrimaryKeyColumn(name = "rank",ordinal = 2,type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	private int rank;

	public int getRace_year() {
		return race_year;
	}

	public void setRace_year(int race_year) {
		this.race_year = race_year;
	}

	public String getRace_name() {
		return race_name;
	}

	public void setRace_name(String race_name) {
		this.race_name = race_name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public RacePK(int race_year, String race_name,int rank ) {
		this.race_year = race_year;
		this.race_name = race_name;
		this.rank = rank;
	}

	public RacePK()
	{

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RacePK racePK = (RacePK) o;

		if (race_year != racePK.race_year) return false;
		if (rank != racePK.rank) return false;
		return race_name.equals(racePK.race_name);
	}

	@Override
	public int hashCode() {
		int result = race_year;
		result = 31 * result + race_name.hashCode();
		result = 31 * result + rank;
		return result;
	}
}
