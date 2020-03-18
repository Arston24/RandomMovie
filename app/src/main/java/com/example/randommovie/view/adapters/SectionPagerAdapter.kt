package ru.arston.randommovie.Adapters

import androidx.fragment.app.Fragment
import com.example.randommovie.SavedActivity
import ru.arston.randommovie.RandomActivity
import ru.arston.randommovie.TopActivity

class SectionPagerAdapter(fm: androidx.fragment.app.FragmentManager?) :
    androidx.fragment.app.FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return RandomActivity()
            }
            1 -> {
                return TopActivity()
            }
            else -> {
                return SavedActivity()
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