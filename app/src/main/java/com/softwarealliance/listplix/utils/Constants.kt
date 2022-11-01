package com.softwarealliance.listplix.utils

import java.util.regex.Pattern

object Constants {
    // digit + lowercase char + uppercase char + punctuation + symbol
    const val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"
//    const val GMAIL_PATTERN = "^[a-z0-9](\\.?[a-z0-9]){5,}@gmail\\.com$"
    const val GMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+"

    fun isValidPattern(input: String?, CHECK_PATTERN: String?): Boolean {
        val pattern = Pattern.compile(CHECK_PATTERN)
        val matcher = pattern.matcher(input)
        return matcher.matches()
    }

}