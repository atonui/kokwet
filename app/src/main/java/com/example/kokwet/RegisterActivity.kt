package com.example.kokwet

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
//    image capture requirements
    val PERMISSION_CODE = 1000
    val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        img_user.setOnClickListener {
            selectImage()
        }
//        collect data from fields
//        validate the data
//        check if user is already registered
//        push data to db


    }

    private fun selectImage() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                            if permission was denied
                            val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
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
            img_user.setImageURI(image_uri)
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        this.image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
//        camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }
}
