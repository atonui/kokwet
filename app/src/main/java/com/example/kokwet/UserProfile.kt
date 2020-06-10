package com.example.kokwet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class UserProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_user_profile)
    }
}
