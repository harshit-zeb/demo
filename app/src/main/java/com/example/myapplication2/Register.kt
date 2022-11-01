package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.checkBox
import kotlinx.android.synthetic.main.activity_login.spinner
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Register : AppCompatActivity() {
    var countryCodeList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        countryCodeList.add("+91")

        signupButton.isEnabled = false
        getMyData()
        spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countryCodeList)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {}

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        checkBox.setOnCheckedChangeListener { _, _ ->
            signupButton.isEnabled = checkBox.isChecked
        }

        if(tvPin.text != tvConfirmPin.text){
            tvConfirmPin.error= "Pin does not match"
        }

//        signupButton.setOnClickListener{
//            val retrofitBuilder = Retrofit.Builder()
//                                  .addConverterFactory(GsonConverterFactory.create()).baseUrl()
//        }

    }
    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_Country).build().create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getCountryList()

        retrofitData.enqueue(object : Callback<CountryInfo?> {
            override fun onResponse(call: Call<CountryInfo?>, response: Response<CountryInfo?>) {
                val responseBody = response.body()

                for(country in responseBody!!.countryDetails){
                    countryCodeList.add("+" +country.ISDCode)
                }
            }

            override fun onFailure(call: Call<CountryInfo?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG)
            }
        })
    }
}