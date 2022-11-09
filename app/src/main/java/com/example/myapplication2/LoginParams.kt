package com.example.myapplication2

class LoginParams {
    var otpProvider:String? =null
    var ivrCapable:String? =null
    var mobileno:String? =null
    var countrycode:String? = null
    constructor(otpProvider:String, ivrCapable:String, mobileno:String, countrycode:String){
        this.otpProvider = otpProvider
        this.ivrCapable = ivrCapable
        this.mobileno = mobileno
        this.countrycode = countrycode
    }
}