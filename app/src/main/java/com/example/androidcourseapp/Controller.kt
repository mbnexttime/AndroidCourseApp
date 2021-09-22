package com.example.androidcourseapp

import android.content.Context
import android.view.View
import com.example.androidcourseapp.navigation.BottomNavigationItem

interface Controller {
    fun getView(context: Context): View

    fun getViewUncheck(): View

    fun initialize(context: Context)

    fun invalidateContext(newContext: Context)

    fun onCreate()

    fun onDestroy(isTemporary: Boolean)

    fun getDataForBottomNavigationToolbar(context: Context): BottomNavigationItem
}