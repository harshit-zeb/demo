package com.example.myapplication2

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

interface ApiInterface {
    @GET("countrylist")
    fun getCountryList(): Call<CountryInfo>

}

interface ApiInterfaceRegister {
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
        "apptoken: 0CB88193-1328-4AFA-B013-5CBF46D81AD0",
        "version: 0",
        "nonce: 637305869802650000",
        "phonehash: ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc",
        "reqid: 4eeb96b8-b09a-4629-9869-fccc14629d08"
    )
    @POST("getaccount")
    fun userLogin(@Header("timestamp") header: String, @Body body: LoginParams):Call<UserLoginResponse>
}

interface ApiInterfaceOtp{

    @Headers(
        "apikey: 61600d09c9954aef897c7f8a249accfe",
        "session: 9590045d-21d2-4cd5-aded-9882bdb22d11",
        "apptoken: 0CB88193-1328-4AFA-B013-5CBF46D81AD0",
        "signature: 86bda55c-c7bd-4bb2-b97a-ab4ee6ae0286",
        "version: 0",
        "nonce: 637305869802650000",
        "phonehash: ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc=",
        "reqid: e7000b3f-b963-4fba-b126-a974d2b196dd"
    )
    @POST("verifyaccountcode")
    fun userOtp(@Header("timestamp") header: String, @Body body: OtpParams):Call<UserOtpResponse>

}

interface ApiInterfacePin{

    @Headers(
        "apikey: 61600d09c9954aef897c7f8a249accfe",
        "session: 86bda55c-c7bd-4bb2-b97a-ab4ee6ae0286",
        "apptoken: 0CB88193-1328-4AFA-B013-5CBF46D81AD0",
        "sinature: wf4Hs5Mnu9ezF2eJ9Z8uUmk7ecfyh2AIfXEcYdYcllM=",
        "version: 0",
        "nonce: 637305869802650000",
        "phonehash: ezSWtbhJ/WK3G6kI+ggMJcxUCJ4yWCApzK/l36nyhYc",
        "reqid: e7000b3f-b963-4fba-b126-a974d2b196dd"
    )
    @POST("verifyaccountpin")
    fun userPin(@Header("timestamp") header: String, @Body body: PinParams):Call<UserPinResponse>

}

//https://live.zebpay.co/api/v1/registerdevice

