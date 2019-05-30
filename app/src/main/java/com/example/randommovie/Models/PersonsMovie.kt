package com.example.randommovie.Models

data class PersonsMovie(
    val cast: List<PersonsCast>,
    val crew: List<Crew>,
    val id: Int
)