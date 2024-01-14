package com.example.tdd.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tdd.MainCoroutineRule
import com.example.tdd.getOrAwaitValue
import com.example.tdd.other.Constants
import com.example.tdd.other.Status
import com.example.tdd.repository.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel


    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("Wheel", "", "100")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item name with long character, returns error`() {
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(name, "100", "100")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item price with long character, returns error`() {
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("Wheel", "100", price)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item amount is long, returns error`() {
        viewModel.insertShoppingItem("Wheel", "100000000000000000000000", "100")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("Wheels", "10", "100")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}