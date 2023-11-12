package com.unalminas.eventsapp.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.unalminas.eventsapp.domain.EventFieldEnum
import com.unalminas.eventsapp.presentation.screens.FormEventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FormEvent(
    navController: NavHostController,
    id: Int? = null,
    isNewEvent: Boolean,
    viewModel: FormEventViewModel = hiltViewModel(),
) {
    val event by viewModel.eventState.collectAsState()

    LaunchedEffect(isNewEvent) {
        if (!isNewEvent) id?.let {
            viewModel.getEventById(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            navController.navigate("MainScreen") {
                popUpTo("MainScreen") { inclusive = true }
            }
        }) {
            Text(text = "volver", fontSize = 14.sp)
        }

        Text(
            text = "Crear un nuevo evento",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = event.name,
            onValueChange = { newName ->
                viewModel.editEventField(EventFieldEnum.NAME, newName)
            },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = event.description,
            onValueChange = { newDescription ->
                viewModel.editEventField(EventFieldEnum.DESCRIPTION, newDescription)
            },
            label = { Text("Descripción") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = event.name,
            onValueChange = { newDate ->
                viewModel.editEventField(EventFieldEnum.DATE, newDate)
            },
            label = { Text("Fecha") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = event.hour,
            onValueChange = { newHour ->
                viewModel.editEventField(EventFieldEnum.HOUR, newHour)
            },
            label = { Text("Horario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = event.place,
            onValueChange = { newPlace ->
                viewModel.editEventField(EventFieldEnum.PLACE, newPlace)
            },
            label = { Text("Lugar") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isNewEvent) {
                    viewModel.insertEvent(event)
                } else {
                    viewModel.updateEvent(event)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        navController.navigate("MainScreen") {
                            popUpTo("MainScreen") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text("Save")
        }
    }
}