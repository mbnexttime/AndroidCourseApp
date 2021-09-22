package com.example.androidcourseapp.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.androidcourseapp.Controller

class MainPagerAdapter(
    private val controllers: List<Controller>
) : PagerAdapter() {
    override fun getCount(): Int {
        return controllers.size
    }
    private val attached = HashMap<Controller, Boolean>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val controller = controllers[position]
        if (attached[controller] == true) {
            return Any()
        }
        val view: View = controller.getView(container.context)
        container.addView(view, position)
        attached[controller] = true
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = Unit
}