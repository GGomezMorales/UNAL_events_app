package com.unalminas.eventsapp.domain

data class Assistant(
    val id: Int? = System.currentTimeMillis().hashCode(),
    val name: String = "",
    val identification: String = "",
    val email: String = "",
    val eventId: Int = 0
)