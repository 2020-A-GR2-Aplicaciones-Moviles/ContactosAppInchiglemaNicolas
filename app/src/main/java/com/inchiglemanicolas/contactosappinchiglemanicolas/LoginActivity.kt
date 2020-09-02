package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    val dataBaseReference:DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val swtRecuerdame = findViewById<Switch>(R.id.swtRecuerdame)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        //LeerDatosDeArchivoPreferenciasEncriptado()

        edtxtEmail.validate("Correo electronico invalido") { s -> s.isValidEmail() }
        edtxtPassword.validate("La contraseña debe tener mas de 8 caracteres") { s -> s.length >= 8}

        btnLogin.setOnClickListener(View.OnClickListener {
            mAuth.signInWithEmailAndPassword(edtxtEmail.text.toString(), edtxtPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                        val intent = Intent(this, principal::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
            EscribirDatosEnArchivoPreferenciasEncriptado()
        })
    }

    fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
    }

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

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

    fun EscribirDatosEnArchivoPreferenciasEncriptado() {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            ENC_USER_DATA_FILE,//filename
            masterKeyAlias,
            this,//context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        if (swtRecuerdame.isChecked) {
            sharedPreferences.edit()
                .putString(REMEMBER_KEY,isSwitcheR())
                .putString(USER_KEY, edtxtEmail.text.toString())
                .putString(PASSWORD_KEY, edtxtPassword.text.toString())
                .apply()
        } else {
            sharedPreferences
                .edit()
                .putString(REMEMBER_KEY,"N")
                .putString(USER_KEY, "")
                .putString(PASSWORD_KEY, "")
                .apply()
        }
    }
    fun LeerDatosDeArchivoPreferenciasEncriptado() {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            ENC_USER_DATA_FILE,//filename
            masterKeyAlias,
            this,//context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        if (sharedPreferences.getString(REMEMBER_KEY,"").equals("Y")){
            edtxtEmail.setText(sharedPreferences.getString(USER_KEY, ""))
            edtxtPassword.setText(sharedPreferences.getString(PASSWORD_KEY, ""))
        }
    }

    fun isSwitcheR()
    : String {
        if (swtRecuerdame.isChecked){
            return "Y"
        }
        return "N"
    }
}