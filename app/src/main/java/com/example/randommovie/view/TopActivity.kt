package ru.arston.randommovie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randommovie.view.TopActivityVM
import com.example.randommovie.view.ViewModelTest
import com.example.randommovie.view.adapters.MovieAdapter
import ru.arston.randommovie.Models.Movie
import ru.arston.randommovie.databinding.FragmentTopBinding


class TopActivity : Fragment() {

    private lateinit var binding: FragmentTopBinding
    private var movieList: LiveData<List<Movie.Result>>? = null
    private lateinit var movieRecycler: RecyclerView
    private var page: Int = 0
    private var currentPage: Int = 1
    private var totalPage: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pastVisibleItemCount: Int = 0
    private var loading: Boolean = true

    private lateinit var vm: TopActivityVM
    private lateinit var vmTest: ViewModelTest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container, false)
        vmTest = ViewModelProviders.of(this).get(ViewModelTest::class.java)
        binding.moviesList.layoutManager = LinearLayoutManager(context)
        //setupOnScrollListener()
        getMovies()
        return binding.root
    }

    private fun getMovies() {
        vmTest.getMovies(1).observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                val movieAdapter = MovieAdapter(data)
//                movieAdapter.submitList()
                binding.moviesList.adapter = movieAdapter
                Log.e("RESULT", data[1].title)
            } else Log.e("RESULT", "Empty data")
        })

//        movieList = vm.getDataFromRetrofit(page)
//        Log.e("RESULT", "before observe")
//        Log.e("RESULTObserve", movieList!!.observe(this, Observer { data -> data[1].title}).toString())
//        movieList!!.observe(this, Observer { data ->
//            Log.e("RESULT", data[1].originalTitle)
//            if (data != null) {
//                movieAdapter = MovieAdapter(data)
//                movieRecycler.adapter = movieAdapter
//            } else Log.e("RESULT", "Empty data")
//
//        })


//        if (movieList == null) {
//
//        } else {
//            movieList!!.addAll(response.body()?.results!!)
//            val currentPosition =
//                androidx.recyclerview.widget.LinearLayoutManager(context).findLastVisibleItemPosition()
//            movieAdapter.notifyDataSetChanged()
//            movieRecycler.scrollToPosition(currentPosition)
//        }


//        loading = true
//        GlobalScope.launch(Dispatchers.Main) {
//            val popularMoviePagesRequest = apiService.getPopularMoviePages(apiKey)
//            try {
//                val response = popularMoviePagesRequest.await()
//                if (response.isSuccessful) {
//                    val movieResponse = response.body()
//                    totalPage = movieResponse?.totalPages!!
//
//                } else {
//                    Log.e("MainActivity ", response.errorBody().toString())
//                }
//            } catch (e: Exception) {
//
//            }
//
//            val popularMovieRequest = apiService.getPopularMovie(page, apiKey)
//            try {
//                Log.e("MoviesRepository", "Current Page = $page")
//                val response = popularMovieRequest.await()
//                if (response.isSuccessful) {
//                    val movieResponse = response.body()
//
//                    if (movieList == null) {
//                        movieList = movieResponse?.results!!
//                        movieAdapter = MovieAdapter(movieList!!)
//                        movieRecycler.adapter = movieAdapter
//                    } else {
//                        movieList!!.addAll(response.body()?.results!!)
//                        val currentPosition = androidx.recyclerview.widget.LinearLayoutManager(context).findLastVisibleItemPosition()
//                        movieAdapter.notifyDataSetChanged()
//                        movieRecycler.scrollToPosition(currentPosition)
//                    }
//
//                    currentPage = page
//                    loading = false
//
//                } else {
//                    Log.e("MainActivity ", response.errorBody().toString())
//                }
//            } catch (e: Exception) {
//
//            }
//        }
    }

//    private fun setupOnScrollListener() {
//
//        val manager = androidx.recyclerview.widget.LinearLayoutManager(context)
//        movieRecycler.layoutManager = manager
//        movieRecycler.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
//                val totalItemCount = manager.itemCount
//                val visibleItemCount = manager.childCount
//                val firstVisibleItem = manager.findFirstVisibleItemPosition()
//
//                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    if (!loading) {
//                        getMovies(currentPage + 1)
//                        Snackbar.make(
//                            movieRecycler,
//                            "Загрузка фильмов...",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        })
//    }

}
