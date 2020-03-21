package com.example.randommovie.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @field:SerializedName("id") val uid: Int,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("posterPath") val posterPath: String?,
    @field:SerializedName("backdropPath") val backdropPath: String?,
    @field:SerializedName("rating") val rating: Double?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("releaseDate") val releaseDate: String,
    @field:SerializedName("genre") val genre: String?
)