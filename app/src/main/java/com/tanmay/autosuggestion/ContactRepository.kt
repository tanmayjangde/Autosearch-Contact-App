package com.tanmay.autosuggestion

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts:LiveData<List<Contact>> = contactDao.getAllContacts()
    var contactsByQuery:ArrayList<Contact> = ArrayList<Contact>()
    suspend fun insert(contact: Contact){
        contactDao.insert(contact)
    }

    suspend fun delete(contact: Contact){
        contactDao.delete(contact)
    }

    suspend fun getInitialContacts():List<Contact> {
            return contactDao.getInitialContacts()
    }
}

