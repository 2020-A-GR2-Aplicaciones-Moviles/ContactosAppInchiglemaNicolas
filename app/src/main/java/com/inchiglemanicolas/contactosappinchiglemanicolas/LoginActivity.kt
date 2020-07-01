package com.inchiglemanicolas.contactosappinchiglemanicolas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val swtRecuerdame = findViewById<Switch>(R.id.swtRecuerdame)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        LeerDatosDeArchivoPreferenciasEncriptado()

        edtxtEmail.validate("Correo electronico invalido") { s -> s.isValidEmail() }
        edtxtPassword.validate("La contraseña debe tener mas de 8 caracteres") { s -> s.length >= 8}

        btnLogin.setOnClickListener(View.OnClickListener {
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

    fun EscribirDatosEnArchivoPreferencias() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if (swtRecuerdame.isActivated) {
            val editor = sharedPref.edit()
            editor.putString(USER_KEY, edtxtEmail.text.toString())
            editor.putString(PASSWORD_KEY, edtxtPassword.text.toString())
            editor.commit()
        } else {
            val editor = sharedPref.edit()
            editor.putString(USER_KEY, "")
            editor.putString(PASSWORD_KEY, "")
            editor.commit()
        }
    }
    fun LeerDatosDeArchivoPreferencias() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        edtxtEmail.setText(sharedPref.getString(USER_KEY, ""))
        edtxtPassword.setText(sharedPref.getString(PASSWORD_KEY, ""))
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
                .putString(USER_KEY, edtxtEmail.text.toString())
                .putString(PASSWORD_KEY, edtxtPassword.text.toString())
                .apply()
        } else {
            sharedPreferences
                .edit()
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
        edtxtEmail.setText(sharedPreferences.getString(USER_KEY, ""))
        edtxtPassword.setText(sharedPreferences.getString(PASSWORD_KEY, ""))
    }
}