package com.example.myapplication2

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.math.BigInteger

interface ApiInterface {
    @GET("countrylist")
    fun getCountryList(): Call<CountryInfo>

}

interface ApiInterfaceLogin {
    @Headers(
        "apikey: null",
        "session: null",
        "apptoken: 0CB88193-1328-4AFA-B013-5CBF46D81AD0",
        "version: 0",
        "nonce: 637305869802650000",
        "phonehash: ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc",
        "reqid: 4eeb96b8-b09a-4629-9869-fccc14629d08"
    )
    @POST("getaccount")
    fun userLogin(
        @Header("timestamp") header: String,
        @Body body: LoginParams
    ): Call<UserLoginResponse>
}


