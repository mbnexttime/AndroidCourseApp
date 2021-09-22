package com.example.androidcourseapp.navigation

import androidx.viewpager.widget.ViewPager

interface MainViewPagerCallbacks {
    fun moveToPage(index: Int)

    fun getCurrentPage(): Int

    fun subscribeOnPageChanges(listener: ViewPager.OnPageChangeListener): Runnable
}