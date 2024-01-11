package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultEditContactComponent(
    componentContext: ComponentContext,
    private val contact: Contact,
    private val onContactSaved: () -> Unit,
) : EditContactComponent, ComponentContext by componentContext {

    private val repository = RepositoryImpl

    private val editContactUseCase = EditContactUseCase(repository)

    private val _model = MutableStateFlow(
        stateKeeper.consume(key = KEY, strategy = EditContactComponent.Model.serializer())
            ?: EditContactComponent.Model(
                userName = contact.username, phone = contact.phone
            )
    )

    init {
        stateKeeper.register(key = KEY, strategy = EditContactComponent.Model.serializer()) {
            model.value
        }
    }

    override val model: StateFlow<EditContactComponent.Model>
        get() = _model.asStateFlow()

    override fun onUserNameChanged(userName: String) {
        _model.value = model.value.copy(userName = userName)
    }

    override fun onPhoneChanged(phone: String) {
        _model.value = model.value.copy(phone = phone)
    }

    override fun saveUser() {
        val (userName, phone) = model.value
        editContactUseCase(
            contact.copy(
                username = userName,
                phone = phone
            )
        )
        onContactSaved()
    }

    companion object {
        private const val KEY = "DefaultEditContactComponent"
    }
}