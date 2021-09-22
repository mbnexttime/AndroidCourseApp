package com.example.androidcourseapp

import android.content.Context
import android.view.View

interface Controller {
    fun getView(context: Context): View

    fun initialize(context: Context)

    fun invalidateContext(newContext: Context)

    fun onCreate()

    fun onDestroy()
}