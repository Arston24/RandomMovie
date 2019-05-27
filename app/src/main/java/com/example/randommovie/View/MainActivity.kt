package ru.arston.randommovie


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import com.example.randommovie.View.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.arston.randommovie.Adapters.SectionPagerAdapter
import android.content.ComponentName
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
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
