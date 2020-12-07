package com.example.trip_friend.database.model

import java.io.Serializable

data class Review(
    var writer: String,
    var comment: MutableMap<String, Map<String, String>>,
    var like: Map<String, Boolean>,
    var image: Map<String, String>,
    var number: String
) : Serializable {
    constructor() : this(
        "",
        hashMapOf<String, Map<String, String>>(),
        hashMapOf<String, Boolean>(),
        hashMapOf<String, String>(),
        ""
    )
}