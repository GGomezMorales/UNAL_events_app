package com.unalminas.eventsapp.presentation.screens.attendants.adapter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unalminas.eventsapp.R
import com.unalminas.eventsapp.domain.Attendant
import com.unalminas.eventsapp.presentation.Screen
import com.unalminas.eventsapp.presentation.myComposables.InfoDialogContent
import com.unalminas.eventsapp.presentation.screens.Attendants.AttendantScreenViewModel
import com.unalminas.eventsapp.presentation.ui.theme.NunitoFont
import com.unalminas.eventsapp.presentation.ui.theme.OxfordBlue
import com.unalminas.eventsapp.presentation.ui.theme.Platinum
import com.unalminas.eventsapp.presentation.ui.theme.Snow
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun AttendantChart(
    eventListState: List<Attendant> = listOf(),
    navController: NavController,
    attendantViewModel: AttendantScreenViewModel = hiltViewModel(),
) {

    var dialogState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OxfordBlue, RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
            .padding(top = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Platinum, RoundedCornerShape(30))
                .padding(12.dp)
                .clip(shape = RoundedCornerShape(30)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "#",
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Black,
                color = OxfordBlue
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                text = stringResource(id = R.string.name_attendant),
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Black,
                color = OxfordBlue
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.identification_attendant),
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Black,
                color = OxfordBlue
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.email_attendant),
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Black,
                color = OxfordBlue
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            var currentAttendant = Attendant()
            itemsIndexed(items = eventListState) { index, item ->

                val delete = SwipeAction(
                    onSwipe = {
                        currentAttendant = item
                        dialogState = true
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.padding(16.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    background = Color.Transparent
                )
                if (dialogState) {
                    Dialog(
                        onDismissRequest = { dialogState = false },
                        content = {
                            InfoDialogContent(
                                R.string.message_delete_attendant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onDeleteClick = {
                                    currentAttendant.id?.let { nonNullId ->
                                        attendantViewModel.deleteAttendantById(nonNullId)
                                    }
                                    dialogState = false
                                },
                                onCancel = { dialogState = false }
                            )
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        )
                    )
                }
                SwipeableActionsBox(
                    swipeThreshold = 50.dp,
                    endActions = listOf(delete),
                    backgroundUntilSwipeThreshold = Color.Transparent
                ) {
                    CardAttendant(
                        modifier = Modifier
                            .background(Snow, RoundedCornerShape(30))
                            .clickable {
                                item.id?.let { nonNullId ->
                                    val screen = Screen.EditAttendantScreen(nonNullId.toString())
                                    navController.navigate(screen.createRoute())
                                }
                            }, index + 1, item
                    )
                }
            }
        }
    }
}
