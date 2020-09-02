package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ContactoAdapter (private val context: Activity, private val contactos: ArrayList<ContactoModelClass>)   : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.contact_list, null, true)

        val textViewUserId = rowView.findViewById<TextView>(R.id.textViewUserId)
        val textViewFirstName = rowView.findViewById<TextView>(R.id.textViewFirstName)
        val textViewLastName = rowView.findViewById<TextView>(R.id.textViewLastName)
        val textViewPhoneNumber = rowView.findViewById<TextView>(R.id.textViewPhoneNumber)
        val textViewEmailAddress = rowView.findViewById<TextView>(R.id.textViewEmailAddress)

        textViewUserId.text = "${contactos[position].userId}"
        textViewFirstName.text = "Nombre: ${contactos[position].firstName}"
        textViewLastName.text = "Apellido: ${contactos[position].lastName}"
        textViewPhoneNumber.text = "Numero: ${contactos[position].phoneNumber}"
        textViewEmailAddress.text = "Email: ${contactos[position].emailAddress}"

        return rowView
    }

    override fun getItem(position: Int): Any? {
        return contactos.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return contactos.size
    }
}