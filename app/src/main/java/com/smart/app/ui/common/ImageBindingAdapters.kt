package com.smart.app.ui.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smart.app.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {

        Glide.with(view)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_traran)
                    .error(R.drawable.ic_traran)
            )
            .load(imageUrl)
            .into(view)
    }

}

@BindingAdapter("circleImageUrl")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_traran)
                    .error(R.drawable.ic_traran)
            )
            .load(imageUrl)
            .circleCrop()
            .into(view)
    }

}