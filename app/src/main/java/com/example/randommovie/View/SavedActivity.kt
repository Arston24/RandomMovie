package com.example.randommovie

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.randommovie.Database.Movie
import com.example.randommovie.Database.MovieDatabase
import ru.arston.randommovie.R
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.randommovie.Adapters.MovieAdapter
import com.example.randommovie.Adapters.SavedAdapter
import java.io.FileOutputStream
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager


class SavedActivity : Fragment() {
    lateinit var movieList: List<Movie>
    lateinit var movieRecycler: RecyclerView
    lateinit var mySwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_saved, container, false)


        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh)

        mySwipeRefreshLayout.setOnRefreshListener {
            doYourUpdate()
        }

        movieRecycler = view.findViewById(R.id.saved_list)
        movieRecycler.layoutManager = GridLayoutManager(context, 3)


        val db = Room.databaseBuilder(activity!!.applicationContext, MovieDatabase::class.java, "descriptionMovie")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        movieList = db.movieDao().getAll()

        val savedAdapter = SavedAdapter(movieList)
        movieRecycler.adapter = savedAdapter


        return view

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