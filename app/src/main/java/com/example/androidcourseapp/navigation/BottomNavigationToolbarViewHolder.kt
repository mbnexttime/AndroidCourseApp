package com.example.androidcourseapp.navigation

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.R

class BottomNavigationToolbarViewHolder(
    val container: ConstraintLayout,
) {
    val items: ArrayList<BottomNavigationItemViewHolder> = ArrayList()
    val scroller =
        container.findViewById<HorizontalScrollView>(R.id.bottom_navigation_toolbar_scroller)
    val titlesContainer =
        scroller.findViewById<LinearLayout>(R.id.bottom_navigation_toolbar_titles_container)

    fun markAsChosen(index: Int) {
        for (item in items) {
            item.mark.visibility = View.INVISIBLE
        }
        items[index].mark.visibility = View.VISIBLE
    }
}