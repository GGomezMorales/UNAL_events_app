package com.unalminas.eventsapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unalminas.eventsapp.framework.db.entity.EventEntity
import com.unalminas.eventsapp.framework.db.entity.EventWithAttendanceCount

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAll(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: EventEntity)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Int)

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): EventEntity

    @Delete
    fun deleteEvent(user: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Query("SELECT * FROM events WHERE date = :date")
    fun getEventsByDate(date: String): List<EventEntity>

    @Query(
        """
        SELECT events.*, COUNT(attendants.id) as quantityAttendants
        FROM events
        LEFT JOIN Attendants ON events.id = Attendants.eventId
        WHERE date = :date
        GROUP BY events.id
    """
    )
    fun getEventsWithAttendantCount(date: String): List<EventWithAttendanceCount>
}
