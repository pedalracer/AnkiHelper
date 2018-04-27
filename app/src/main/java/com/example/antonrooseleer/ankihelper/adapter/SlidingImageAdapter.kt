package com.example.antonrooseleer.ankihelper.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.antonrooseleer.ankihelper.R


class SlidingImageAdapter(val context: Context, val imageList: ArrayList<Int>) : PagerAdapter() {

    val layoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = layoutInflater.inflate(R.layout.sliding_images_layout, view, false)!!

        val imageView = imageLayout!!
                .findViewById(R.id.image) as ImageView

        imageView.setImageResource(imageList.get(position))

        view.addView(imageLayout, 0)

        return imageLayout
    }
}