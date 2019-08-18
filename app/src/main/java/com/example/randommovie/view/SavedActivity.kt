package com.example.randommovie

import androidx.room.Room
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieDatabase
import ru.arston.randommovie.R
import com.example.randommovie.view.adapters.SavedAdapter


class SavedActivity : androidx.fragment.app.Fragment() {
    lateinit var movieList: List<Movie>
    lateinit var movieRecycler: androidx.recyclerview.widget.RecyclerView
    lateinit var mySwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_saved, container, false)


        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh)

        mySwipeRefreshLayout.setOnRefreshListener {
            doYourUpdate()
        }

        movieRecycler = view.findViewById(R.id.saved_list)
        movieRecycler.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)


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