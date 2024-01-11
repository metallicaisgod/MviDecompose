package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.example.mvidecomposetest.core.componentScope
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultContactListComponent(
    componentContext: ComponentContext,
    private val onEditContactRequested: (Contact) -> Unit,
    private val onAddContactRequested: () -> Unit,
) : ContactListComponent, ComponentContext by componentContext {

    private val repository = RepositoryImpl
    private val getContactsUseCase = GetContactsUseCase(repository)

    override val model: StateFlow<ContactListComponent.Model> = getContactsUseCase()
        .map { ContactListComponent.Model(it) }
        .stateIn(
            scope = componentScope(),
            started = SharingStarted.Lazily,
            initialValue = ContactListComponent.Model(listOf())
        )


    override fun onEditContactClicked(contact: Contact) {
        onEditContactRequested(contact)
    }

    override fun onAddContactClicked() {
        onAddContactRequested()
    }
}