package com.tanmay.autosuggestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_contact_info.*

class ContactInfo : AppCompatActivity() {

    lateinit var viewModel: ContactViewModel
    var trie = Trie.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(ContactViewModel::class.java)

        addContactInfo.setOnClickListener{
            val contactNameText = contactNameEt.text.toString()
            val contactNumberText = contactNumberEt.text.toString()
            if(contactNameText.isNotEmpty() && contactNumberText.isNotEmpty()){
                viewModel.insert(Contact(contactNameText,contactNumberText))
                trie.insert(contactNameText,contactNumberText)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}