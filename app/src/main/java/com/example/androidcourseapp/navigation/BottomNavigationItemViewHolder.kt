package com.example.androidcourseapp.navigation

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.R

class BottomNavigationItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val image = item.findViewById<ImageView>(R.id.tab_image)
    val title = item.findViewById<TextView>(R.id.tab_title)
    val mark = item.findViewById<FrameLayout>(R.id.bottom_navigation_toolbar_chosen_item_mark)
}