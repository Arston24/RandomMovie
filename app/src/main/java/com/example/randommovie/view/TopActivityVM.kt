package com.example.randommovie.view

import androidx.lifecycle.LiveData
import ru.arston.randommovie.Models.Movie

class TopActivityVM : BaseViewModel<Movie.Result>() {

    private lateinit var movieList: LiveData<List<Movie.Result>>
    //private var repository = ApiRepository(Api.create())


//    override fun getDataFromRetrofit(page: Int): LiveData<List<Movie.Result>> {
//        movieList = repository.loadData(page)
//        return movieList
//    }

    override fun getDataFromRetrofit(page: Int): LiveData<List<Movie.Result>> {
       // movieList = repository.loadData(page)
        return movieList
    }

}



