package com.victorsysuev.randommovie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.victorsysuev.randommovie.models.PersonsCast
import com.victorsysuev.randommovie.network.Api
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentPersonBinding


class PersonFragment : Fragment() {
    lateinit var binding: FragmentPersonBinding
    private lateinit var personID: String

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    private val imageUrl = "http://image.tmdb.org/t/p/w500"
    private var movieList: List<PersonsCast> = listOf()

    val args: PersonFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        getPerson()
        return binding.root

    }

    private fun getPerson() {
        personID = args.personId

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val apiService: Api = retrofit.create(Api::class.java)

        GlobalScope.launch(Dispatchers.Main) {

            val personDetails = apiService.getPersonDetails(personID, apiKey)
            try {
                val response = personDetails.await()

                if (response.isSuccessful) {
                    val personList = response.body()
                    binding.nameText.text = personList!!.name
                    binding.departmentText.text = personList.knownForDepartment
                    if (personList.birthday!!.isNotEmpty()) {
                        binding.birthdayText.text =
                            "День рождения: " + personList.birthday + ", " + personList.placeOfBirth
                    }
                    if (personList.biography!!.isNotEmpty()) {
                        binding.biographyLabel.visibility = View.VISIBLE
                        binding.biographyText.text = personList.biography + " "
                    }
                    Glide.with(this@PersonFragment).load(imageUrl + personList.profilePath)
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(binding.profileImage)

                } else {
                    Log.e("Ошибка! ", response.errorBody().toString())
                }
            } catch (e: Exception) {
            }

            val personMovies = apiService.getPersonMovie(personID, apiKey)
            try {

                val response = personMovies.await()
                if (response.isSuccessful) {
                    movieList = response.body()?.cast!!
                    if (movieList.isNotEmpty()) {
                        personMovieLabel.visibility = View.VISIBLE

                        for (i in movieList.indices) {
                            var parent: View =
                                layoutInflater.inflate(R.layout.item_cast, personMovie, false)
                            var photoCast: ImageView = parent.findViewById(R.id.cast_photo)
                            var nameCast: TextView = parent.findViewById(R.id.cast_name)
                            var characterCast: TextView = parent.findViewById(R.id.cast_character)

                            nameCast.text = movieList[i].original_title
                            characterCast.text = movieList[i].media_type
                            Glide.with(this@PersonFragment)
                                .load("http://image.tmdb.org/t/p/w500" + movieList[i].poster_path)
                                .diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                ).into(photoCast)

//                            photoCast.setOnClickListener {
//                                val intent = Intent(this@PersonFragment, DetailsFragment::class.java)
//                                intent.putExtra("MovieID", movieList[i].id.toString())
//                                this@PersonFragment.startActivity(intent)
//                            }

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
