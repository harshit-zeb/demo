package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.myapplication.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


const val BASE_URL_Country = "https://live.zebpay.co/api/v1/configuration/"
const val BASE_URL_Login = "https://live.zebpay.co/api/v1/"

 class Login : AppCompatActivity() {

    var countryNameList = ArrayList<String>()
     var countryCodeList = ArrayList<String>()
     var isoCode = ArrayList<CountryDetailX>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPref = this@Login?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = "False"
        val userStatus= sharedPref.getString("User", defaultValue)
        if(userStatus == "True"){
            val intent = Intent(applicationContext, EnterPin::class.java)
            startActivity(intent)
        }
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
                .baseUrl(BASE_URL_Login).build().create(ApiInterfaceLogin::class.java)

            val retrofitData = retrofitBuilder.userLogin( System.currentTimeMillis().toString(), LoginParams("2","true",phnumtext.text.toString().trim(),isoCode.find { it.Name == spinner.selectedItem.toString()}!!.ThreeLetterIsoCode.toString()))

            retrofitData.enqueue(object : Callback<UserLoginResponse?>{
                override fun onResponse(
                    call: Call<UserLoginResponse?>,
                    response: Response<UserLoginResponse?>
                ) {
                    try {
                        val responseBody:UserLoginResponse? = response.body()
                        Log.d("response",responseBody.toString())
                        if(responseBody!!.err == "Success"){
                            val sharedPref = this@Login?.getPreferences(Context.MODE_PRIVATE) ?: return
                            with(sharedPref.edit()){
                                putString("User", "True")
                                apply()
                            }
                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, Verifyotp::class.java)
                            finish()
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_LONG).show()
                            phnumtext.error ="Number not registered"
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
                         isoCode.add(country)
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




