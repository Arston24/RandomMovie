package com.victorsysuev.randommovie.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @field:SerializedName("id") val id: Int,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("vote_average") val voteAverage: Double?,
    @field:SerializedName("popularity") val popularity: Double?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("genre") val genre: String?,
    var isFavorite: Boolean = false
)