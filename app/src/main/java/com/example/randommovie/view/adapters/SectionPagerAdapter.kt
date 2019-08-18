package ru.arston.randommovie.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.randommovie.SavedActivity
import ru.arston.randommovie.RandomActivity
import ru.arston.randommovie.TopActivity
import kotlin.contracts.Returns

class SectionPagerAdapter(fm: androidx.fragment.app.FragmentManager?) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
        when (position){
            0 -> {
                return RandomActivity()
            }
            1 -> {
                return TopActivity()
            }
            2-> {
                return SavedActivity()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "ПОДОБРАТЬ"
            1 -> return "ПОПУЛЯРНОЕ"
            2 -> return "ИЗБРАННОЕ"
        }
        return null
    }

}