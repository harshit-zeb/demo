package com.example.myapplication2

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

interface ApiInterface {
    @GET("countrylist")
    fun getCountryList(): Call<CountryInfo>

}

interface ApiInterface2 {
    @Headers(
        "Zebpay-Subscription-Key: 24ADDBCD-76A5-4F20-9EA4-84F3B7D19F96",
        "Content-Type: application/json",
        "charset: utf-8"
    )
    @FormUrlEncoded
    @POST("registerdevice")
    fun deviceRegister(
        @Field("pin") pin: Number,
        @Field("mobile") mobile: Number,
        @Field("email") email: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("countrycode") countrycode: Number
    )
}

interface ApiInterfaceLogin{

    @Headers(
        "apikey: null",
        "session: null",
        "apptoken: D6E8C93D-FC2B-45A1-AF17-BC3E6F919934",
        "version: 0",
        "nonce: 637305869802650000",
        "phonehash: ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc",
        "reqid: 4eeb96b8-b09a-4629-9869-fccc14629d08"
    )
    @POST("getaccount")
    fun userLogin(@Header("timestamp") header: String, @Body body: LoginParams):Call<UserLoginResponse>
}

//https://live.zebpay.co/api/v1/registerdevice

