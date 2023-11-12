package com.unalminas.eventsapp.domain.framework.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unalminas.eventsapp.domain.framework.db.dao.EventDao
import com.unalminas.eventsapp.domain.framework.db.entity.EventEntity

@Database(entities = [EventEntity::class], version = 5)
abstract class EventDatabase : RoomDatabase() {
    abstract fun EventDao(): EventDao
}