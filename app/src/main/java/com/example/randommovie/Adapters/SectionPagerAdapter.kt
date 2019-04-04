package ru.arston.randommovie.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.arston.randommovie.RandomActivity
import ru.arston.randommovie.TopActivity

class SectionPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position){
            0 -> {
                return RandomActivity()
            }
            1 -> {
                return TopActivity()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "ПОДОБРАТЬ"
            1 -> return "ПОПУЛЯРНОЕ"
        }
        return null
    }

}