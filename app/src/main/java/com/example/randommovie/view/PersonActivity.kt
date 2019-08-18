package com.example.randommovie.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.DetailsActivity
import com.example.randommovie.models.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.android.synthetic.main.activity_person.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.randommovie.network.Api
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
    private lateinit var personMovieLabel: TextView

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    private val imageUrl = "http://image.tmdb.org/t/p/w500"
    private var movieList: List<PersonsCast> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        personName = findViewById(R.id.name_text)
        personType = findViewById(R.id.department_text)
        personBithday = findViewById(R.id.birthday_text)
        personBiography = findViewById(R.id.biography_text)
        profilePhoto = findViewById(R.id.profile_image)
        biographyLabel = findViewById(R.id.biography_label)
        personMovieLabel = findViewById(R.id.personMovieLabel)

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

                } else {
                    Log.e("Ошибка! ", response.errorBody().toString())
                }
            } catch (e: Exception) {
            }

            val personMovies = apiService.getPersonMovie(personID, apiKey)
            try {
                Log.e("Фильмы актера: ", "в тру")

                val response = personMovies.await()
                Log.e("Фильмы актера: ", "в respe")
                Log.e("Фильмы актера: ", response.body().toString())

                //Log.e("Фильмы актера: ", response.body()?.result!![0].name.toString())

                if (response.isSuccessful) {
                        movieList = response.body()?.cast!!
                    if (movieList.isNotEmpty()) {
                        //Log.e("Фильмы актера: ", movieList[0].name)

                        personMovieLabel.visibility = View.VISIBLE
                        //Log.e("Фильмы актера: ", movieList[0].name)

                        for (i in movieList.indices) {
                            var parent: View = layoutInflater.inflate(R.layout.cast_item, personMovie, false)
                            var photoCast: ImageView = parent.findViewById(R.id.cast_photo)
                            var nameCast: TextView = parent.findViewById(R.id.cast_name)
                            var characterCast: TextView = parent.findViewById(R.id.cast_character)

                            nameCast.text = movieList[i].original_title
                            characterCast.text = movieList[i].media_type
                            Glide.with(this@PersonActivity)
                                .load("http://image.tmdb.org/t/p/w500" + movieList[i].poster_path)
                                .diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                ).into(photoCast)

                            photoCast.setOnClickListener {
                                val intent = Intent(this@PersonActivity, DetailsActivity::class.java)
                                intent.putExtra("MovieID", movieList[i].id.toString())
                                this@PersonActivity.startActivity(intent)
                            }

                            personMovie.addView(parent)
                        }
                    }
                } else {
                    Log.e("Ошибка! ", response.errorBody().toString())
                }

            } catch (e: Exception) {
                println(e.message)

            }

        }
    }
}
