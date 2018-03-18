package ru.spbau.mit.dictionary.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class PagerAdapter(fm: FragmentManager, private var mNumOfTabs: Int)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return when (position) {
            0 -> TabStudy()
            1 -> TabStudied()
            else -> null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}