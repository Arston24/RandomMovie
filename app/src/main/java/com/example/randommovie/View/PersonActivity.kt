package com.example.randommovie.View

import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.menu.ExpandedMenuView
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.Database.Movie
import com.example.randommovie.Models.Person
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import java.lang.Exception



class PersonActivity : AppCompatActivity() {

    private lateinit var personID: String
    private lateinit var personName: TextView
    private lateinit var personType: TextView
    private lateinit var personBithday: TextView
    private lateinit var personBiography: ExpandableTextView
    private lateinit var profilePhoto: ImageView
    private lateinit var biographyLabel: TextView

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    private val imageUrl = "http://image.tmdb.org/t/p/w500"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        personName = findViewById(R.id.name_text)
        personType = findViewById(R.id.department_text)
        personBithday = findViewById(R.id.birthday_text)
        personBiography = findViewById(R.id.biography_text)
        profilePhoto = findViewById(R.id.profile_image)
        biographyLabel = findViewById(R.id.biography_label)

        personID = intent.extras.getString("PersonID")

        getPerson()
    }

    fun getPerson() {

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        var apiService: Api = retrofit.create(Api::class.java)

        GlobalScope.launch(Dispatchers.Main) {

            val personDetails = apiService.getPersonDetails(personID, apiKey)
            try {
                val response = personDetails.await()

                if (response.isSuccessful) {
                    val personList = response.body()
                    personName.text = personList!!.name
                    personType.text = personList.knownForDepartment
                    if (personList.birthday!!.isNotEmpty()) {
                        personBithday.text = "День рождения: " + personList.birthday + ", " + personList.placeOfBirth
                    }
                    if (personList.biography!!.isNotEmpty()) {
                        biographyLabel.visibility = View.VISIBLE
                        personBiography.text = personList.biography + " "
                    }
                    Glide.with(this@PersonActivity).load(imageUrl + personList.profilePath).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).into(profilePhoto)

                }
            } catch (e: Exception) {
            }
        }
    }
}
