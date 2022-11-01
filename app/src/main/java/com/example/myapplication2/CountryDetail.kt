package com.example.myapplication2

data class CountryDetail(
    val AllowsRegistration: Boolean,
    val ISDCode: String,
    val Id: Int,
    val IsNationalityAllowed: Boolean,
    val IsResidencyAllowed: Boolean,
    val Name: String,
    val NumericIsoCode: Int,
    val States: List<State>,
    val ThreeLetterIsoCode: String,
    val TwoLetterIsoCode: String
)