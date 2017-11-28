package sample.data.cassandra;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

public class InsertQueries {

	private RaceRepository repository;

	public InsertQueries(RaceRepository repository)
	{
		this.repository = repository;

	}

	enum Location {
		Delhi, Mumbai, Bangalore, Calcutta
	}

	enum CyclistName {
		LyubovK, JiriK, IvanH, LiliyaB
	}

	enum IPAddress {
		ADDRESS ("121.112.64.99"),
		ADDRESS2 ("2001:cdba:0000:0000:0000:0000:3257:9652"),
		ADDRESS3 ("192.168.20.15"),
		ADDRESS4 ("192.168.20.18");

		private String value;
		private IPAddress(String value)
		{
			this.value = value;
		}

		public String toString()
		{
			return this.value;
		}
	}

	public void insertRecords() {
		// Inserting Records
		try {
			System.out.println("Create Race Records");
			System.out.println("-----------------");
			insertData();
			System.out.println("Records have been Successfully created");
		} catch (Exception e) {
			System.out.println("Insertion failed. Stack Trace:" + e.toString());
		}
	}

	private void insertData() {

		insertData(2012, "GPX", 10);
		insertData(2012, "GPY", 10);
		insertData(2013, "GPX", 10);
		insertData(2013, "GPY", 10);
	}

	private  void insertData (int race_year, String race_name, int count_insert )
	{
		try {
			System.out.println("Inserting Records for race_year: " + race_year + " race_name: " + race_name);
			int rank_start = 1;
			Random rand = new Random();
			int inserted;
			for (inserted = 0; inserted < count_insert; inserted++) {
				int randomNumber = (inserted * 100) + (rand.nextInt(100) + 1);
				Race race = new Race(race_year, race_name, rank_start++,
						InsertQueries.CyclistName.values()[rand.nextInt(4)].toString(),
						InsertQueries.Location.values()[rand.nextInt(4)].toString(),
						false,
						BigInteger.valueOf(randomNumber),
						randomNumber,
						UUID.randomUUID(),
						InetAddress.getByName(InsertQueries.IPAddress.values()[rand.nextInt(4)].toString()));
				this.repository.save(race);
			}

			System.out.println("Insertion for Records for race_year: " + race_year + " race_name: " + race_name + " done");
		}
		catch (Exception e) {
			System.out.println("Insertion for Records for race_year: " + race_year + " race_name: " + race_name + " Failed" + e.toString());
		}
	}
}
