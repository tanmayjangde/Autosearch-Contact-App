package com.tanmay.autosuggestion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contact_Table")
class Contact(val contactName: String, val contactNumber:String) {
    @PrimaryKey(autoGenerate = true) var id=0
}