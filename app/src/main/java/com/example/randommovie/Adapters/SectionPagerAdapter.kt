package ru.arston.randommovie.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.randommovie.SavedActivity
import ru.arston.randommovie.RandomActivity
import ru.arston.randommovie.TopActivity
import kotlin.contracts.Returns

class SectionPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
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