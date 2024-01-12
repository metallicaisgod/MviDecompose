package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Store

interface EditContactStore :
    Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> {

    data class State(
        val id: Int,
        val userName: String,
        val phone: String,
    )

    sealed interface Label {

        object ContactSaved: Label
    }

    sealed interface Intent {

        data class ChangeUserName(val userName: String): Intent

        data class ChangePhone(val phone: String): Intent

        object SaveContact: Intent
    }
}