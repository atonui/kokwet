package com.example.kokwet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class SlideAdapter(var context: Context): PagerAdapter() {
//    first get the array of slider content i.e. images, title and descriptions

    val images = arrayListOf<Int>(
        R.drawable.service,
        R.drawable.location,
        R.drawable.agreement
    )

    val headings = arrayListOf<Int>(
        R.string.first_slide_title,
        R.string.second_slide_title,
        R.string.third_slide_title
    )

    val descriptions = arrayListOf<Int>(
        R.string.first_slide_description,
        R.string.second_slide_description,
        R.string.third_slide_description
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getCount(): Int {
        return headings.count()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val wrapper = `object` as View
        container.removeView(wrapper)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slide_layout, container, false)

//        find the views which will display the data given to them by the adapter
        val imageView: ImageView = view.findViewById(R.id.img_slider)
        val headingView: TextView = view.findViewById(R.id.tv_heading)
        val descriptionView: TextView = view.findViewById(R.id.tv_description)

//        push the data to the views
        imageView.setImageResource(images[position])
        headingView.setText(headings[position])
        descriptionView.setText(descriptions[position])

        container.addView(view)
        return view
    }

}