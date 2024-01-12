package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase

class EditContactStoreFactory(
    private val storeFactory: StoreFactory,
    private val editContactUseCase: EditContactUseCase
) {

    fun create(contact: Contact): EditContactStore {

        return object : EditContactStore,
            Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> by
            storeFactory.create(
                name = "EditContactStore",
                initialState = EditContactStore.State(
                    id = contact.id,
                    userName = contact.username,
                    phone = contact.phone
                ),
                reducer = ReducerImpl(),
                executorFactory = ::ExecutorImpl
            ) {}
    }
    private sealed interface Action

    private sealed interface Msg {

        data class ChangeUserName(val userName: String): Msg

        data class ChangePhone(val phone: String): Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<EditContactStore.Intent, Action,
                EditContactStore.State, Msg, EditContactStore.Label>() {

        override fun executeIntent(
            intent: EditContactStore.Intent,
            getState: () -> EditContactStore.State,
        ) {
            when (intent) {
                is EditContactStore.Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(intent.phone))
                }
                is EditContactStore.Intent.ChangeUserName -> {
                    dispatch(Msg.ChangeUserName(intent.userName))
                }
                EditContactStore.Intent.SaveContact -> {
                    val state = getState()
                    editContactUseCase(contact = Contact(
                        id = state.id,
                        username = state.userName,
                        phone = state.phone
                    ))
                    publish(EditContactStore.Label.ContactSaved)
                }
            }
        }
    }

    private class ReducerImpl: Reducer<EditContactStore.State, Msg> {

        override fun EditContactStore.State.reduce(msg: Msg) = when(msg) {
            is Msg.ChangePhone -> {
                copy(phone = msg.phone)
            }
            is Msg.ChangeUserName -> {
                copy(userName = msg.userName)
            }
        }
    }
}