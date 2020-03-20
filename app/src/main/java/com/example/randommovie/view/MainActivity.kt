package ru.arston.randommovie


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.randommovie.view.SearchActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.arston.randommovie.Adapters.SectionPagerAdapter
import ru.arston.randommovie.databinding.ActivityMainBinding
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var mSectionsPagerAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        (application as App).preferenceRepository
            .nightModeLive.observe(this, Observer { nightMode ->
            nightMode?.let { AppCompatDelegate.setDefaultNightMode(it) }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nightMode -> {
                val preferenceRepository = (application as App).preferenceRepository
                preferenceRepository.isDarkTheme =
                    preferenceRepository.nightMode != AppCompatDelegate.MODE_NIGHT_YES
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_icon, menu)
//        val searchItem = menu.findItem(R.id.search)
//        if (searchItem != null) {
//
//            val searchView: SearchView = searchItem.actionView as SearchView
//            val searchManager: SearchManager =
//                this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(
//                    ComponentName(
//                        applicationContext,
//                        MainActivity::class.java!!
//                    )
//                )
//            )
//
//            searchView.onActionViewExpanded()
//            searchView.requestFocus()
//            searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView(this@MainActivity),
//                SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
//                    intent.putExtra("MovieName", query)
//                    this@MainActivity.startActivity(intent)
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//
//                    return true
//                }
//
//            })
//
//        }

        return true

    }

}

