package com.example.lojaprodutos.di

import android.content.Context
import com.example.lojaprodutos.data.source.local.MyDatabase
import com.example.lojaprodutos.domain.usecase.BuyProductsUseCase
import com.example.lojaprodutos.domain.usecase.GetAllProductsUseCase

class AppContainer(context: Context) {
    // Local Database
    private val database = MyDatabase.getDatabase(context)

    // DAOs
    private val productDao = database.getProductDao()

    // Repositories
    val productRepository by lazy {  }

    // Use Cases
    val GetAllProductsUseCase by lazy {
        GetAllProductsUseCase(productRepository)
    }
    val BuyProductsUseCase by lazy {
        BuyProductsUseCase(productRepository)
    }
}