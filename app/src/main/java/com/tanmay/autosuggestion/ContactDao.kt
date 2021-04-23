package com.tanmay.autosuggestion

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contact:Contact)

    @Delete
    suspend fun delete(contact:Contact)

    @Query("Select * from Contact_Table order by contactName ASC")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("Select * from Contact_Table order by contactName ASC")
    suspend fun getInitialContacts():List<Contact>
}