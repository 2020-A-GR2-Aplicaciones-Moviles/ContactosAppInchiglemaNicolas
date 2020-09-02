package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {
    private val TAG = "RegistroActivity"
    val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroR)

        edtxtNombreR.validate("El nombre no puede quedar en blanco") { s -> s.length > 0}
        edtxtEmailR.validate("Correo electronico invalido") { s -> s.isValidEmail() }
        edtxtPasswordR.validate("La contraseña debe tener mas de 8 caracteres") { s -> s.length >= 8}
        edtxtPasswordr2.validate("La contraseña debe tener mas de 8 caracteres") { s -> s.length >= 8}
        edtxtPasswordR.validate("Las contraseñas no son iguales") { s -> s.isEqualPass(edtxtPasswordr2.text.toString())}
        edtxtPasswordr2.validate("Las contraseñas no son iguales") { s -> s.isEqualPass(edtxtPasswordR.text.toString())}
        edtxtTelefonoR.validate("El numero debe tener 8 caracteres o mas") { s -> s.length >= 8}

        btnRegistro.setOnClickListener(
            View.OnClickListener{
                mAuth.createUserWithEmailAndPassword(edtxtEmailR.text.toString(), edtxtPasswordR.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                        val intent = Intent(this, principal::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                }
            })
    }

    fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
    }

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isEqualPass(passVal:String): Boolean
            = this.isNotEmpty() && this.equals(passVal)

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    fun updateUI(user: FirebaseUser?) {
        if(user != null){
            //start MAIN activity
            Toast.makeText(this, "Inicio de sesion exitoso.",Toast.LENGTH_LONG).show()
        }
    }

}