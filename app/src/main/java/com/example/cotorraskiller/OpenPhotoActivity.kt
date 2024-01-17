package com.example.cotorraskiller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class OpenPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_photo)

        val imageUrl = intent.getStringExtra("IMAGE_URL")

        val imageView: ImageView = findViewById(R.id.imageView)
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }
}