package com.example.mvidecomposetest.presentation

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface EditContactComponent {

    val model: StateFlow<Model>

    fun onUserNameChanged(userName: String)

    fun onPhoneChanged(phone: String)

    fun saveUser()

    @Serializable
    data class Model(
        val userName: String,
        val phone: String
    )
}