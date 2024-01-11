package com.example.mvidecomposetest.domain

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Int = -1,
    val username: String,
    val phone: String,
)
