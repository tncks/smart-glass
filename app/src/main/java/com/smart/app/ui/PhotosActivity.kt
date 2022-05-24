package com.smart.app.ui

import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R


class PhotosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        findViewById<ImageView>(R.id.iv_open_camera).visibility = View.GONE

        val gridView = findViewById<GridView>(R.id.gv_folder)
        val nPos = intent.getIntExtra("value", 0)
        val mmIndex = intent.getIntExtra("mIndex", 0)
        val adapter = GridViewAdapter(this, GalleryActivity.alImages2, nPos, mmIndex)
        gridView?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        findViewById<ImageView>(R.id.iv_open_camera).visibility = View.VISIBLE
    }
}