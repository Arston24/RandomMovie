package com.victorsysuev.randommovie.ui.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.victorsysuev.randommovie.database.Movie
import ru.arston.randommovie.databinding.ItemTopMovieListBinding

class TopMovieViewHolder(binding: ItemTopMovieListBinding) : RecyclerView.ViewHolder(binding.root) {
    val viewModel = ViewModel()

    init {
        binding.data = viewModel
    }

    companion object {
        fun create(container: ViewGroup): TopMovieViewHolder {
            val binding = ItemTopMovieListBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
            return TopMovieViewHolder(binding)
        }
    }

    class ViewModel {
        val releaseDate = ObservableField<String>()
        val title = ObservableField<String>()
        val rating = ObservableField<String>()
        val genre = ObservableField<String>()
        val poster = ObservableField<String>()

        fun bind(movie: Movie) {
            releaseDate.set(movie.releaseDate)
            title.set(movie.title)
            rating.set(movie.voteAverage.toString())
            genre.set(movie.genre)
            poster.set(movie.posterPath)
        }
    }

}