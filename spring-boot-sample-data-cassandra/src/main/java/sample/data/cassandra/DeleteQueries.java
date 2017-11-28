package sample.data.cassandra;

public class DeleteQueries {
	private RaceRepository repository;

	public DeleteQueries(RaceRepository repository)
	{
		this.repository = repository;
	}

	public void deleteRecords()
	{
		deleteWithPartitionParitionKey();
		deleteWithCompletePartitionKey();
	}

	private void deleteWithPartitionParitionKey()
	{
        // Deleting Records with only partition key Specified

		try {
			System.out.println("Deleting Records With Partial Partition Key: Trying");

			this.repository.deleteByExactRaceName("GPX");

			throw new Exception("Deleting Records on Partial primary Key shoud have thrown Exception. Deleting Records on Partial Partition Key: Failed");
		}

		catch (Exception e) {

			if(e.toString().contains("Some partition key parts are missing: race_year"))
			{
				System.out.println("Deleting Records on Partial Partition Key: Worked Successfully as complete Partition key was expected." );
			}
			else
			{

				System.out.println(e.toString());
			}
		}
	}

	private void deleteWithCompletePartitionKey()
	{
		// Deleting Records with Complete partition key Specified
		try {
			System.out.println("Deleting Records with Complete partition key Specified: Trying");

			this.repository.deleteByExactRaceNameAndExactRaceYear("GPX", 2012);
            int recordCount = 0;
			for (Race race : this.repository.findByExactRaceNameAndRaceYear("GPX",2012)) {
				recordCount++;
			}

			if (recordCount != 0) {
				throw new Exception("We got " + recordCount + " Records. Expected records count: 0");
			}

			System.out.println("Deleting Records with Complete partition key Specified: Worked Successfully");
		}

		catch (Exception e) {

			System.out.println("Deleting Records with Complete partition key Failed:" + e.toString());
		}
	}
}
