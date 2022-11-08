package com.example.myapplication2

data class CountryInfo(
    val approximateActivationTime: String,
    val blockEndTime: String,
    val blockReason: String,
    val blocked: Boolean,
    val countryDetails: List<CountryDetailX>,
    val err: String,
    val nonce: String,
    val requestid: String,
    val returncode: Int,
    val timestamp: String,
    val title: Any
)