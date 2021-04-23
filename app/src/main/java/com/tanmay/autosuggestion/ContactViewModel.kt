package com.tanmay.autosuggestion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ContactRepository
    val allContacts : LiveData<List<Contact>>

    init{
        val dao = ContactDatabase.getDatabase(application).getContactDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun delete(contact:Contact)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
    }

    fun insert(contact:Contact)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }

    fun getInitialContacts():List<Contact>{

        var contactList:List<Contact> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            contactList=repository.getInitialContacts()
        }
        return contactList
    }
}