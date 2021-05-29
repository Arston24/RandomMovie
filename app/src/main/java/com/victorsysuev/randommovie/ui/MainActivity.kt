package ru.arston.randommovie


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.victorsysuev.randommovie.util.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.arston.randommovie.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        setContentView(R.layout.activity_main)
//        (application as App).preferenceRepository
//            .nightModeLive.observe(this, Observer { nightMode ->
//                nightMode?.let { AppCompatDelegate.setDefaultNightMode(it) }
//            })

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setupBottomNavigationBar()

    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.random, R.navigation.top_list, R.navigation.favorite)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}

