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

    private val view2Controller = HashMap<View, Controller>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.e("kirill", position.toString())
        val controller = controllers[position]
        val view: View = controller.getView(container.context)
        view2Controller[view] = controller
        insertView(container, position, view)
        return view
    }

    private fun insertView(
        container: ViewGroup,
        position: Int,
        view: View
    ) {
        for (i in 0 until container.childCount) {
            val challenge = view2Controller[container.getChildAt(i)] ?: continue
            if (position < controllers.indexOf(challenge)) {
                container.addView(view, i)
                return
            }
        }
        container.addView(view)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}