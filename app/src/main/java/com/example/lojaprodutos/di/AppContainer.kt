package com.example.lojaprodutos.di

import android.content.Context
import com.example.lojaprodutos.data.repository.ProductRepositoryImpl
import com.example.lojaprodutos.data.source.local.MyDatabase
import com.example.lojaprodutos.domain.usecase.AddProductUseCase
import com.example.lojaprodutos.domain.usecase.BuyProductsUseCase
import com.example.lojaprodutos.domain.usecase.GetAllProductsUseCase

class AppContainer(context: Context) {
    // Local Database
    private val database = MyDatabase.getDatabase(context)

    // DAOs
    private val productDao = database.getProductDao()

    // Repositories
    val productRepository by lazy {
        ProductRepositoryImpl(productDao)
    }

    // Use Cases
    val getAllProductsUseCase by lazy {
        GetAllProductsUseCase(productRepository)
    }

    val addProductUseCase by lazy {
        AddProductUseCase(productRepository)
    }

    val buyProductsUseCase by lazy {
        BuyProductsUseCase(productRepository)
    }
}