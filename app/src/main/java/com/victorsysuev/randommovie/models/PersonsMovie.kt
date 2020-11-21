package com.victorsysuev.randommovie.models

data class PersonsMovie(
    val cast: List<PersonsCast>,
    val crew: List<Crew>,
    val id: Int
)