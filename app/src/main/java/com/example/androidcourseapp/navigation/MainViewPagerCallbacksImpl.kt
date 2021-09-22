package com.example.androidcourseapp.navigation

import androidx.viewpager.widget.ViewPager
import java.lang.Math.abs

class MainViewPagerCallbacksImpl(
    private val mainViewPager: ViewPager
) : MainViewPagerCallbacks {
    override fun moveToPage(index: Int) {
        mainViewPager.setCurrentItem(index, true)
    }

    override fun getCurrentPage(): Int {
        return mainViewPager.currentItem
    }

    override fun subscribeOnPageChanges(listener: ViewPager.OnPageChangeListener): Runnable {
        mainViewPager.addOnPageChangeListener(listener)
        return Runnable {
            mainViewPager.removeOnPageChangeListener(listener)
        }
    }
}