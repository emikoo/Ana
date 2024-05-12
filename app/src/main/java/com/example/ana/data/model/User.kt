package com.example.ana.data.model

data class User(
    val name: String,
    val phone: String,
    val children: MutableList<Child>? = null,
    val photo: String? = null
)
