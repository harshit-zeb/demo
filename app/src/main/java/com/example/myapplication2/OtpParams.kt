package com.example.myapplication2

class OtpParams {
    var verificationIntId:String? =null
    var verificationId:String? =null
    var messageReceived:String? =null
    var code:String? = null
    var otpAuthToken:String? = null
    constructor(verificationIntId:String, verificationId:String, messageReceived:String, code:String, otpAuthToken:String){
        this.verificationIntId = verificationIntId
        this.verificationId = verificationId
        this.messageReceived = messageReceived
        this.code = code
        this.otpAuthToken= otpAuthToken
    }
}