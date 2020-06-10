package com.example.kokwet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        btn_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_signin.setOnClickListener {
            loginUser()
        }
    }

    fun validateUsername(): Boolean {
        val value: String = login_username.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.login_username)

        return if (value.isEmpty()) {
            textLayout.error = "Username cannot be empty"
            false

        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun validatePassword(): Boolean {
        val value: String = login_password.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.login_password)

        return if (value.isEmpty()) {
            textLayout.error = "Password cannot be empty"
            false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun loginUser() {
        if (!validateUsername() || !validatePassword()) {
            return
        } else {
            isUser()
        }
    }

    private fun isUser() {
//        get user entered data from fields
        val userName: String = login_username.editText?.text?.trim().toString()
        val userPassword: String = login_password.editText?.text?.trim().toString()
//        find layout fields
        val passwordTextLayout = findViewById<TextInputLayout>(R.id.login_password)
        val usernameTextLayout = findViewById<TextInputLayout>(R.id.login_username)

//        check data against firebase database
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserQuery: Query = databaseReference.ref.orderByChild("user_username").equalTo(userName)
        checkUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    usernameTextLayout.error = null
                    usernameTextLayout.isErrorEnabled = false

//                    val passwordFromDb = dataSnapshot.child(userName).child("user_password").getValue(String::class.java)
                    val passwordFromDb = dataSnapshot.child("user_password").getValue(String::class.java).toString()

                    if (passwordFromDb.equals(userPassword)) {

                        passwordTextLayout.error = null
                        passwordTextLayout.isErrorEnabled = false

                        val nameFromDb = dataSnapshot.child(userName).child("user_name").getValue(String.javaClass)
                        val emailFromDb = dataSnapshot.child(userName).child("user_email").getValue(String.javaClass)
                        val phoneFromDb = dataSnapshot.child(userName).child("user_phone").getValue(String.javaClass)

                        val intent = Intent(this@LoginActivity, UserProfile::class.java)
                        intent.putExtra("name", nameFromDb)
                        intent.putExtra("email", emailFromDb)
                        intent.putExtra("phone", phoneFromDb)
                        intent.putExtra("password", passwordFromDb)

                        startActivity(intent)
                    } else {
                        passwordTextLayout.error = "Wrong Password"
                        passwordTextLayout.requestFocus()
                    }
                } else {
                    usernameTextLayout.error = "User does not exist"
                    usernameTextLayout.requestFocus()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }
}

private fun Any.putExtra(s: String, emailFromDb: String.Companion?) {

}
