package com.example.androidcourseapp.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidcourseapp.R
import com.example.androidcourseapp.utils.Delegate
import com.example.androidcourseapp.utils.Item

class ContactViewHolderDelegate : Delegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
        val contactViewHolder: ContactViewHolder = holder as? ContactViewHolder ?: return
        val data: ContactItem = item as? ContactItem ?: return


        contactViewHolder.nameView.text = data.userName
        Glide.with(contactViewHolder.avatarView).load(data.avatarUrl).into(contactViewHolder.avatarView)
    }

    override fun match(item: Item): Boolean {
        return item is ContactItem
    }
}