package com.victorsysuev.randommovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.data.MovieRepository

class MovieViewModel(val movieRepository: MovieRepository): ViewModel(){

    val movies: LiveData<PagedList<Movie>> = MutableLiveData<PagedList<Movie>>()

//    fun getMovies(){
//        movies.value = movieRepository.getMovies().value
//    }
}