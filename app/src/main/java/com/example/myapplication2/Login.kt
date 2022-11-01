package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL_Country = "https://live.zebpay.co/api/v1/configuration/"
const val BASE_URL_Login = "https://www.zebpay.co/api/v1/"

 class Login : AppCompatActivity() {

    var countryNameList = ArrayList<String>()
     var countryCodeList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        countryNameList.add("India")
        countryCodeList.add("+91")
        loginbutton.isEnabled = false
        loginbutton.isClickable=false
        getMyData()
       spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countryNameList)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txt_country_code.text = countryCodeList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        checkBox.setOnCheckedChangeListener { _, _ ->
            loginbutton.isEnabled = checkBox.isChecked
            loginbutton.isClickable= checkBox.isChecked
        }

        getstartedbtn.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        loginbutton.setOnClickListener{
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_Login).build().create(ApiInterface2::class.java)
            val retrofitData = retrofitBuilder.userLogin(spinner.selectedItem.toString(), phnumtext.text.toString().trim().toBigInteger(),"ed42efd6-ba9e-4cfe-99a8-3531b6123148","46b341e4-ba8a-49db-9915-ea39c8281a65")

            retrofitData.enqueue(object : Callback<UserLoginResponse?>{
                override fun onResponse(
                    call: Call<UserLoginResponse?>,
                    response: Response<UserLoginResponse?>
                ) {
                    try {
                        val responseBody = response.body()
                        if(responseBody!!.statusCode.equals("200") ){
                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG)
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_LONG)
                        }
                    }catch (ex: Exception){
                        Toast.makeText(applicationContext,"Login Error",Toast.LENGTH_LONG)
                    }

                }

                override fun onFailure(call: Call<UserLoginResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to Login", Toast.LENGTH_LONG)
                }
            })
        }


    }

     private fun getMyData() {
         val retrofitBuilder = Retrofit.Builder()
             .addConverterFactory(GsonConverterFactory.create())
             .baseUrl(BASE_URL_Country).build().create(ApiInterface::class.java)
         val retrofitData = retrofitBuilder.getCountryList()

         retrofitData.enqueue(object : Callback<CountryInfo?>{
             override fun onResponse(call: Call<CountryInfo?>, response: Response<CountryInfo?>) {
                 try {
                     val responseBody = response.body()

                     for(country in responseBody!!.countryDetails){
                         countryNameList.add(country.Name)
                         countryCodeList.add("+" +country.ISDCode)
                     }
                 }catch (ex:Exception){}
             }

             override fun onFailure(call: Call<CountryInfo?>, t: Throwable) {
                 Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG)
             }
         })
     }
 }




