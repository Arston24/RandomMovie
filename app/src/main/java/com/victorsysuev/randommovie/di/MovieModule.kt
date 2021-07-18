package com.victorsysuev.randommovie.di

import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.database.MovieDao
import com.victorsysuev.randommovie.database.MovieLocalCache
import com.victorsysuev.randommovie.network.Api
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors

@Module
object MovieModule {

    private val executor = Executors.newSingleThreadExecutor()

    @Provides
    fun provideMovieRepository(apiService: Api, cache: MovieLocalCache): MovieRepository {
        return MovieRepository(apiService, cache)
    }

    @Provides
    fun provideApi(): Api {
        return Api.create()
    }

    @Provides
    fun provideMovieLocalCache(
        movieDao: MovieDao,
    ): MovieLocalCache {
        return MovieLocalCache(movieDao, executor)
    }

}