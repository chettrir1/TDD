package com.example.tdd.di

import android.content.Context
import androidx.room.Room
import com.example.tdd.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    /**
     * inMemoryDatabaseBuilder is called for saving data
     *to just in temporary memory and not in real device
     *done for test
     */
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) = Room.inMemoryDatabaseBuilder(
        context,
        ShoppingItemDatabase::class.java
    ).allowMainThreadQueries().build()
}