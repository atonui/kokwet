package com.example.kokwet

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.Manifest.permission
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    //    image capture requirements
    val PERMISSION_CODE = 1000
    val IMAGE_CAPTURE_CODE = 1001
    var imageUri: Uri? = null
//    still to work on image display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            registerUser()
        }
    }

    fun validateName(): Boolean {
        val value: String = et_name.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.et_name)

        return if (value.isEmpty()) {
            textLayout.error = "Name cannot be empty"
            false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun validatePhone(): Boolean {
        val value: String = et_phone.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.et_phone)

        return if (value.isEmpty()) {
            textLayout.error = "Phone number cannot be empty"
            false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun validateEmail(): Boolean {
        val value: String = et_email.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.et_email)
        val emailPattern: String = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

        return if (value.isEmpty()) {
            textLayout.error = "Email cannot be empty"
            false
        } else if (!value.matches(emailPattern.toRegex())) {
            textLayout.error = "Invalid email address"
            false

        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun validatePassword(): Boolean {
        val value: String = et_password.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.et_password)
        val passwordPattern: String = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$"

        return if (value.isEmpty()) {
            textLayout.error = "Password cannot be empty"
            false
        } else if (!value.matches(passwordPattern.toRegex())) {
            textLayout.error = "Password is too weak"
            false

        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    fun validateUsername(): Boolean {
        val value: String = et_username.editText?.text?.trim().toString()
        val textLayout = findViewById<TextInputLayout>(R.id.et_username)

        return if (value.isEmpty()) {
            textLayout.error = "Username cannot be empty"
            false

        } else if (value.length>=15) {
            textLayout.error = "Username too long"
            false

        }else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            true
        }

    }

    private fun registerUser() {

        val name: String = et_name.editText?.text?.trim().toString()
        val username: String = et_username.editText?.text?.trim().toString()
        val phone: String = et_phone.editText?.text?.trim().toString()
        val email: String = et_email.editText?.text?.trim().toString()
        val password: String = et_password.editText?.text?.trim().toString()

        val primaryKey = System.currentTimeMillis() //primary key in firebase database

        val progress = showProgress("Processing", "We'll be done in just a moment")

//        validate the data
        if (!validateName() || !validatePhone() || !validateEmail() || !validatePassword() ||!validateUsername()) {
            return
        } else {
//        check if user is already registered
//        push data to db
//            1. save the data
            val myDatabase = FirebaseDatabase.getInstance()
            val myRef = myDatabase.reference.child("users/$primaryKey")
            val userObject = UserModel(name, username, phone, email, password)
            progress.show()
            myRef.setValue(userObject).addOnCompleteListener { task ->
                progress.dismiss()
                if (task.isSuccessful) {
                    showMessage(
                        "Registration Complete",
                        "$name, you have been successfully registered!"
                    )
                    startActivity(Intent(this, UserProfile::class.java))
                    finish()
                } else {
                    showMessage(
                        "Registration failed",
                        "Sorry we could not complete your registration."
                    )
                }
            }
        }
    }

    //alert box to show message to user
    private fun showMessage(title: String, message: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        alertDialog.create().show()
    }

    //    progress dialog
    private fun showProgress(title: String, message: String): ProgressDialog {
        val progress = ProgressDialog(this)
        progress.setTitle(title)
        progress.setMessage(message)
        return progress
    }

    fun clearTextFields() {
        et_name.editText?.text?.clear()
        et_email.editText?.text?.clear()
        et_phone.editText?.text?.clear()
        et_password.editText?.text?.clear()
    }

    //function to set user image
    private fun selectImage() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                                permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_DENIED
                        ) {
//                            if permission was denied
                            val permission = arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
//                            show popup to request for permission
                            requestPermissions(permission, PERMISSION_CODE)
                        } else {
//                            permission was already granted
                            openCamera()
                        }
                    } else {
//                        system os is mashmallow
                        openCamera()
                    }
                }
                options[item] == "Choose from Gallery" -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        allow user to upload photo
        super.onActivityResult(requestCode, resultCode, data)
//        called when an image is captured
        if (resultCode == Activity.RESULT_OK) {
//            set captured image to the image view
//            img_user.setImageURI(imageUri)
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        this.imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
//        camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }
}
