package com.example.mvidecomposetest.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mvidecomposetest.presentation.AddContactComponent
import com.example.mvidecomposetest.presentation.EditContactComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContact(
    component: AddContactComponent,
) {
    val model by component.model.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.userName,
            placeholder = {
                Text(text = "Username:")
            },
            onValueChange = { component.onUserNameChanged(it) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.phone,
            placeholder = {
                Text(text = "Phone:")
            },
            onValueChange = { component.onPhoneChanged(it) }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                component.saveUser()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContact(
    component: EditContactComponent,
) {
    val model by component.model.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.userName,
            placeholder = {
                Text(text = "Username:")
            },
            onValueChange = { component.onUserNameChanged(it) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.phone,
            placeholder = {
                Text(text = "Phone:")
            },
            onValueChange = { component.onPhoneChanged(it) }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                component.saveUser()
            }
        ) {
            Text(text = "Save")
        }
    }
}
