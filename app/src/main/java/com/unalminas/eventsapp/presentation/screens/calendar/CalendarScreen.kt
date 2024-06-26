package com.unalminas.eventsapp.presentation.screens.calendar

import android.widget.CalendarView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unalminas.eventsapp.R
import com.unalminas.eventsapp.domain.Event
import com.unalminas.eventsapp.presentation.Screen
import com.unalminas.eventsapp.presentation.ui.theme.BlueGreen
import com.unalminas.eventsapp.presentation.ui.theme.NunitoFont
import com.unalminas.eventsapp.presentation.ui.theme.OxfordBlue
import com.unalminas.eventsapp.presentation.ui.theme.Platinum
import com.unalminas.eventsapp.presentation.ui.theme.Snow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val selectDate by viewModel.formattedDataState.collectAsState()
    val selectDateOnlyDay by viewModel.formattedDateStateOnlyDay.collectAsState()
    val currentDate by viewModel.currentDateState.collectAsState()
    val eventList by viewModel.eventListWithAttendantCountState.collectAsState()

    LaunchedEffect(currentDate) {
        viewModel.getEventsWithAttendantCount(currentDate)
    }

    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(OxfordBlue, Snow),
                    startY = 280f,
                    endY = 285f
                )
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 38.dp, top = 16.dp, bottom = 16.dp),
            text = stringResource(R.string.calendar),
            fontFamily = NunitoFont,
            fontWeight = FontWeight.Black,
            color = Snow,
            style = MaterialTheme.typography.titleLarge,
        )
        CustomCalendarView()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .background(
                    OxfordBlue,
                    RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
                .padding(top = 16.dp)
                .clip(shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))

        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(20)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Platinum)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = selectDate,
                            fontFamily = NunitoFont,
                            fontWeight = FontWeight.Bold,
                            color = OxfordBlue,
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        )
                        Text(
                            text = selectDateOnlyDay,
                            fontFamily = NunitoFont,
                            fontWeight = FontWeight.Bold,
                            color = OxfordBlue,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = currentDate,
                            fontFamily = NunitoFont,
                            fontWeight = FontWeight.Bold,
                            color = OxfordBlue,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    IconButton(
                        modifier = Modifier,
                        onClick = {
                            val screen = Screen.CreateEventScreenWithDate(currentDate)
                            navController.navigate(screen.createRoute())
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(38.dp),
                            imageVector = Icons.Filled.PostAdd,
                            contentDescription = "create event",
                            tint = OxfordBlue
                        )
                    }
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(eventList) { event ->
                    EventItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        event = event,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun CustomCalendarView(
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val calendarView = Calendar.getInstance()
    val selectedDate by remember { mutableStateOf(calendarView) }

    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    selectedDate.set(year, month, dayOfMonth)

                    val formattedDate = formatDate(dayOfMonth, month, year)
                    val formattedDateOnlyDay = formatDateOnlyDay(dayOfMonth, month, year)

                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val dateString = dateFormat.format(selectedDate.time)

                    viewModel.setDateSelected(formattedDate)
                    viewModel.setDateSelectedOnlyDay(formattedDateOnlyDay)
                    viewModel.setCurrentDate(dateString)

                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Snow, RoundedCornerShape(topStartPercent = 11, topEndPercent = 11))
            .padding(top = 8.dp)
            .clip(shape = RoundedCornerShape(topStartPercent = 11, topEndPercent = 11)),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EventItem(
    event: Event,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Snow)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.90f)
                            .basicMarquee(),
                        text = stringResource(
                            id = R.string.event_name_format,
                            event.name
                        ),
                        color = OxfordBlue,
                        fontFamily = NunitoFont,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = stringResource(id = R.string.event_date_format, event.date),
                        color = OxfordBlue,
                        fontFamily = NunitoFont,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = stringResource(id = R.string.event_hour_format, event.hour),
                        color = OxfordBlue,
                        fontFamily = NunitoFont,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(modifier = Modifier.size(70.dp), onClick = {
                    val screen = Screen.AttendantScreen(event.id.toString())
                    navController.navigate(screen.createRoute())
                }) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = BlueGreen,
                                contentColor = Snow,
                            ) {
                                Text(
                                    text =
                                    event.attendantCount.toString(),
                                )
                            }
                        },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.CoPresent,
                            contentDescription = "bottomNavigationItem.title"
                        )
                    }
                }
            }
        }
    }
}

fun formatDate(day: Int, month: Int, year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    val format = SimpleDateFormat("dd 'de' MMMM", Locale("es", "ES"))
    return format.format(calendar.time)
}

fun formatDateOnlyDay(day: Int, month: Int, year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    val format = SimpleDateFormat("EEEE", Locale("es", "ES"))
    return format.format(calendar.time)
}
