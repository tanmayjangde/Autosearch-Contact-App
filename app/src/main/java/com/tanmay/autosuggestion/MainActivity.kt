package com.tanmay.autosuggestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),IContactsRVAdapter{

    lateinit var viewModel: ContactViewModel
    var trie = Trie.getInstance()
    var searchedContacts:ArrayList<Contact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        contactsRv.layoutManager=LinearLayoutManager(this)
        val adapter=ContactRvAdapter(this,this)
        contactsRv.adapter=adapter

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(ContactViewModel::class.java)

        addAllContactsToTrie(viewModel.getInitialContacts())

        toolbarEt.onFocusChangeListener = View.OnFocusChangeListener { view,hasFocus->
            if(hasFocus){
                toolbarEt.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        if(p0.toString().isNotEmpty()) {
                            searchedContacts = trie.getAutosuggestion(p0?.toString())
                            adapter.updateList(searchedContacts)
                        }else{
                            viewModel.allContacts.observe(this@MainActivity, Observer {
                                it?.let {
                                    adapter.updateList(it)
                                }
                            })
                        }
                    }
                })
            }
            else if(toolbarEt.text.isNotEmpty()){
                adapter.updateList(trie.getAutosuggestion(toolbarEt.text.toString()))
            }
            else{
                viewModel.allContacts.observe(this@MainActivity, Observer {
                    it?.let {
                        adapter.updateList(it)
                    }
                })
            }
        }

        viewModel.allContacts.observe(this, Observer {
            it?.let {
                adapter.updateList(it)
            }
        })

        addContactBtn?.setOnClickListener{
            val intent = Intent(this, ContactInfo::class.java)
            startActivity(intent)
        }
    }

    private fun addAllContactsToTrie(contactList:List<Contact>) {

        val contactsIterator = contactList.iterator()
        while (contactsIterator.hasNext()) {
           trie.insert(contactsIterator.next().contactName,contactsIterator.next().contactNumber)
        }
    }

    override fun onItemClicked(contact: Contact) {
        viewModel.delete(contact)
        trie.remove(contact.contactName)
    }
}