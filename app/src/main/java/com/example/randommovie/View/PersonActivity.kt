package com.example.randommovie.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.arston.randommovie.R

class PersonActivity : AppCompatActivity() {

    private lateinit var personID: String
    private lateinit var personName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        personName = findViewById(R.id.name_text)

        personID = intent.extras.getString("PersonID")
        personName.text = personID


    }
}
