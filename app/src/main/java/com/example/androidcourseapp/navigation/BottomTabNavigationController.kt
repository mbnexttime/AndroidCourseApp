package com.example.androidcourseapp.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.androidcourseapp.Controller
import com.example.androidcourseapp.R
import com.example.androidcourseapp.utils.Item
import com.example.androidcourseapp.utils.RecyclerViewAdapterWithDelegates

class BottomTabNavigationController(
    val viewStub: ViewStub,
    private val callbacks: MainViewPagerCallbacks,
) {
    private var initialized = false

    private lateinit var viewHolder: BottomNavigationToolbarViewHolder

    private var callbackToUnsubscribeFromViewPager: Runnable? = null

    @SuppressLint("NotifyDataSetChanged")
    fun initialize(context: Context, tabs: List<Controller>) {
        if (initialized) {
            return
        }
        val container = viewStub.inflate() as ConstraintLayout
        viewHolder = BottomNavigationToolbarViewHolder(container)
        val items = ArrayList<BottomNavigationItem>()
        for (tab in tabs) {
            items.add(tab.getDataForBottomNavigationToolbar(context))
        }
        for (i in items.indices) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.bottom_navigation_item_layout, null)
            view.layoutParams = LinearLayout.LayoutParams(
                context.resources.getDimensionPixelSize(R.dimen.bottom_item_width),
                context.resources.getDimensionPixelSize(R.dimen.bottom_item_height),
            )

            val holder = BottomNavigationItemViewHolder(view)
            holder.image.setImageDrawable(items[i].image)
            holder.title.setText(items[i].title)
            view.setOnClickListener {
                callbacks.moveToPage(i)
                viewHolder.markAsChosen(i)
            }
            viewHolder.titlesContainer.addView(view)
            viewHolder.items.add(BottomNavigationItemViewHolder(view))
            Log.d(TAG, "current page is ${callbacks.getCurrentPage()}")
        }
        viewHolder.markAsChosen(callbacks.getCurrentPage())
        callbackToUnsubscribeFromViewPager = callbacks.subscribeOnPageChanges(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

                override fun onPageSelected(position: Int) {
                    viewHolder.markAsChosen(position)
                }

                override fun onPageScrollStateChanged(state: Int) = Unit
            }
        )
    }

    fun updateChosenMark() {
        viewHolder.markAsChosen(callbacks.getCurrentPage())
    }

    fun getView(context:    Context, tabs: List<Controller>): View {
        initialize(context, tabs)
        return viewHolder.container
    }

    fun getViewUnchecked(): View {
        return viewHolder.container
    }

    fun onDestroy() {
        callbackToUnsubscribeFromViewPager?.run()
        callbackToUnsubscribeFromViewPager = null
    }

    companion object {
        private val TAG = "BottomTabNavigationController"
    }
}