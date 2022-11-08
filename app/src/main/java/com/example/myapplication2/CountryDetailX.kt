package com.example.myapplication2

data class CountryDetailX(
    val CompanyName: String,
    val CompanyUrl: String,
    val CurrencyUnicode: String,
    val DefaultCountryCode: Any,
    val ISDCode: String,
    val IsCountrySupported: Boolean,
    val IsNationalityAllowed: Boolean,
    val IsReceiveAllowed: Boolean,
    val IsRequiredDSAR: Boolean,
    val IsResidencyAllowed: Boolean,
    val IsSendAllowed: Boolean,
    val Name: String,
    val NumericIsoCode: Int,
    val ThreeLetterIsoCode: String,
    val TwoLetterIsoCode: String,
    val currency: String,
    val states: List<StateX>
)