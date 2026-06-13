package com.example.lojaprodutos.data.mapper

import com.example.lojaprodutos.data.source.local.entity.ProductEntity
import com.example.lojaprodutos.data.source.remote.dto.ProductDto
import com.example.lojaprodutos.domain.model.Product

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        priceInCents = (this.price * 100).toInt()
    )
}

fun ProductEntity.toModel(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.priceInCents / 100.0
    )
}
