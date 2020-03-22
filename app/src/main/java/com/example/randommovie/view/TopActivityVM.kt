package com.example.randommovie.view

import androidx.lifecycle.LiveData
import com.example.randommovie.database.Movie
import ru.arston.randommovie.Models.MovieResponse

class TopActivityVM : BaseViewModel<Movie>() {

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



