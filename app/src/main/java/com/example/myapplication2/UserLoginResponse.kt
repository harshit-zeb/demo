package com.example.myapplication2

data class UserLoginResponse(
    val APIKey: String,
    val APISecret: String,
    val CountryConfiguration: CountryConfiguration,
    val End: String,
    val Id: Any,
    val OtpProviderAllowed: Boolean,
    val SessionToken: String,
    val Start: String,
    val VerificationIntId: String,
    val VerificationRequestId: String,
    val approximateActivationTime: String,
    val blockEndTime: String,
    val blockReason: String,
    val blocked: Boolean,
    val err: String,
    val nonce: String,
    val publicGUID: String,
    val requestid: String,
    val returncode: Int,
    val timestamp: String
)