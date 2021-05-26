package com.vvek.consentmoduleapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vvek.consentmoduleapp.R
import com.vvek.consentmoduleapp.data.model.Contact
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(
    private val contacts: ArrayList<Contact>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.textViewUserName.text = contact.name
            itemView.textViewUserEmail.text = contact.email
            Glide.with(itemView.imageViewAvatar.context)
                .load(contact.avatar)
                .placeholder(R.mipmap.ic_launcher)
                .into(itemView.imageViewAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(contacts[position])

    fun addData(list: List<Contact>) {
        contacts.addAll(list)
    }

}