package com.example.androidcourseapp.contacts

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.utils.Item
import com.example.androidcourseapp.utils.RecyclerViewAdapterWithDelegates

class ContactsTabViewHolder(
    private val recycler: RecyclerView
) {
    private lateinit var adapterWithDelegates: RecyclerViewAdapterWithDelegates
    fun getView(): View {
        return recycler
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(data: List<Item>) {
        adapterWithDelegates = RecyclerViewAdapterWithDelegates(
            data, listOf(
                ContactViewHolderDelegate(),
                ContactDelimeterDelegate()
            )
        )
        recycler.adapter = adapterWithDelegates
        adapterWithDelegates.notifyDataSetChanged()
    }
}