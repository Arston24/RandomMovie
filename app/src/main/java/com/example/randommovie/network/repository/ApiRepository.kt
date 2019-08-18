//package com.example.randommovie.network.repository
//
//import androidx.lifecycle.LiveData
//import com.example.randommovie.network.Api
//import ru.arston.randommovie.BuildConfig
//import ru.arston.randommovie.Models.Movie
//
//
//class ApiRepository(service: Api) : BaseRepository<Movie.Result>(service) {
//    private val apiKey: String = BuildConfig.TMDB_API_KEY
//
//    override fun loadData(page: Int): LiveData<List<Movie.Result>> {
//        return fetchData { service.getPopularMovie(page, apiKey) }
//    }
//}