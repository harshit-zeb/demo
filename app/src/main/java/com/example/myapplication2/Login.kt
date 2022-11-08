package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL_Country = "https://live.zebpay.co/api/v1/configuration/"
const val BASE_URL_Login = "https://live.zebapi.com/api/v1/"

 class Login : AppCompatActivity() {

    var countryNameList = ArrayList<String>()
     var countryCodeList = ArrayList<String>()
     var isoCode = ArrayList<CountryDetail>()
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
            Log.d("Code", isoCode.find { it.Name == spinner.selectedItem.toString()}!!.TwoLetterIsoCode.toString().lowercase())
            Log.d("Num" , phnumtext.text.toString().trim())
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_Login).build().create(ApiInterfaceLogin::class.java)
            var headers = HashMap<String, String?>()
            headers["apikey"] = null
            headers["session"] =null
            headers["apptoken"] = "D6E8C93D-FC2B-45A1-AF17-BC3E6F919934"
            headers["version"]="0"
            headers["nonce"] = "637305869802650000"
            headers["phonehash"] = "ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc"
            headers["timestamp"] = System.currentTimeMillis().toString()
            headers["reqid"]="4eeb96b8-b09a-4629-9869-fccc14629d08"

            var paramObj = JSONObject()
            paramObj.put("ivrCapable","true")
            paramObj.put("otpProvider","2")
            paramObj.put("mobileno",phnumtext.text.toString().trim().toBigInteger())
            paramObj.put("countrycode",isoCode.find { it.Name == spinner.selectedItem.toString()}!!.ThreeLetterIsoCode.toString())
            Log.d("obj",paramObj.toString())
            val retrofitData = retrofitBuilder.userLogin( headers, paramObj)

            retrofitData.enqueue(object : Callback<UserLoginResponse?>{
                override fun onResponse(
                    call: Call<UserLoginResponse?>,
                    response: Response<UserLoginResponse?>
                ) {
                    try {
                        val responseBody = response.body()
                        Log.d("response",responseBody.toString())
                        if(responseBody!!.VerificationIntId.isNotEmpty()){
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




