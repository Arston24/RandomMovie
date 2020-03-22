package com.example.randommovie.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.network.Api
import com.example.randommovie.network.getMovies
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieBoundaryCallback(
    private val apiService: Api,
    private val cache: MovieLocalCache
) : PagedList.BoundaryCallback<Movie>() {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    val networkErrors: LiveData<String>
        get() = _networkErrors


    override fun onZeroItemsLoaded() {
        Timber.e("onZeroItemsLoaded")
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Timber.e("onItemAtEndLoaded")
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true
        GlobalScope.launch {
            getMovies(apiService, lastRequestedPage) { movies ->
                cache.insert(movies) {
                    lastRequestedPage++
                    isRequestInProgress = false
                }
            }
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}