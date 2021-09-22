package com.example.androidcourseapp.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.R
import com.example.androidcourseapp.utils.Delegate
import com.example.androidcourseapp.utils.Item

class ContactDelimeterDelegate : Delegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return DelimViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.delim_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
        return
    }

    override fun match(item: Item): Boolean {
        return item is DelimItem
    }
}

class DelimItem : Item