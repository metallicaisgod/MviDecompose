package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetest.core.componentScope
import com.example.mvidecomposetest.domain.Contact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultContactListComponent(
    componentContext: ComponentContext,
    private val onEditContactRequested: (Contact) -> Unit,
    private val onAddContactRequested: () -> Unit,
) : ContactListComponent, ComponentContext by componentContext {

    private lateinit var store: ContactListStore

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    ContactListStore.Label.AddContact -> {
                        onAddContactRequested()
                    }
                    is ContactListStore.Label.EditContact -> {
                        onEditContactRequested(it.contact)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ContactListStore.State>
        get() = store.stateFlow


    override fun onEditContactClicked(contact: Contact) {
        store.accept(ContactListStore.Intent.EditContact(contact))
    }

    override fun onAddContactClicked() {
        store.accept(ContactListStore.Intent.AddContact)
    }
}