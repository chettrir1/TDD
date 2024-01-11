package com.example.tdd

object RegistrationUtil {

    private var existingUsers = listOf("Raju", "Chettri")

    /**
     * the input is not valid if...
     * ...the username/password is empty
     * ...the username is already taken
     * ...the confirm password is not the same as password
     * ...the password contains less then 2 digits
     */
    fun validateRegistrationInput(
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return false
        }
        if (username in existingUsers) {
            return false
        }

        if (password != confirmPassword) {
            return false
        }

        if (password.count { it.isDigit() } < 2) {
            return false
        }
        return true
    }
}