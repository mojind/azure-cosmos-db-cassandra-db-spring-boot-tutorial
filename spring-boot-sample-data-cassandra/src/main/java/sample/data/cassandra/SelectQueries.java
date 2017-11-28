package sample.data.cassandra;

import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigInteger;
import java.util.List;

public class SelectQueries  {

	private RaceRepository repository;
	private int recordCount;

	public SelectQueries(RaceRepository repository) {
		this.repository = repository;
	}

	public void showRecords() {

		showAllRecords();
		showRecordsWithEqualityClause();
		showRecordsWithGreaterThanLessThanClause();
		showRecordsUsingLimitClause();
		showRecordsUsingOrderByDescClause();
		showRecordsInAsync();
	}

	private void showAllRecords() {
		// Showing All Records
		try {
			System.out.println("Race records found with findAll():");
			System.out.println("-------------------------------");
			recordCount = 0;
			for (Race race : this.repository.findAll()) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 40) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 40");
			}

			System.out.println("All records shown Successfully");
		} catch (Exception e) {
			System.out.println("Select all records failed Stack Trace: " + e.toString());
		}
	}

	private void showRecordsWithEqualityClause() {
		// Selecting with Where Clause '=' and No Partition Key Specified  (without ALLOW FILTERING)

		try {
			System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified (without ALLOW FILTERING): Trying");
			recordCount = 0;
			this.repository.findByExactRankNoAllowFiltering(3);
			throw new Exception("Selecting with Where Clause '=' and No Partition Keys Specified (without ALLOW FILTERING): Failed");
		} catch (Exception e) {
			if (e.toString().contains("use ALLOW FILTERING"))
			{
				System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified(without ALLOW FILTERING): Worked Successfully as 'ALLOW FILTERING' was expected");
			}
			else
			{
				System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified(without ALLOW FILTERING): Failed" + e.toString());
			}
		}

		// Selecting with Where Clause '=' and No Partition Key Specified (with ALLOW FILTERING)

		try {
			System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified: Trying");
			recordCount = 0;
			for (Race race : this.repository.findByExactRank(3)) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 4) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 4");
			}
			System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified: Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and No Partition Keys Specified: Failed " + e.toString());
		}

		// Selecting with Where Clause '=' and Partial Partition Key Specified (with ALLOW FILTERING)

		try {
			System.out.println("Selecting with Where Clause '=' and Partial Partition Keys Specified: Trying");
			recordCount = 0;
			for (Race race : this.repository.findByExactRaceYear(2012)) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 20) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 20");
			}
			System.out.println("Selecting with Where Clause '=' and Partial Partition Keys Specified: Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and Partial Partition Keys Specified: Failed " + e.toString());
		}

		// Selecting with Where Clause '=' and All Partition Key Specified

		try {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified : Trying");
			recordCount = 0;
			for (Race race : this.repository.findByExactRaceNameAndRaceYear("GPX", 2012)) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 10) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 10");
			}

			System.out.println("Selecting with Where Clause '='and All Partition Keys Specified : Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified : Failed " + e.toString());
		}
	}

	private void showRecordsWithGreaterThanLessThanClause() {
		// Selecting with Where Clause '><' and All Partition Key Specified with '=' clause)
		try {
			System.out.println("Selecting with Where Clause '><' and All Partition Keys Specified with '=' clause: Trying");
			recordCount = 0;
			for (Race race : this.repository.findByExactRaceNameAndExactRaceYearGreaterThanAudianceCapacityAndLessThanAudianceCapacity("GPX", 2012, BigInteger.valueOf(200),BigInteger.valueOf(800))) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 6) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 6");
			}
			System.out.println("Selecting with Where Clause '><' and All Partition Keys Specified with '=' clause: Worked Successfully");
		} catch (Exception e) {

			System.out.println("Selecting with Where Clause '><' and All Partition Keys Specified: Failed " + e.toString());
		}
	}

	private void showRecordsUsingLimitClause() {
		// Selecting with Where Clause '=' and All Partition Key Specified  and LIMIT Clause
		try {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified and LIMIT Clause: Trying");
			recordCount = 0;
			for (Race race : this.repository.findByExactRaceNameAndRaceYear("GPX", 2012, 5)) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 5) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 5");
			}

			System.out.println("Selecting with Where Clause '='and All Partition Keys Specified and LIMIT Clause: Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified and LIMIT Clause: Failed " + e.toString());
		}
	}

	private void showRecordsUsingOrderByDescClause() {

		// Selecting with Where Clause '=' and All Partition Key Specified  + Ordering on Rank DESC
		try {

			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified + Ordering on Clustering key DESC: Trying");
			int previousRank = Integer.MAX_VALUE;
			for (Race race : this.repository.findByExactRaceNameAndYearSortDescOnRank("GPX", 2012)) {
				int currentRank = race.getRank();
				if (currentRank > previousRank) {
					throw new Exception(String.format("Ordering on Rank Desc did not work. Previous Rank Value: %s , Current Rank: %s", previousRank, currentRank));
				}
				previousRank = currentRank;
				System.out.println(race);
			}
			System.out.println("Selecting with Where Clause '='and All Partition Keys Specified + Ordering on Clustering key DESC: Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified  + Ordering on Clustering key DESC: Failed. " + e.toString());
		}
	}

	private void showRecordsInAsync()
	{
		// Selecting with Where Clause '=' and All Partition Key Specified in An Async way

		try {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified in An Async way : Trying");
			recordCount = 0;
			ListenableFuture<List<Race>> races = this.repository.findByExactRaceNameAndRaceYearAsync("GPX", 2012);

			for (Race race : races.get()) {
				recordCount++;
				System.out.println(race);
			}
			if (recordCount != 10) {
				throw new Exception("We got only " + recordCount + " Records. Expected records count: 10");
			}

			System.out.println("Selecting with Where Clause '='and All Partition Keys Specified in An Async way : Worked Successfully");
		} catch (Exception e) {
			System.out.println("Selecting with Where Clause '=' and All Partition Keys Specified in An Async way : Failed " + e.toString());
		}
	}
}








