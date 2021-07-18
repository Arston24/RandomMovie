package com.victorsysuev.randommovie.di

import android.app.Application
import androidx.room.Room
import com.victorsysuev.randommovie.database.MovieDao
import com.victorsysuev.randommovie.database.MovieDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule(private val application: Application) {

    var movieDatabase: MovieDatabase = Room.databaseBuilder(
        application.applicationContext,
        MovieDatabase::class.java,
        "movie_db"
    ).build()

    @Provides
    fun provideRoomDatabase(): MovieDatabase {
        return movieDatabase
    }

    @Provides
    fun provideMovieDao(): MovieDao {
        return movieDatabase.movieDao()
    }

}