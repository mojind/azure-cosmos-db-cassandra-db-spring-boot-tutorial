package sample.data.cassandra;

import java.util.Optional;

public class UpdateQueries {
	private RaceRepository repository;

	public UpdateQueries(RaceRepository repository) {
		this.repository = repository;
	}

	 public void updateQueries() {
		UpdateWithoutEntirePrimaryKey();
		UpdateWithEntirePrimaryKey();
	 }

	 private void UpdateWithoutEntirePrimaryKey() {
		 // Update with  Partial Primary Key Specified

		 try {
			 System.out.println("Update With Partial PrimaryKey: Trying");

			 this.repository.updateCyclistNamebyExactRaceNameAndExactRaceYear("GPX", 2012, InsertQueries.CyclistName.IvanH.toString());

			 throw new Exception("Update on Partial primary Key shoud have thrown Exception. Update With Partial PrimaryKey: Failed");
		 } catch (Exception e) {

			 if (e.toString().contains("Some cluster key parts are missing: rank")) {
				 System.out.println("Update With Partial PrimaryKey: Worked Successfully as Clustering Key was also expected.");
			 } else {

				 System.out.println(e.toString());
			 }
		 }
	 }
	private void UpdateWithEntirePrimaryKey()
	{
		// Update with Entire Primary Key Specified

		try {
			System.out.println("Update With Entire PrimaryKey: Trying");

			this.repository.updateCyclistNameyExactRaceNameAndExactRaceYearAndExactRank("GPX", 2012, 2, InsertQueries.CyclistName.IvanH.toString());

			RacePK racePk = new RacePK(2012, "GPX", 2);
			Optional<Race> race = this.repository.findById(racePk);

			if (race.isPresent() && race.get().getCyclistName().equals(InsertQueries.CyclistName.IvanH.toString()))
			{
				System.out.println(race.get());
				System.out.println("Update With Entire PrimaryKey: Worked SuccessFully");
			}
			else
			{
				throw new Exception("Update on Entire primary Key Failed as data recieved on select query is different");
			}
	    }

		catch (Exception e) {

			System.out.println("Update With Entire PrimaryKey: Failed: " + e.toString());
		}
	}
}
