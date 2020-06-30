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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val swtRecuerdame = findViewById<Switch>(R.id.swtRecuerdame)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        

        edtxtEmail.validate("Correo electronico invalido") { s -> s.isValidEmail() }
        edtxtPassword.validate("La contraseña debe tener mas de 8 caracteres") { s -> s.length >= 8}

        btnLogin.setOnClickListener(View.OnClickListener {

            if (swtRecuerdame.isChecked) {
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
}