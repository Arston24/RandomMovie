package com.example.randommovie.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.randommovie.network.repository.TestRepository
import ru.arston.randommovie.Models.Movie

class ViewModelTest: ViewModel(){

    private lateinit var movieList: LiveData<List<Movie.Result>?>
    private var repository = TestRepository()

    fun getDataFromRetrofit(page: Int): LiveData<List<Movie.Result>?> {
        movieList = repository.getMovies(page)
        return movieList
    }
}