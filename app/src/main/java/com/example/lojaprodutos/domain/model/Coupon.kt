package com.example.lojaprodutos.domain.model

data class Coupon (
    val code: String,
    val tax: Double
) {
    init {
        require(code.isNotBlank())
        if (tax <= 0 || tax > 1)
            throw IllegalArgumentException("O desconto deve ser maior que 0 e menor ou igual que 1")
    }
}