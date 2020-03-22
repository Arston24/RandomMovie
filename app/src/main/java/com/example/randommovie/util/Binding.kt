package com.example.randommovie.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.arston.randommovie.R

@BindingAdapter("date")
fun TextView.setMovieYear(date: String?) {
    date?.let {
        text = date.split("-")[0]
    }
}

@BindingAdapter("posterUrl")
fun ImageView.setMoviePoster(posterUrl: String?) {
    val baseUrl: String = "http://image.tmdb.org/t/p/w500"
    Glide.with(this)
        .load(baseUrl + posterUrl)
        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
        .into(this)
}