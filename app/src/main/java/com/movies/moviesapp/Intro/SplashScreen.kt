package com.movies.moviesapp.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.movies.moviesapp.R
import android.content.Intent
import android.os.Handler
import com.movies.moviesapp.ListScreen.MoviesMain

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MoviesMain::class.java))
            finish()
        }, 3000)
    }
}