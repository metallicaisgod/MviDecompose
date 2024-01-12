package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.launch

class ContactListStoreFactory {

    private val storeFactory: StoreFactory = DefaultStoreFactory()
    private val getContactsUseCase: GetContactsUseCase = GetContactsUseCase(RepositoryImpl)

    fun create(): ContactListStore = object : ContactListStore,
        Store<ContactListStore.Intent, ContactListStore.State, ContactListStore.Label> by
        storeFactory.create(
            name = "ContactListStore",
            initialState = ContactListStore.State(listOf()),
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl
        ){}

    private sealed interface Action {

        data class ContactListLoaded(val contacts: List<Contact>): Action
    }

    private sealed interface Msg {

        data class ContactListLoaded(val contacts: List<Contact>): Msg
    }

    private object ReducerImpl: Reducer<ContactListStore.State, Msg>{

        override fun ContactListStore.State.reduce(msg: Msg): ContactListStore.State {
            return when(msg) {
                is Msg.ContactListLoaded -> {
                    copy(contactList = msg.contacts)
                }
            }
        }
    }

    private inner class ExecutorImpl: CoroutineExecutor<ContactListStore.Intent, Action, ContactListStore.State, Msg, ContactListStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ContactListStore.State) {
            when(action) {
                is Action.ContactListLoaded -> {
                    dispatch(message = Msg.ContactListLoaded(action.contacts))
                }
            }
        }

        override fun executeIntent(
            intent: ContactListStore.Intent,
            getState: () -> ContactListStore.State,
        ) {
            when(intent) {
                ContactListStore.Intent.AddContact -> {
                    publish(ContactListStore.Label.AddContact)
                }
                is ContactListStore.Intent.EditContact -> {
                    publish(ContactListStore.Label.EditContact(intent.contact))
                }
            }
        }
    }

    private inner class BootstrapperImpl: CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getContactsUseCase().collect{
                    dispatch(Action.ContactListLoaded(it))
                }
            }
        }
    }
}