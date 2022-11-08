package com.example.myapplication2

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
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

var timeStamp = System.currentTimeMillis()

interface ApiInterfaceLogin{

    @POST("getaccount")
    fun userLogin(@HeaderMap headers: Map<String, String?>,@Body body: JSONObject):Call<UserLoginResponse>
}

//https://live.zebpay.co/api/v1/registerdevice

