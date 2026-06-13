package com.example.lojaprodutos.domain.repository

import com.example.lojaprodutos.domain.model.Product

interface ProductRepository {
    fun insertProduct(product: Product): Unit
    fun getAllProducts(): List<Product>
    fun buyProducts(cart: List<Int>): Unit
}
