package com.example.randommovie.Database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class Movie(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "posterPath") val posterPath: String?,
    @ColumnInfo(name = "backdropPath") val backdropPath: String?,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "genre") val genre: String?
)