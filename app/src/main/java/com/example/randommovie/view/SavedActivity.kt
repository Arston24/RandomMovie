package com.example.randommovie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieDatabase
import com.example.randommovie.view.adapters.SavedAdapter
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentSavedBinding


class SavedActivity : Fragment() {

    lateinit var binding: FragmentSavedBinding
    lateinit var movieList: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false)


        binding.swiperefresh.setOnRefreshListener {
            doYourUpdate()
        }

        binding.savedList.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)

        val db = Room.databaseBuilder(
            activity!!.applicationContext,
            MovieDatabase::class.java,
            "descriptionMovie"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        db.movieDao().getFavoriteMovies().observe(viewLifecycleOwner, Observer {
            it.forEach {
                Log.e("fff", "movie $it")
            }
            movieList = it
            val savedAdapter = SavedAdapter(movieList)
            binding.savedList.adapter = savedAdapter
        })
        return binding.root

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }

    private fun doYourUpdate() {
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }
}