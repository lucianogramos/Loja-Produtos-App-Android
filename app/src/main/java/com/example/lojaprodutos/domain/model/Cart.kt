package com.example.lojaprodutos.domain.model

data class Cart(
    val products: List<Product>
) {
    fun getTotalPrice(): Double {
        return products.sumOf { it.price }
    }
}
