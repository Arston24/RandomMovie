package com.victorsysuev.randommovie.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.victorsysuev.randommovie.database.Movie
import ru.arston.randommovie.R


class FavoriteAdapter(private val movieList: List<Movie>) :
    RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val imageUrl: String = "http://image.tmdb.org/t/p/w500"
        var poster: ImageView = view.findViewById(R.id.item_movie_saved)

        fun bind(movie: Movie) {
            Glide.with(view)
                .load(imageUrl + movie.posterPath)
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(poster)


            view.setOnClickListener {
                val action =
                    FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailsFragment(
                        movie.id.toString()
                    )
                Navigation.findNavController(it).navigate(action)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_saved_card, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size
}