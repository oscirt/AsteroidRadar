package com.example.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.adapters.AsteroidsListAdapter
import com.example.asteroidradar.models.Asteroid
import com.example.asteroidradar.models.PictureOfTheDay
import com.example.asteroidradar.viewmodel.Status
import com.squareup.picasso.Picasso

@BindingAdapter("isHazardous")
fun bindHazardImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("listData")
fun bindRecyclerData(recycler: RecyclerView, asteroids: List<Asteroid>) {
    val adapter = recycler.adapter as AsteroidsListAdapter
    adapter.submitList(asteroids)
}

@BindingAdapter("auText")
fun bindAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmText")
fun bindKilometerUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("kmsText")
fun bindKilometerPerSecUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("imageSrc")
fun bindImage(imageView: ImageView, pictureOfTheDay: PictureOfTheDay) {
    if (pictureOfTheDay.media_type == "image") {
        Picasso.get()
            .load(pictureOfTheDay.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(imageView)
    } else if (pictureOfTheDay.media_type == "ERROR") {
        Log.d("TAG", "bindImage: error")
    }
}

@BindingAdapter("loadListStatus")
fun bindLoadingStatus(progressBar: ProgressBar, status: Status) {
    when (status) {
        Status.LOADING -> progressBar.visibility = View.VISIBLE
        Status.DONE, Status.ERROR -> progressBar.visibility = View.GONE
    }
}

@BindingAdapter("blockVisibility")
fun bindReconnectVisibility(linearLayout: LinearLayout, status: Status) {
    if (status == Status.ERROR) linearLayout.visibility = View.VISIBLE
    else linearLayout.visibility = View.GONE
}