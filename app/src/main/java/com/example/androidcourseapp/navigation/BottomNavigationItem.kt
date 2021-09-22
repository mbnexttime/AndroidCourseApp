package com.example.androidcourseapp.navigation

import android.graphics.drawable.Drawable
import com.example.androidcourseapp.utils.Item

data class BottomNavigationItem(
    val image: Drawable,
    val title: String,
) : Item
