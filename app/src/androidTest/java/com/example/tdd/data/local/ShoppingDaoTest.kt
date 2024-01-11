package com.example.tdd.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tdd.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var shoppingItemDatabase: ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingDao

    @Before
    fun setUp() {
        /**
         * inMemoryDatabaseBuilder is called for saving data
         *to just in temporary memory and not in real device
         *done for test
         */
        shoppingItemDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()

        shoppingDao = shoppingItemDatabase.shoppingDao()
    }

    @After
    fun tearDown() {
        shoppingItemDatabase.close()
    }


    /**
     * because of the function to be in suspend
     * the functions should be inside runTest
     */
    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem =
            ShoppingItem(name = "Wheel", price = 100f, amount = 100, imageUrl = "", id = 1)
        shoppingDao.insertShoppingItems(shoppingItem)
        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)

    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem =
            ShoppingItem(name = "Wheel", price = 100f, amount = 100, imageUrl = "", id = 1)
        shoppingDao.insertShoppingItems(shoppingItem)
        shoppingDao.deleteShoppingItem(shoppingItem)
        val allShoppingItem = shoppingDao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem1 =
            ShoppingItem(name = "Wheel", price = 10f, amount = 2, imageUrl = "", id = 1)
        val shoppingItem2 =
            ShoppingItem(name = "Wheel", price = 5.5f, amount = 4, imageUrl = "", id = 2)
        val shoppingItem3 =
            ShoppingItem(name = "Wheel", price = 100f, amount = 0, imageUrl = "", id = 3)

        shoppingDao.insertShoppingItems(shoppingItem1)
        shoppingDao.insertShoppingItems(shoppingItem2)
        shoppingDao.insertShoppingItems(shoppingItem3)

        val totalPriceSum = shoppingDao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}