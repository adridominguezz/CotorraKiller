package com.example.cotorraskiller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity (Intent(this,MainActivity::class.java))

//        setContentView(R.layout.activity_splash)
//        Handler().postDelayed({
//            val intent = Intent(this@Splash,  MainActivity::class.java)
//        }, 500)
//
    }
}