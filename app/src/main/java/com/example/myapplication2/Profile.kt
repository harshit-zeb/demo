package com.example.myapplication2

data class Profile(
    val Country: String,
    val CurrencyCode: String,
    val Email: String,
    val EmailVerified: Boolean,
    val HasSharedFacebookInfo: Boolean,
    val IsFBWalkthroughEnabled: Boolean,
    val IsFBWalkthroughRewardPaid: Boolean,
    val Language: String,
    val Name: String,
    val TimeZone: String,
    val profilePicBLOBUrl: Any,
    val profilePicUrl: String
)