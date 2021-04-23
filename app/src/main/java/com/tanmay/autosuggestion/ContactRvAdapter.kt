package com.tanmay.autosuggestion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactRvAdapter(val context: Context, val listener : IContactsRVAdapter): RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>(){

    var allContacts = ArrayList<Contact>()
    val trie : Trie = Trie.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val viewHolder = ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact,parent,false))

        viewHolder.deleteBtn.setOnClickListener {
            listener.onItemClicked(allContacts[viewHolder.adapterPosition])
            trie.remove(allContacts[viewHolder.adapterPosition].contactName)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val currentContact = allContacts[position]
        holder.nameTv.text = currentContact.contactName
        holder.numberTv.text = currentContact.contactNumber
    }

    override fun getItemCount(): Int {
        return allContacts.size
    }

    fun updateList(newContactList: List<Contact>){

        allContacts.clear()
        allContacts.addAll(newContactList)

        notifyDataSetChanged()
    }

    inner class ContactViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val nameTv = itemView.findViewById<TextView>(R.id.name)
        val numberTv = itemView.findViewById<TextView>(R.id.number)
        val deleteBtn = itemView.findViewById<ImageView>(R.id.deleteBtn)
    }
}

interface IContactsRVAdapter{
    fun onItemClicked(contact: Contact)
}