package com.example.randommovie.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CastResponse {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("cast")
    @Expose
    var castList: List<Cast>? = null
}