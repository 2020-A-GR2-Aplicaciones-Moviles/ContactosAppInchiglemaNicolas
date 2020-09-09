package com.inchiglemanicolas.contactosappinchiglemanicolas
import com.google.firebase.auth.FirebaseAuth

const val USER_KEY = "USER_KEY"
const val REMEMBER_KEY = "REMEMBER_KEY"
const val PASSWORD_KEY = "PASWORD_KEY"
const val USER_DATA_FILE = "USER_DATA_FILE"
const val ENC_USER_DATA_FILE = "ENC_USER_DATA_FILE"
const val PERMISSION_REQUEST_CODE = 100

lateinit var mAuth: FirebaseAuth
