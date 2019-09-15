package co.yulu.assignment.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.yulu.assignment.database.entity.Venue

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenue(venue: Venue): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(venues: List<Venue>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateVenue(venue: Venue): Int

    @Query("SELECT * FROM `Venue` WHERE :id")
    fun getVenue(id: Long): LiveData<Venue>

    @Query("SELECT * FROM `Venue`")
    fun getAllVenues(): LiveData<List<Venue>>

}