package com.example.myapplication2

//import retrofit2.converter.gson.GsonConverterFactory

import android.annotation.SuppressLint
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
import com.android.volley.Request.Method
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.myapplication2.network.VolleyController
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_verifyotp.*
import org.json.JSONObject
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


var otpText: String = ""
const val BASE_URL_Otp = "https://live.zebpay.co/api/v1/"

class Verifyotp : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
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
        otp_edit_box4.setOnKeyListener(GenericKeyEvent(otp_edit_box4, otp_edit_box3))
        otp_edit_box5.setOnKeyListener(GenericKeyEvent(otp_edit_box5, otp_edit_box4))
        otp_edit_box6.setOnKeyListener(GenericKeyEvent(otp_edit_box6, otp_edit_box5))

        //val sharedPref = this@Verifyotp?.getPreferences(Context.MODE_PRIVATE) ?: return
        // val verificationIntId= sharedPref.getString("verificationIntId","")
        //val verificationId= sharedPref.getString("verificationId","")
        //  val loginTimestamp= sharedPref.getString("loginTimestamp","")
        var verificationIntId: String? = ""
        var verificationId: String? = ""
        var loginTimestamp: String? = ""
        var sessionToken: String? = ""
        var apiKey: String? = ""
        var apiSecret: String? = ""
        var bundle: Bundle = intent.extras!!
        if (bundle != null) {
            verificationIntId = bundle.getString("verificationIntId")
            verificationId = bundle.getString("verificationId")
            loginTimestamp = bundle.getString("loginTimestamp")
            sessionToken = bundle.getString("session")
            apiKey = bundle.getString("apiKey")
            apiSecret = bundle.getString("apiSecret")
        }

        Log.d("verificationIntId", verificationIntId!!)
        Log.d("verificationId", verificationId!!)
        Log.d("loginTimestamp", loginTimestamp!!)
        Log.d("apiSecret", apiSecret!!)
        Log.d("sessionToken", sessionToken!!)


        verify_otp_btn.setOnClickListener {
            Log.d("otp", otpText)
            var reqId = UUID.randomUUID().toString()
            Log.d(
                "loginTimestampDiff",
                (2 * (621355968000000000L + System.currentTimeMillis() * 10000) - loginTimestamp!!.toLong()).toString()
            )
//
//            val retrofitBuilder = Retrofit.Builder()
//                .baseUrl(BASE_URL_Otp).build().create(ApiInterfaceOtp::class.java)

            var parameterCollection = mutableMapOf<String, String>()
            parameterCollection["verificationId"] = verificationId!!
            parameterCollection["messageReceived"] = "840429 is your Zebpay verification code"
            parameterCollection["code"] = "840429"
            parameterCollection["otpAuthToken"] = ""


            var jsonResponse: UserOtpResponse.userOtpData? = null
            val t = Thread {
                Log.d("RT", "Thread t Begins")
                val url: String = "https://live.zebpay.co/api/v1/verifyaccountcode"
                var response = sendRequest(
                    url,
                    getParameters(parameterCollection),
                    apiSecret,
                    apiKey!!,
                    sessionToken,
                    (2 * getCurrentTimeTicks()!!.toLong() - loginTimestamp!!.toLong()).toString(),
                    "ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc"
                )

                if (response != null){
                    Log.e("responseeeeee", response!!)
                    jsonResponse = Gson()?.fromJson(response!!.trimIndent() , UserOtpResponse.userOtpData::class.java)
                    if (jsonResponse != null) {
                        if (jsonResponse!!.err == "Success") {
                            //Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                            val sharedPref = getSharedPreferences("myData",Context.MODE_PRIVATE)
                            val editor = sharedPref!!.edit()
                                editor.putString(
                                    "verificationCompleteToken",
                                    jsonResponse!!.VerificationCompleteToken
                                )
                            editor.putString("otpTimestamp", jsonResponse!!.timestamp)
                            editor.putString("verificationCompleteToken", jsonResponse!!.VerificationCompleteToken)
                            editor.putString("apiSecret",apiSecret)
//                            editor.putString("apiKey",apiKey)
//                            editor.putString("sessionToken",sessionToken)
                            editor.commit()

                            val intent = Intent(applicationContext, EnterPin::class.java)
                            intent.putExtra(
                                "verificationCompleteToken",
                                jsonResponse!!.VerificationCompleteToken
                            )
                            intent.putExtra("otpTimestamp", jsonResponse!!.timestamp)
                            intent.putExtra("apiKey", apiKey!!)
                            intent.putExtra("apiSecret", apiSecret!!)
                            intent.putExtra("verificationId", verificationId!!)
                            intent.putExtra("sessionToken", sessionToken!!)
                            startActivity(intent)
                        }
                    }else{
                       // Toast.makeText(applicationContext, "invalid" , Toast.LENGTH_LONG).show()
                    }
                }


            }
            t.start()




//            CoroutineScope(Dispatchers.IO).launch {
//                val retrofitData = retrofitBuilder.userOtp( headers, requestBody)
//
//                withContext(Dispatchers.Main) {
//                    if(retrofitData.isSuccessful){
//                        val gson = GsonBuilder().setPrettyPrinting().create()
//                        val prettyJson = gson.toJson(
//                            JsonParser.parseString(
//                                retrofitData.body()
//                                    ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                            ))
//                        Log.d("responseeeeeee ", prettyJson)
//                    }else{
//                        Log.e("RETROFIT_ERROR", retrofitData.code().toString())
//                    }
//                }
//
//            }


//            retrofitData.enqueue(object : Callback<UserOtpResponse?> {
//                override fun onResponse(
//                    call: Call<UserOtpResponse?>,
//                    response: Response<UserOtpResponse?>
//                ) {
//                    try {
//                        val responseBody:UserOtpResponse? = response.body()
//                        Log.d("response",responseBody.toString())
//                        if(responseBody!!.err == "Success"){
//                            Toast.makeText(applicationContext,"Login Successful" , Toast.LENGTH_LONG).show()
//                            val sharedPref = this@Verifyotp?.getPreferences(Context.MODE_PRIVATE) ?: return
//                            with(sharedPref.edit()){
//                                putString("verificationCompleteToken",  responseBody!!.VerificationCompleteToken)
//                                putString("otpTimestamp",  responseBody!!.timestamp)
//                                putString("otpTimestamp",  responseBody!!.timestamp)
//                                apply()
//                            }
//                            val intent = Intent(applicationContext, EnterPin::class.java)
//                            intent.putExtra("verificationCompleteToken",  responseBody!!.VerificationCompleteToken)
//                            intent.putExtra("otpTimestamp",  responseBody!!.timestamp)
//                            intent.putExtra("verificationIntId", verificationId!!)
//                            intent.putExtra("verificationId",  verificationId!!)
//                            startActivity(intent)
//                        }else{
//                            Toast.makeText(applicationContext,"Invalid Otp",Toast.LENGTH_LONG).show()
//
//                        }
//                    }catch (ex: Exception){
//                        Toast.makeText(applicationContext,"Login Error",Toast.LENGTH_LONG)
//                    }
//                }
//
//                override fun onFailure(call: Call<UserOtpResponse?>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//            })
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


    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.otp_edit_box1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            otpText += text

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
            Base64UtilJava.enCodeByArrayToBase64(result)
        } catch (ex: NoSuchAlgorithmException) {
            null
        } catch (ex: InvalidKeyException) {
            null
        }
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
                Method.POST, requestName, headers,
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

    fun getPublicRequestQueue(): RequestQueue? {
        var mNormalRequestQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)
        return mNormalRequestQueue
    }

    private fun  setHeaderParameters(
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
}