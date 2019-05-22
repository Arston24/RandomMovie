package com.example.randommovie.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Cast {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("cast")
    @Expose
    var result: List<Result>? = null


    inner class Result{

        @SerializedName("cast_id")
        @Expose
        var castId: Int? = null
        @SerializedName("character")
        @Expose
        var character: String? = null
        @SerializedName("credit_id")
        @Expose
        var creditId: String? = null
        @SerializedName("gender")
        @Expose
        var gender: Int? = null
        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("order")
        @Expose
        var order: Int? = null
        @SerializedName("profile_path")
        @Expose
        var profilePath: String? = null

    }
}