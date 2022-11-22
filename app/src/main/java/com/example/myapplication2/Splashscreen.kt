package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.myapplication.MainActivity

@Suppress("Deprecation")
class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val init = Intent(this, Login::class.java)
            startActivity(init)
            finish()
        }, 1000)

    }
}