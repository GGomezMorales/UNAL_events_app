package com.unalminas.eventsapp.domain.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unalminas.eventsapp.domain.Assistant
import com.unalminas.eventsapp.domain.framework.db.entity.AssistantEntity
import com.unalminas.eventsapp.domain.framework.db.entity.EventEntity

@Dao
interface AssistantDao {

    @Query("SELECT * FROM assistants")
    fun getAll(): List<AssistantEntity>

    @Query("SELECT * FROM assistants WHERE id = :id")
    fun getAssistantById(id: Int): AssistantEntity

    @Update
    suspend fun update(assistant: AssistantEntity)

    @Query("DELETE FROM assistants WHERE id= :assistantId")
    suspend fun deleteAssistantById(assistantId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAssistant(vararg users: AssistantEntity)
}