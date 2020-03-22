package com.example.randommovie.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.randommovie.database.Movie
import com.example.randommovie.network.repository.MovieRepository
import ru.arston.randommovie.Models.MovieResponse

class MovieViewModel(val movieRepository: MovieRepository): ViewModel(){

    val movies: LiveData<PagedList<Movie>> = MutableLiveData<PagedList<Movie>>()

//    fun getMovies(){
//        movies.value = movieRepository.getMovies().value
//    }
}