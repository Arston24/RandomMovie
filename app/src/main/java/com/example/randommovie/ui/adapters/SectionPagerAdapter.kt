package ru.arston.randommovie.Adapters

import androidx.fragment.app.Fragment
import com.example.randommovie.SavedFragment
import com.example.randommovie.ui.random.RandomMoviesFragment
import com.example.randommovie.ui.popular.TopMoviesFragment

class SectionPagerAdapter(fm: androidx.fragment.app.FragmentManager?) :
    androidx.fragment.app.FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return RandomMoviesFragment()
            }
            1 -> {
                return TopMoviesFragment()
            }
            else -> {
                return SavedFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "ПОДОБРАТЬ"
            1 -> return "ПОПУЛЯРНОЕ"
            2 -> return "ИЗБРАННОЕ"
        }
        return null
    }

}