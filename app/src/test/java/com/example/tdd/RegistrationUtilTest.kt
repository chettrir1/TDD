package com.example.tdd

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and confirm password and password same returns true`() {
        val result = RegistrationUtil.validateRegistrationInput("raju", "123", "123")
        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Raju", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `password or confirm password is empty returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Raju", "", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `password or confirm password contains less then 2 digits returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Raju", "abcede", "abcede")
        assertThat(result).isFalse()
    }

    @Test
    fun `password or confirm password are not same returns false`() {
        val result = RegistrationUtil.validateRegistrationInput("Raju", "124", "123")
        assertThat(result).isFalse()
    }

}