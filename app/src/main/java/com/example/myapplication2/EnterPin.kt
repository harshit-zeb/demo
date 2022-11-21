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
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.RequestFuture
import com.example.myapplication.MainActivity
import com.example.myapplication2.network.VolleyController
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_enter_pin.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_verifyotp.*
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box1
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box2
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box3
import kotlinx.android.synthetic.main.activity_verifyotp.otp_edit_box4
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


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

        var verificationId:String? = ""
        var verificationCompleteToken:String? = ""
        var otpTimestamp:String? = ""
        var apiSecret:String? = ""
        var apiKey:String? = ""
        var sessionToken:String? = ""
        val sharedPref = this@EnterPin?.getPreferences(Context.MODE_PRIVATE) ?: return
        var bundle:Bundle? = intent.extras
        if(sharedPref.getString("User","False") == "True"){
            verificationId= sharedPref.getString("verificationId","")
            verificationCompleteToken= sharedPref.getString("verificationCompleteToken","")
            otpTimestamp= sharedPref.getString("otpTimestamp","")
            apiSecret= sharedPref.getString("apiSecret","")
            apiKey= sharedPref.getString("apiKey","")
            sessionToken= sharedPref.getString("sessionToken","")
        }else if(bundle!= null){
            bundle= intent.extras!!
            verificationId= bundle.getString("verificationId")
            verificationCompleteToken= bundle.getString("verificationCompleteToken")
            otpTimestamp= bundle.getString("otpTimestamp")
            apiSecret= bundle.getString("apiSecret")
            apiKey= bundle.getString("apiKey")
            sessionToken= bundle.getString("sessionToken")
        }


        verify_pin_btn.setOnClickListener{
//            if(pinText == "1112") {
//                var intent = Intent(applicationContext, MainActivity::class.java)
//                finish()
//                startActivity(intent)
//            }

//            val retrofitBuilder = Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(BASE_URL_PIN).build().create(ApiInterfacePin::class.java)
//
//            val retrofitData = retrofitBuilder.userPin((621355968000000000L + (System.currentTimeMillis() * 10000) - otpTimestamp!!.toLong()).toString(), PinParams(verificationIntId!!,verificationId!!,verificationCompleteToken!!, "1112",""))

//            retrofitData.enqueue(object : Callback<UserPinResponse?> {
//                override fun onResponse(
//                    call: Call<UserPinResponse?>,
//                    response: Response<UserPinResponse?>
//                ) {
//                    try {
//                        val responseBody:UserPinResponse? = response.body()
//                        Log.d("response",responseBody.toString())
//                        if(responseBody!!.err == "Success"){
//                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG).show()
//                            val intent = Intent(applicationContext, MainActivity::class.java)
//                            finish()
//                            startActivity(intent)
//                        }else{
//                            Toast.makeText(applicationContext,"Invalid Pin", Toast.LENGTH_LONG).show()
//                            phnumtext.error ="Number not registered"
//                        }
//                    }catch (ex: Exception){
//                        Toast.makeText(applicationContext,"Login Error", Toast.LENGTH_LONG)
//                    }
//                }
//
//                override fun onFailure(call: Call<UserPinResponse?>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//
//            })
            val encyPin =
                AESHelper.encrypt("1112", apiSecret)
                    .trim()

            var parameterCollection = mutableMapOf<String, String>()
            parameterCollection["verificationId"] = verificationId!!
            parameterCollection["verificationCompleteToken"] = verificationCompleteToken!!
            parameterCollection["pin"] = encyPin

            var jsonResponse: UserOtpResponse.userOtpData? = null
            val t = Thread {
                Log.d("RT", "Thread t Begins")
                val url: String = "https://live.zebpay.co/api/v1/verifyaccountpin"
                var response = sendRequest(
                    url,
                    getParameters(parameterCollection),
                    apiSecret!!,
                    apiKey!!,
                    sessionToken!!,
                    (2 * getCurrentTimeTicks()!!.toLong() - otpTimestamp!!.toLong()).toString(),
                    "ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc"
                )

                if (response != null){
                    Log.e("responseeeeee", response!!)
                    jsonResponse = Gson()?.fromJson(response!!.trimIndent() , UserOtpResponse.userOtpData::class.java)
                    if (jsonResponse != null) {
                        if (jsonResponse!!.err == "Success") {
                            //Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }else{
                        // Toast.makeText(applicationContext, "invalid" , Toast.LENGTH_LONG).show()
                    }
                }


            }
            t.start()


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

    private fun getCurrentTimeTicks(): String? {
        return try {
            val tsLong = 621355968000000000L + System.currentTimeMillis() * 10000
            return tsLong.toString()
        } catch (e: java.lang.Exception) {
            return System.currentTimeMillis().toString()
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getParameters(parameterCollection: MutableMap<String, String>): String {
        var par = StringBuilder()

        for ((key, value) in parameterCollection) {
            par.append(URLEncoder.encode(key, "UTF-8")).append("=").append(value).append("&")
        }
        if (par.length > 0 && par[par.length - 1] == '&') {
            par = StringBuilder(par.substring(0, par.length - 1))
        }
        return par.toString()
    }

    fun sendRequest(
        requestName: String,
        requestParams: String?,
        apiSecret: String,
        apiKey: String,
        session: String,
        timeStamp: String,
        phoneHash: String,
    ): String? {
        var requestParams = requestParams
        val response: String

        return try {
            // Set application headers
            val phoneHashHeaderValue: String = phoneHash
            val appTokenHeaderValue: String = "0CB88193-1328-4AFA-B013-5CBF46D81AD0"
            val nonceHeaderValue: String? = getCurrentTimeTicks()
            val apiKeyHeaderValue: String = apiKey
            val sessionHeaderValue: String = session
            val requestIdHeaderValue = UUID.randomUUID().toString()
            var headers = HashMap<String?, String?>()
            val API_AUTHENTICATION_VERSION = "0"
            headers = setHeaderParameters(
                headers, API_AUTHENTICATION_VERSION,
                phoneHashHeaderValue,
                appTokenHeaderValue, nonceHeaderValue!!, apiKeyHeaderValue,
                sessionHeaderValue,
                timeStamp, requestIdHeaderValue
            )
            val headersMessage: String? =
                getHeadersForPayloadGeneration(
                    appTokenHeaderValue,
                    phoneHashHeaderValue, apiKeyHeaderValue, sessionHeaderValue,
                    requestIdHeaderValue
                )
            val payloadMessage: String? =
                getMessageForPayload(
                    timeStamp,
                    requestName, requestParams!!, headersMessage!!
                )
            val payLoad: String? = encodeParamsToSecret(apiSecret, payloadMessage!!)
            val SIGNATURE = "signature"
            headers[SIGNATURE] = payLoad

            val future = RequestFuture.newFuture<Any>()
            val mVolleyRequest = VolleyRequest(
                Request.Method.POST, requestName, headers,
                requestParams, future, future, false
            )

            //Set cache allow false
            mVolleyRequest.setShouldCache(false)

            VolleyController.getInstance(applicationContext).addToRequestQueue(mVolleyRequest);
            val apiResponse = future[1, TimeUnit.MINUTES]
            response = apiResponse.toString()


            //Set api error log in analytics
            val returnCode = "returncode"
            val err = "err"
            response
        } catch (ex: InterruptedException) {
            Log.e("Ex InterruptedException", ex.message.toString())
            null
        } catch (tm: TimeoutException) {
            Log.e("Ex TimeoutException", tm.message.toString())
            null
        } catch (e: ExecutionException) {
            Log.e("Ex ExecutionException", e.message.toString())
            null
        } catch (ex: Exception) {
            Log.e("Ex Exception", ex.message.toString())
            null
        }
    }

    private fun setHeaderParameters(
        headers: HashMap<String?, String?>,
        version: String,
        phoneHash: String,
        appToken: String,
        nonce: String,
        apiKey: String,
        session: String,
        timeStamp: String,
        requestId: String
    ): HashMap<String?, String?> {
        headers["version"] = version
        headers["phonehash"] = phoneHash
        headers["apptoken"] = appToken
        headers["nonce"] = nonce
        headers["apikey"] = apiKey
        headers["session"] = session
        headers["timestamp"] = timeStamp
        headers["reqid"] = requestId
        return headers
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
            Base64UtilJava.enCodeByArrayToBase64(result)
        } catch (ex: NoSuchAlgorithmException) {
            null
        } catch (ex: InvalidKeyException) {
            null
        }
    }

}