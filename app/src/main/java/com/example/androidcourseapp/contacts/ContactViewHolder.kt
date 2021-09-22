package com.example.androidcourseapp.contacts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.R

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val avatarView = itemView.findViewById<ImageView>(R.id.contact_image)
    val nameView = itemView.findViewById<TextView>(R.id.contact_name)
}