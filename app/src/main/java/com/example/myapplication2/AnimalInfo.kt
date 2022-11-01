package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication2.R
import kotlinx.android.synthetic.main.activity_animal_info.*

class AnimalInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_info)
        val bundle:Bundle = intent.extras!!
        tvNameInfo.text = bundle.getString("name")
        tvDesInfo.text = bundle.getString("des")
        imageViewInfo.setImageResource(bundle.getInt("image"))
    }
}