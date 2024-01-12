package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetest.domain.AddContactUseCase

class AddContactStoreFactory(
    private val storeFactory: StoreFactory,
    private val addContactUseCase: AddContactUseCase,
) {

    fun create(): AddContactStore = object : AddContactStore,
        Store<AddContactStore.Intent, AddContactStore.State, AddContactStore.Label> by
        storeFactory.create(
            name = "AddContactStore",
            initialState = AddContactStore.State(userName = "", phone = ""),
            reducer = ReducerImpl(),
            executorFactory = ::ExecutorImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeUserName(val userName: String) : Msg

        data class ChangePhone(val phone: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<AddContactStore.Intent, Action,
                AddContactStore.State, Msg, AddContactStore.Label>() {

        override fun executeIntent(
            intent: AddContactStore.Intent,
            getState: () -> AddContactStore.State,
        ) {
            when (intent) {
                is AddContactStore.Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(intent.phone))
                }
                is AddContactStore.Intent.ChangeUserName -> {
                    dispatch(Msg.ChangeUserName(intent.userName))
                }
                AddContactStore.Intent.SaveContact -> {
                    val state = getState()
                    addContactUseCase(state.userName, state.phone)
                    publish(AddContactStore.Label.ContactSaved)
                }
            }
        }
    }

    private class ReducerImpl : Reducer<AddContactStore.State, Msg> {
        override fun AddContactStore.State.reduce(msg: Msg) = when (msg) {
            is Msg.ChangePhone -> {
                copy(phone = msg.phone)
            }
            is Msg.ChangeUserName -> {
                copy(userName = msg.userName)
            }
        }
    }
}