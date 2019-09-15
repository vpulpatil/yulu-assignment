package co.yulu.assignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.yulu.assignment.database.dao.*
import co.yulu.assignment.database.entity.*
import co.yulu.assignment.database.entity.typeconverters.*

@Database(
    entities = [
        Venue::class,
        BeenHere::class,
        Category::class,
        Group::class,
        HereNow::class,
        Icon::class,
        LabeledLatLng::class,
        Location::class,
        Photos::class,
        Stats::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    BeenHereConverter::class,
    CategoryConverter::class,
    GroupConverter::class,
    LabeledLatLngConverter::class,
    HereNowConverter::class,
    IconConverter::class,
    LocationConverter::class,
    PhotosConverter::class,
    StatsConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}