package com.example.lojaprodutos.data.mapper

import com.example.lojaprodutos.data.source.local.entity.CouponEntity
import com.example.lojaprodutos.domain.model.Coupon

fun Coupon.toEntity(): CouponEntity {
    return CouponEntity(
        code = this.code,
        tax = this.tax
    )
}

fun CouponEntity.toModel(): Coupon {
    return Coupon(
        code = this.code,
        tax = this.tax
    )
}