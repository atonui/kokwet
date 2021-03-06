package com.example.kokwet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        //    animation variables
        val topAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

//        bind animations to the frontend components to be animated
        img_logo.startAnimation(topAnimation)
        tv_app_title.startAnimation(bottomAnimation)

        val onBoardingScreen: SharedPreferences = getSharedPreferences("onBoardingScreen", Context.MODE_PRIVATE)


        Handler().postDelayed({

            var isFirstTime: Boolean = onBoardingScreen.getBoolean("firstTime", true)

            if (isFirstTime) {
                val editor: SharedPreferences.Editor = onBoardingScreen.edit()
                editor.putBoolean("firstTime", false)
                editor.commit()
                startActivity(Intent(this, Onboarding::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
