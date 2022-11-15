package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_verifyotp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import zebpay.application.utils.Base64Util
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


var otpText:String = ""
const val BASE_URL_Otp = "https://live.zebpay.co/api/v1/"
class Verifyotp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifyotp)

        otp_edit_box1.addTextChangedListener(GenericTextWatcher(otp_edit_box1, otp_edit_box2))
        otp_edit_box2.addTextChangedListener(GenericTextWatcher(otp_edit_box2, otp_edit_box3))
        otp_edit_box3.addTextChangedListener(GenericTextWatcher(otp_edit_box3, otp_edit_box4))
        otp_edit_box4.addTextChangedListener(GenericTextWatcher(otp_edit_box4, otp_edit_box5))
        otp_edit_box5.addTextChangedListener(GenericTextWatcher(otp_edit_box5, otp_edit_box6))
        otp_edit_box6.addTextChangedListener(GenericTextWatcher(otp_edit_box6, null))



        otp_edit_box1.setOnKeyListener(GenericKeyEvent(otp_edit_box1, null))
        otp_edit_box2.setOnKeyListener(GenericKeyEvent(otp_edit_box2, otp_edit_box1))
        otp_edit_box3.setOnKeyListener(GenericKeyEvent(otp_edit_box3, otp_edit_box2))
        otp_edit_box4.setOnKeyListener(GenericKeyEvent(otp_edit_box4,otp_edit_box3))
        otp_edit_box5.setOnKeyListener(GenericKeyEvent(otp_edit_box5,otp_edit_box4))
        otp_edit_box6.setOnKeyListener(GenericKeyEvent(otp_edit_box6,otp_edit_box5))

        val sharedPref = this@Verifyotp?.getPreferences(Context.MODE_PRIVATE) ?: return
       // val verificationIntId= sharedPref.getString("verificationIntId","")
        //val verificationId= sharedPref.getString("verificationId","")
      //  val loginTimestamp= sharedPref.getString("loginTimestamp","")
         var verificationIntId : String? = ""
         var verificationId :String? = ""
         var loginTimestamp :String? = ""
         var sessionToken :String? = ""
         var apiKey :String? = ""
         var apiSecret :String? = ""
        var bundle: Bundle = intent.extras!!
        if(bundle != null){
             verificationIntId= bundle.getString("verificationIntId")
            verificationId= bundle.getString("verificationId")
            loginTimestamp= bundle.getString("loginTimestamp")
            sessionToken= bundle.getString("session")
            apiKey= bundle.getString("apiKey")
            apiSecret= bundle.getString("apiSecret")
        }

        Log.d("verificationIntId" , verificationIntId!!)
        Log.d("verificationId" , verificationId!!)
        Log.d("loginTimestamp" , loginTimestamp!!)
        Log.d("apiSecret" , apiSecret!!)
        Log.d("sessionToken" , sessionToken!!)
        Log.d("loginTimestampDiff" ,(getCurrentTimeTicks()!!.toLong()- loginTimestamp!!.toLong()).toString())


        verify_otp_btn.setOnClickListener{
        Log.d("otp", otpText)
            Log.d("loginTimestampDiff" ,((621355968000000000L + System.currentTimeMillis() * 10000)- loginTimestamp!!.toLong()).toString())
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_Otp).build().create(ApiInterfaceOtp::class.java)
                var headers = mutableMapOf<String, String>()
            headers["timestamp"] = (2*(621355968000000000L + (System.currentTimeMillis() * 10000))- loginTimestamp!!.toLong()).toString()
            headers["session"] = sessionToken!!
            headers["apikey"] = apiKey!!

            val headersMessage: String? =
                getHeadersForPayloadGeneration(
                    "0CB88193-1328-4AFA-B013-5CBF46D81AD0",
                    "ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc", apiKey!!, sessionToken!!,
                    "e7000b3f-b963-4fba-b126-a974d2b196dd"
                )
            val payloadMessage: String? =
                    getMessageForPayload(
                        ((621355968000000000L + (System.currentTimeMillis() * 10000))- loginTimestamp!!.toLong()).toString(),
                    "https://live.zebpay.co/api/v1/verifyaccountcode", "dummy=1", headersMessage!!
                )
            val payLoad: String? =  encodeParamsToSecret(apiSecret!!, payloadMessage!!)
            val SIGNATURE = "signature"
            Log.d("payload", payLoad!!)
            headers[SIGNATURE] = payLoad!!
            val retrofitData = retrofitBuilder.userOtp( headers, OtpParams(verificationIntId!!,verificationId!!,"840429 is your Zebpay verification code", "840429",""))

            retrofitData.enqueue(object : Callback<UserOtpResponse?> {
                override fun onResponse(
                    call: Call<UserOtpResponse?>,
                    response: Response<UserOtpResponse?>
                ) {
                    try {
                        val responseBody:UserOtpResponse? = response.body()
                        Log.d("response",responseBody.toString())
                        if(responseBody!!.err == "Success"){
                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG).show()
                            val sharedPref = this@Verifyotp?.getPreferences(Context.MODE_PRIVATE) ?: return
                            with(sharedPref.edit()){
                                putString("verificationCompleteToken",  responseBody!!.VerificationCompleteToken)
                                putString("otpTimestamp",  responseBody!!.timestamp)
                                putString("otpTimestamp",  responseBody!!.timestamp)
                                apply()
                            }
                            val intent = Intent(applicationContext, EnterPin::class.java)
                            intent.putExtra("verificationCompleteToken",  responseBody!!.VerificationCompleteToken)
                            intent.putExtra("otpTimestamp",  responseBody!!.timestamp)
                            intent.putExtra("verificationIntId", verificationId!!)
                            intent.putExtra("verificationId",  verificationId!!)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Invalid Otp",Toast.LENGTH_LONG).show()
                        }
                    }catch (ex: Exception){
                        Toast.makeText(applicationContext,"Login Error",Toast.LENGTH_LONG)
                    }
                }

                override fun onFailure(call: Call<UserOtpResponse?>, t: Throwable) {
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

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            otpText+=text

            when (currentView.id) {
                R.id.otp_edit_box1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box4 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp_edit_box5 -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
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


    private fun getHeadersForPayloadGeneration(
        appToken: String,
        phoneHash: String,
        apiKey: String,
        session: String,
        reqId: String
    ): String? {
        var headersInpayLoad = ""
        headersInpayLoad += "apptoken:$appToken"
        headersInpayLoad += "phoneHash:$phoneHash"
        headersInpayLoad += "apikey:$apiKey"
        headersInpayLoad += "session:$session"
        headersInpayLoad += "reqid:$reqId"
        return headersInpayLoad
    }

    private fun getMessageForPayload(
        date: String,
        uri: String,
        parameters: String,
        headers: String
    ): String? {
        return """
            POST
            $date
            $uri
            $parameters
            $headers
            """.trimIndent()
    }

    fun encodeParamsToSecret(apiSecret: String, parameters: String): String? {
        var mac: Mac? = null
        var result: ByteArray? = null
        return try {
            mac = Mac.getInstance("HmacSHA256")
            val key = apiSecret.trim { it <= ' ' }
            val keyBytes = key.toByteArray()
            val sk = SecretKeySpec(keyBytes, mac.algorithm)
            mac.init(sk)
            result = mac.doFinal(parameters.toByteArray(StandardCharsets.UTF_8))
            Base64Util.enCodeByArrayToBase64(result)
        } catch (ex: NoSuchAlgorithmException) {
            null
        } catch (ex: InvalidKeyException) {
            null
        }
    }

    fun getCurrentTimeTicks(): String? {
        return try {
            val tsLong = 621355968000000000L + System.currentTimeMillis() * 10000
            tsLong.toString()
        } catch (e: java.lang.Exception) {
            System.currentTimeMillis().toString()
        }
    }

}