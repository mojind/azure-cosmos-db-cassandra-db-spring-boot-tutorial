package sample.data.cassandra;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.concurrent.ListenableFuture;

public interface RaceRepository extends CrudRepository<Race, RacePK> {

   @Query("Select * from race where rank=?0")
	public List<Race> findByExactRankNoAllowFiltering(int rank);

	@Query("Select * from race where rank=?0 ALLOW FILTERING")
	public List<Race> findByExactRank(int rank);

	@Query("Select * from race where race_year=?0 ALLOW FILTERING")
	public List<Race> findByExactRaceYear(int raceYear);

	@Query("Select * from race where race_name=?0 and race_year=?1")
	public List<Race> findByExactRaceNameAndRaceYear(String raceName, int raceYear);

	@Query("Select * from race where race_name=?0 and race_year=?1")
	public ListenableFuture<List<Race>> findByExactRaceNameAndRaceYearAsync(String raceName, int raceYear);

	@Query("Select * from race where race_name=?0 and race_year=?1 limit ?2")
	public List<Race> findByExactRaceNameAndRaceYear(String raceName, int raceYear, int recordCount);

	@Query("Select * from race where race_name=?0 and race_year=?1 order by rank desc")
	public List<Race> findByExactRaceNameAndYearSortDescOnRank(String raceName, int raceYear);

	@Query("Select * from race where race_name=?0 and race_year=?1 and audiance_capacity > ?2 and audiance_capacity < ?3 ")
	public List<Race> findByExactRaceNameAndExactRaceYearGreaterThanAudianceCapacityAndLessThanAudianceCapacity(String raceName, int raceYear, BigInteger lowerAudianceCapacity, BigInteger higherAudianceCapacity);

	@Query("Update race set cyclist_name = ?2 where race_name = ?0 and race_year = ?1 ")
	public void updateCyclistNamebyExactRaceNameAndExactRaceYear(String raceName, int raceYear, String cyclistName);

	@Query("Update race set cyclist_name = ?3 where race_name = ?0 and race_year = ?1 and rank = ?2 ")
	public void updateCyclistNameyExactRaceNameAndExactRaceYearAndExactRank(String raceName, int raceYear, int rank, String cyclistName );

	@Query("Delete from race where race_name = ?0")
	public void deleteByExactRaceName(String raceName);

	@Query("Delete from race where race_name = ?0 and race_year = ?1 ")
	public void deleteByExactRaceNameAndExactRaceYear(String raceName, int raceYear );
}
