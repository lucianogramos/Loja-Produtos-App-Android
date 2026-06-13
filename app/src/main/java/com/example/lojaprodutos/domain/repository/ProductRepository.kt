package com.example.lojaprodutos.domain.repository

import com.example.lojaprodutos.domain.model.Product

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    fun buyProducts(cart: List<Int>)
}
