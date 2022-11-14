package com.example.myapplication2

data class UserPinResponse(
    val APIKey: String,
    val APISecret: String,
    val SessionToken: String,
    val approximateActivationTime: String,
    val balanceInSatoshi: String,
    val blockEndTime: String,
    val blockReason: String,
    val blocked: Boolean,
    val contacts: List<Any>,
    val err: String,
    val guid: String,
    val nonce: String,
    val notificationHandle: Any,
    val profile: Profile,
    val requestid: String,
    val returncode: Int,
    val timestamp: String,
    val transactions: List<Any>,
    val verificationStatusMessage: String,
    val verificationlevel: Int,
    val verificationstatus: Int
)