package com.wein_business.utils

object Validator {

    private val mobileRegex = Regex("(05\\d{8})")
    private val emailRegex = Regex("^(.+)@(.+)\$")
    private val passwordRegex =
        Regex("^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$")

    fun validateMobile(mobile: String): Boolean {
        return mobileRegex.matches(mobile)
    }

    fun validateEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun validatePassword(password: String): Boolean {
        return passwordRegex.matches(password)
    }
}