package com.smart.app.util

import java.util.regex.Pattern

@Suppress("RegExpSimplifiable", "BooleanMethodIsAlwaysInverted")
class FormValidator {
    fun validateInput(password: String): Boolean {
        try {
            val isSatisfied = mutableListOf<Boolean>()
            val strictPatterns = listOf<Pattern>(
                Pattern.compile("[A-Z]"),
                Pattern.compile("[a-z]"),
                Pattern.compile("[0-9]"),
                Pattern.compile("[!@#$%^&*(){}?]")
            )
            val pwMinLen = 8
            val pwMaxLen = 16

            for (strictPattern in strictPatterns) {
                isSatisfied.add(strictPattern.matcher(password).find())
            }
            isSatisfied.add((password.length in pwMinLen..pwMaxLen))

            val predicate: (Boolean) -> Boolean = { it }
            return isSatisfied.all(predicate)

        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun validateInputEmail(emails: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()
    }
}