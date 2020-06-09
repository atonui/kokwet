package com.example.kokwet

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_onboarding.*


class Onboarding : AppCompatActivity() {
    private val slideAdapter = SlideAdapter(this) //object of the SliderAdapter class
    var currentPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        btn_start.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
//        hook activity to adapter

        slider.adapter = slideAdapter
        addDots(0)
        slider.addOnPageChangeListener(changeListener)
    }

    public fun skip(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

    }

    public fun next(view: View) {
        slider.setCurrentItem(currentPosition + 1)


    }

    private fun addDots(position: Int) {
        val dotsViewArray = arrayOfNulls<TextView>(size = slideAdapter.headings.size)
        dots.removeAllViews()
        for (i in 0 until slideAdapter.headings.size) {
            dotsViewArray[i] = TextView(this) // create a TextView
            dotsViewArray[i]?.text = Html.fromHtml("&#8226;") //set the text to a dot
            dotsViewArray[i]?.textSize = 35F

            dots.addView(dotsViewArray[i])
        }
        if (dotsViewArray.isNotEmpty()) {
//            set the color for the current (active TextView/ dot)
            dotsViewArray[position]?.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }

    //    getting the current(active slide)
    var changeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
            currentPosition = position
            when (position) {
                0 -> btn_start.visibility = View.INVISIBLE
                1 -> btn_start.visibility = View.INVISIBLE
                else -> {
                    val bottomAnimation: Animation = AnimationUtils.loadAnimation(this@Onboarding, R.anim.bottom_animation)
                    btn_start.startAnimation(bottomAnimation)
                    btn_start.visibility = View.VISIBLE
                }
            }

        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}
