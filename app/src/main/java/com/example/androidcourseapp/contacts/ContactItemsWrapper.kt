package com.example.androidcourseapp.contacts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONArray

@JsonClass(generateAdapter = true)
data class ContactItemsWrapper(
    @Json(name = "contacts") val contacts: List<String>
)