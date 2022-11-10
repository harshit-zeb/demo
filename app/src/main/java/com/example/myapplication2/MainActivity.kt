package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import  kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.animal_ticket.view.*
import android.widget.BaseAdapter
import androidx.appcompat.app.ActionBar
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication2.Animal
import com.example.myapplication2.R

class MainActivity : AppCompatActivity() {

    var listOfAnimal = ArrayList<Animal>()
    var adapter : AnimalsAdaptor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#338FFF")))
        listOfAnimal.add(Animal("Panda","Panda lives in a zoo with other animals and Panda",R.drawable.panda, false))
        listOfAnimal.add(Animal("Tiger","Tiger lives in a zoo with other animals and Panda",R.drawable.white_tiger, true))
        listOfAnimal.add(Animal("Zebra","Zebra lives in a zoo with other animals and Panda",R.drawable.zebra, false))
        adapter = AnimalsAdaptor(this,listOfAnimal)
        tvListAnimal.adapter=adapter
    }


    class AnimalsAdaptor: BaseAdapter{
        var  listOfAnimals = ArrayList<Animal>()
        var context:Context?= null

        constructor(context:Context,listOfAnimals: ArrayList<Animal>):super(){
            this.listOfAnimals= listOfAnimals
            this.context = context
        }

        override fun getCount(): Int {
            return listOfAnimals.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var animal = listOfAnimals[position]
            var inflaterService = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                var myView = inflaterService.inflate(R.layout.animal_ticket, null)
                myView.tvName.text = animal.name!!
                myView.tvDes.text = animal.des!!
                myView.imageView.setImageResource(animal.image!!)
                myView.imageView.setOnClickListener {
                    val intent = Intent(context, AnimalInfo::class.java)
                    intent.putExtra("name", animal.name)
                    intent.putExtra("des", animal.des)
                    intent.putExtra("image", animal.image)
                    context!!.startActivity(intent)
                }
                return myView

        }

    }

}