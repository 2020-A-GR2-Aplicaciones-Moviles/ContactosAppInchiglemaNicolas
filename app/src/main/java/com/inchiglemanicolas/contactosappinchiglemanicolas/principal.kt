package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import database.ContactosSQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_principal.*
import org.json.JSONObject

class principal : AppCompatActivity() {
    var contactos = arrayListOf<ContactoModelClass>()
    var selectedContactPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        ConsultarContactos()
        val username = intent.getStringExtra(USER_KEY)

        listViewContacts.setOnItemClickListener { parent, view, position, id ->
            selectedContactPosition = position
            editTextUserId.setText(contactos[selectedContactPosition].userId.toString())
            editTextFirstName.setText(contactos[selectedContactPosition].firstName)
            editTextLastName.setText(contactos[selectedContactPosition].lastName)
            editTextPhoneNumber.setText(contactos[selectedContactPosition].phoneNumber)
            editTextEmailAddress.setText(contactos[selectedContactPosition].emailAddress)
        }

        buttonSave.setOnClickListener {
            var contact = ContactoModelClass(0,"","","","")
            contact.userId = editTextUserId.text.toString().toInt()
            contact.firstName = editTextFirstName.text.toString()
            contact.lastName = editTextLastName.text.toString()
            contact.phoneNumber = editTextPhoneNumber.text.toString()
            contact.emailAddress = editTextEmailAddress.text.toString()
            contactos.add(contact)
            /*val contactoAdaptador = ContactoAdapter(this, contactos)
            listViewContacts.adapter = contactoAdaptador*/

            val respuesta = ContactosSQLiteOpenHelper(this).addContacto(contact)
            if(respuesta > -1){
                Toast.makeText(this,"Contacto añadido", Toast.LENGTH_LONG).show()
                limpiarCamposEditables()
                reloadData()
            }
            else{
                Toast.makeText(this,"Error al grabar Contacto", Toast.LENGTH_LONG).show()
            }
        }

        buttonView.setOnClickListener {
            reloadData()
        }

        buttonUpdate.setOnClickListener {
//            contactos[selectedContactPosition].userId = editTextUserId.text.toString().toInt()
//            contactos[selectedContactPosition].firstName = editTextFirstName.text.toString()
//            contactos[selectedContactPosition].lastName = editTextLastName.text.toString()
//            contactos[selectedContactPosition].phoneNumber = editTextPhoneNumber.text.toString()
//            contactos[selectedContactPosition].emailAddress = editTextEmailAddress.text.toString()
//            listViewContacts.adapter = ContactoAdapter(this, contactos)
//            Toast.makeText(this,"Contacto actualizado",Toast.LENGTH_LONG).show()
//            limpiarCamposEditables()

            var contact  = contactos[selectedContactPosition]
            contact.userId = editTextUserId.text.toString().toInt()
            contact.firstName = editTextFirstName.text.toString()
            contact.lastName = editTextLastName.text.toString()
            contact.phoneNumber = editTextPhoneNumber.text.toString()
            contact.emailAddress = editTextEmailAddress.text.toString()

            val respuesta = ContactosSQLiteOpenHelper(this).updateContacto(contact)
            if(respuesta > -1){
                Toast.makeText(this,"Contacto actualizado", Toast.LENGTH_LONG).show()
                limpiarCamposEditables()
                reloadData()
            }
            else{
                Toast.makeText(this,"Error al actualizar Contacto", Toast.LENGTH_LONG).show()
            }

        }

        buttonDelete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Confirmación de Eliminación")
            dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            dialogBuilder.setMessage("¿Esta seguro que desea eliminar el contacto ${contactos[selectedContactPosition].firstName} ${contactos[selectedContactPosition].lastName}?")
            dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
//                contactos.removeAt(selectedContactPosition)
//                val contactoAdaptador = ContactoAdapter(this, contactos)
//                listViewContacts.adapter = contactoAdaptador

                var contact = contactos[selectedContactPosition]

                val respuesta = ContactosSQLiteOpenHelper(this).deleteContacto(contact)
                if(respuesta > -1){
                    Toast.makeText(this,"Contacto eliminado",Toast.LENGTH_LONG).show()
                    limpiarCamposEditables()
                    reloadData()
                }
                else{
                    Toast.makeText(this,"Error al eliminar Contacto", Toast.LENGTH_LONG).show()
                }

                limpiarCamposEditables()
            })
            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                //pass
            })
            dialogBuilder.create().show()
        }


    }

    fun ConsultarContactos() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.androidhive.info/contacts/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response -> val jsonObj = JSONObject(response.toString())
                val contacts = jsonObj.getJSONArray("contacts")
                for (i in 0 until contacts.length()) {
                    val c = contacts.getJSONObject(i)
                    val id = c.getString("id").substring(1).toInt()
                    val name = c.getString("name")
                    val nombre = name.substringBefore(" ")
                    val apellido = name.substringAfter(" ")
                    val email = c.getString("email")
                    //val address = c.getString("address")
                    //val gender = c.getString("gender")
                    val phone = c.getJSONObject("phone")
                    val mobile = phone.getString("mobile")
                    val home = phone.getString("home")
                    //val office = phone.getString("office")
                    val respuesta = ContactosSQLiteOpenHelper(this).addContacto(
                        ContactoModelClass( id, nombre, apellido, mobile, email )
                    )
                }
            },
            Response.ErrorListener {
                    error -> Toast.makeText(this, "Error to read Webservice: ${error.message}",
                Toast.LENGTH_SHORT).show()
                Log.d("MYTAG",error.message)
            } )
        queue.add(jsonObjectRequest)
    }

    fun onClickEnviar(view: View){
        makeCall(editTextPhoneNumber.text.toString())
    }

    fun makeCall(numero:String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + numero)
            startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + numero)
            val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            if (result == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            } else {
                requestForCallPermission()
            }
        }
    }

    private fun requestForCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission for call was granted!", Toast.LENGTH_SHORT).show();
                    makeCall(editTextPhoneNumber.text.toString())
                } else {
                    Toast.makeText(this, "Permission for call was denied!", Toast.LENGTH_SHORT).show();
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


    override fun onStart() {
        super.onStart()
        reloadData()
    }

    private fun reloadData(){
        contactos = ContactosSQLiteOpenHelper(this).readContacto()
        listViewContacts.adapter = ContactoAdapter(this, contactos)
        limpiarCamposEditables()

    }
    private fun limpiarCamposEditables() {
        editTextUserId.setText("")
        editTextFirstName.setText("")
        editTextLastName.setText("")
        editTextPhoneNumber.setText("")
        editTextEmailAddress.setText("")
    }
}