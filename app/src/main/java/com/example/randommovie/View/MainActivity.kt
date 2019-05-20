package ru.arston.randommovie

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.randommovie.DetailsActivity
import com.example.randommovie.View.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.arston.randommovie.Adapters.SectionPagerAdapter

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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.options_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.search) {
//
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        if(searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra("MovieName", newText)
                    this@MainActivity.startActivity(intent)
                    return true
                }

            })
        }
        //val searchView = searchItem.getActionView() as SearchView
//        val mlFragment: TopActivity = findViewById(R.id.)
//        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                return true
//            }
//
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                val mlFragment =
//                    supportFragmentManager.findFragmentById(R.id.fragment_listing) as MoviesListingFragment?
//                mlFragment!!.searchViewBackButtonClicked()
//                return true
//            }
//        })

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(q: String): Boolean {
//                update(q)
//                return false
//            }
//
//            override fun onQueryTextSubmit(q: String): Boolean {
//                return false
//            }
//        })

        return true

    }
}
