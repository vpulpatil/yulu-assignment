package co.yulu.assignment.di.module;

import android.app.Application
import androidx.room.Room
import co.yulu.assignment.database.AppDatabase
import co.yulu.assignment.database.dao.VenueDao
import co.yulu.assignment.util.AppConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase:: class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideVenueDao(appDatabase: AppDatabase): VenueDao = appDatabase.venueDao()

}