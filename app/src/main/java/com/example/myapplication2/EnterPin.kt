package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.MainActivity
import kotlinx.android.synthetic.main.activity_enter_pin.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_verifyotp.*
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box1
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box2
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box3
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var pinText: String = ""
var BASE_URL_PIN = "https://live.zebpay.co/api/v1/"
class EnterPin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_pin)
        otp_edit_box1.addTextChangedListener(GenericTextWatcher(otp_edit_box1, otp_edit_box2))
        otp_edit_box2.addTextChangedListener(GenericTextWatcher(otp_edit_box2, otp_edit_box3))
        otp_edit_box3.addTextChangedListener(GenericTextWatcher(otp_edit_box3, otp_edit_box4))
        otp_edit_box4.addTextChangedListener(GenericTextWatcher(otp_edit_box4, null))

        otp_edit_box1.setOnKeyListener(GenericKeyEvent(otp_edit_box1, null))
        otp_edit_box2.setOnKeyListener(GenericKeyEvent(otp_edit_box2, otp_edit_box1))
        otp_edit_box3.setOnKeyListener(GenericKeyEvent(otp_edit_box3, otp_edit_box2))
        otp_edit_box4.setOnKeyListener(GenericKeyEvent(otp_edit_box4,otp_edit_box3))

        var verificationIntId:String? = ""
        var verificationId:String? = ""
        var verificationCompleteToken:String? = ""
        var otpTimestamp:String? = ""
        val sharedPref = this@EnterPin?.getPreferences(Context.MODE_PRIVATE) ?: return
        val bundle:Bundle? = intent.extras!!
        if(sharedPref.getString("User","False") == "True"){
            verificationIntId= sharedPref.getString("verificationIntId","")
            verificationId= sharedPref.getString("verificationId","")
            verificationCompleteToken= sharedPref.getString("verificationCompleteToken","")
            otpTimestamp= sharedPref.getString("otpTimestamp","")
        }else if(bundle!= null){
            verificationIntId= bundle.getString("verificationIntId")
            verificationId= bundle.getString("verificationId")
            verificationCompleteToken= bundle.getString("verificationCompleteToken")
            otpTimestamp= bundle.getString("otpTimestamp")
        }


        verify_pin_btn.setOnClickListener{
//            if(pinText == "1112") {
//                var intent = Intent(applicationContext, MainActivity::class.java)
//                finish()
//                startActivity(intent)
//            }

            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_PIN).build().create(ApiInterfacePin::class.java)

            val retrofitData = retrofitBuilder.userPin((621355968000000000L + (System.currentTimeMillis() * 10000) - otpTimestamp!!.toLong()).toString(), PinParams(verificationIntId!!,verificationId!!,verificationCompleteToken!!, "1112",""))

            retrofitData.enqueue(object : Callback<UserPinResponse?> {
                override fun onResponse(
                    call: Call<UserPinResponse?>,
                    response: Response<UserPinResponse?>
                ) {
                    try {
                        val responseBody:UserPinResponse? = response.body()
                        Log.d("response",responseBody.toString())
                        if(responseBody!!.err == "Success"){
                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            finish()
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Invalid Pin", Toast.LENGTH_LONG).show()
                            phnumtext.error ="Number not registered"
                        }
                    }catch (ex: Exception){
                        Toast.makeText(applicationContext,"Login Error", Toast.LENGTH_LONG)
                    }
                }

                override fun onFailure(call: Call<UserPinResponse?>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })
//            }else{
//                Toast.makeText(applicationContext, "Wrong OTP" , Toast.LENGTH_LONG).show()
//                otp_edit_box1.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//                otp_edit_box2.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//                otp_edit_box3.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//                otp_edit_box4.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//                otp_edit_box5.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//                otp_edit_box6.setBackgroundResource(R.drawable.edittext_curve_bg_error)
//            }
        }


    }

    class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.otp_edit_box1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            pinText+=text
            when (currentView.id) {
                R.id.otp_edit_box1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box3 -> if (text.length == 1) nextView!!.requestFocus()
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }
}