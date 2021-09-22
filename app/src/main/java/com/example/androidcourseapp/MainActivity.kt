package com.example.androidcourseapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewStub
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.androidcourseapp.contacts.ContactsTabController
import com.example.androidcourseapp.navigation.BottomTabNavigationController
import com.example.androidcourseapp.navigation.MainViewPagerCallbacksImpl
import com.example.androidcourseapp.utils.MainPagerAdapter

class MainActivity : Activity() {
    private lateinit var mainViewGroup: ViewGroup
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: MainPagerAdapter
    private var controllers: List<Controller> = emptyList()
    private lateinit var bottomTabNavigationController: BottomTabNavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewGroup = LayoutInflater.from(applicationContext)
            .inflate(R.layout.activity_main, null) as ViewGroup
        viewPager = mainViewGroup.findViewById(R.id.main_view_pager)

        setContentView(mainViewGroup)
        if (ControllersHolder.isInitialized()) {
            val controllers = ControllersHolder.getControllers()
            for (controller in controllers) {
                controller.invalidateContext(this)
            }
            this.controllers = controllers
            adapter = MainPagerAdapter(controllers)
            viewPager.adapter = adapter
        } else {
            val controllers = getTabs()
            ControllersHolder.setControllers(controllers)
            this.controllers = controllers
            adapter = MainPagerAdapter(controllers)
            viewPager.adapter = adapter
        }
        adapter.notifyDataSetChanged()

        val viewStub = mainViewGroup.findViewById<ViewStub>(R.id.bottom_navigation_toolbar)
        bottomTabNavigationController =
            BottomTabNavigationController(viewStub, MainViewPagerCallbacksImpl(viewPager))
        bottomTabNavigationController.initialize(this, controllers)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bottomTabNavigationController.updateChosenMark()
    }

    override fun onDestroy() {
        super.onDestroy()
        for (controller in controllers) {
            controller.onDestroy(isChangingConfigurations)
        }
    }


    private fun getTabs(): List<Controller> {
        Log.d(TAG, "creating tabs")
        return listOf(
            ContactsTabController(),
            ContactsTabController(),
            ContactsTabController(),
        )
    }

    companion object {
        private val TAG = "MainActivity"
    }
}