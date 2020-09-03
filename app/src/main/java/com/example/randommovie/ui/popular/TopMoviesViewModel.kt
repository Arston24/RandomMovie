package com.example.randommovie.ui.popular

import androidx.lifecycle.LiveData
import com.example.randommovie.database.Movie
import com.example.randommovie.ui.BaseViewModel

class TopMoviesViewModel : BaseViewModel<Movie>() {

    private lateinit var movieResponseList: LiveData<List<Movie>>
    //private var repository = ApiRepository(Api.create())


//    override fun getDataFromRetrofit(page: Int): LiveData<List<Movie.Result>> {
//        movieList = repository.loadData(page)
//        return movieList
//    }

    override fun getDataFromRetrofit(page: Int): LiveData<List<Movie>> {
       // movieList = repository.loadData(page)
        return movieResponseList
    }

}



