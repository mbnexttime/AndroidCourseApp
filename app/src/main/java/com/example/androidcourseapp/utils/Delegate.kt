package com.example.androidcourseapp.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.contacts.ContactViewHolder

interface Delegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)

    fun match(item: Item): Boolean
}