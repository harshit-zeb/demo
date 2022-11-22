package com.example.myapplication2

class UserOtpResponse {

    data class userOtpData(
        val VerificationCompleteToken: String,
        val VerificationRequestId: String,
        val VerificationStatus: String,
        val accountAge: String,
        val approximateActivationTime: String?,
        val blockEndTime: String,
        val blockReason: String?,
        val blocked: Boolean,
        val err: String,
        val nonce: String,
        val pinSetup: Int,
        val title: String?,
        val requestid: String,
        val returncode: Int,
        val timestamp: String,
    )
}