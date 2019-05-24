package ru.arston.randommovie

import android.app.ActionBar
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

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val actionBar = actionBar
//        val searchView = SearchView(this)
//        actionBar!!.customView = searchView
//        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        searchView.setQuery("test", true)
//        searchView.isFocusable = true
//        searchView.isIconified = false
//        searchView.requestFocusFromTouch()

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as android.widget.SearchView
            searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra("MovieName", query)
                    this@MainActivity.startActivity(intent)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    return true
                }

            })

        }

        return true

    }

}
