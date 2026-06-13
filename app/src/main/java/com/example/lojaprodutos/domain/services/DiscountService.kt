package com.example.lojaprodutos.domain.services

import com.example.lojaprodutos.domain.repository.CouponRepository

class DiscountService(
    private val couponRepository: CouponRepository
) {
    suspend fun applyDiscount(value: Double, couponCode: String?): Double {
        if (couponCode == null)
            return value
        val coupon = couponRepository.getCoupon(couponCode) ?: return value
        return value - (value * coupon.tax)
    }
}