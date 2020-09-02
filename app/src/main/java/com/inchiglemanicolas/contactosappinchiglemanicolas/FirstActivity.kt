package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.FirebaseApp.getInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth(getInstance())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        btnIngresar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onStart() {
        super.onStart()
        var currentUser: FirebaseUser? = mAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Toast.makeText(this, "Inicio de sesion exitoso.",Toast.LENGTH_LONG).show()
            val intent = Intent(this, principal::class.java)
            startActivity(intent)
        }
    }
}