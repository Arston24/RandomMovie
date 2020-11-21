package ru.arston.randommovie.Models
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Genre{

    @SerializedName("genres")
    @Expose
    var genres: List<Attributes>? = null

    inner class Attributes {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null

    }

}
