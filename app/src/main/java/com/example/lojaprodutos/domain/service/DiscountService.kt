package com.example.lojaprodutos.domain.service

import com.example.lojaprodutos.domain.repository.CouponRepository

class DiscountService(
    private val couponRepository: CouponRepository
) {
    suspend fun applyDiscount(value: Double, code: String?): Double {
        if (code == null)
            return value
        val coupon = couponRepository.getCoupon(code) ?: return value
        return value - (value * coupon.tax)
    }
}