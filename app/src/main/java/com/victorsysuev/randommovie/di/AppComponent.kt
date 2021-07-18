package com.victorsysuev.randommovie.di

import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.ui.details.DetailsFragment
import com.victorsysuev.randommovie.ui.favorite.FavoriteMoviesFragment
import com.victorsysuev.randommovie.ui.popular.TopMoviesFragment
import dagger.Component

@Component(modules = [MovieModule::class, RoomModule::class])
interface AppComponent {

    fun inject(fragment: DetailsFragment)
    fun inject(fragment: FavoriteMoviesFragment)
    fun inject(fragment: TopMoviesFragment)


    val movieRepository: MovieRepository

}