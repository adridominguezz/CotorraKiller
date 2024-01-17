package com.example.cotorraskiller

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_details)

        val player = intent.getSerializableExtra("PLAYER") as Jugador

        // Mostrar los detalles del jugador en los TextView e ImageView correspondientes
        val nombreTextView: TextView = findViewById(R.id.nombreTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val puntuacionTextView: TextView = findViewById(R.id.puntuacionTextView)
        val iconImageView: ImageView = findViewById(R.id.iconImageView)
        val imgImageView: ImageView = findViewById(R.id.imgImageView)

        nombreTextView.text = player.name.toUpperCase()
        emailTextView.text = player.email
        puntuacionTextView.text = player.cotorras.toString()

        Glide.with(this)
            .load(player.iconUrl)
            .into(iconImageView)

        Glide.with(this)
            .load(player.imgUrl)
            .into(imgImageView)
    }
}
