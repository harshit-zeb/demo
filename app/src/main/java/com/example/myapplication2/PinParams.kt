package com.example.myapplication2

class PinParams {
    var verificationIntId:String? =null
    var verificationId:String? =null
    var verificationCompleteToken:String? =null
    var pin:String? = null
    var otpAuthToken:String? =null
    constructor(verificationIntId:String, verificationId:String, verificationCompleteToken:String, pin:String, otpAuthToken:String){
        this.verificationIntId = verificationIntId
        this.verificationId = verificationId
        this.verificationCompleteToken = verificationCompleteToken
        this.pin = pin
        this.otpAuthToken =otpAuthToken
    }
}