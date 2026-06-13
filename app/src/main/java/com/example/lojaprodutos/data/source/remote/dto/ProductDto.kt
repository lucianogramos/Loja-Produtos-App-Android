package com.example.lojaprodutos.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name_product") val name: String,
    @SerialName("price_in_cents") val priceInCents: Int
)
