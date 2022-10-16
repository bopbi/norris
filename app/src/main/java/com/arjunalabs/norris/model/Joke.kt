package com.arjunalabs.norris.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Joke(
    val id: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    val url: String,
    val value: String
)
