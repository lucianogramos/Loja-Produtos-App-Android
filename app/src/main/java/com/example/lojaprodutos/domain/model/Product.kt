package com.example.lojaprodutos.domain.model

data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double
) {
    init {
        require(name.isNotBlank())
        if (price <= 0)
            throw IllegalArgumentException("O preço não pode ser menor ou igual a 0")
    }
}
