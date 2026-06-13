package com.example.lojaprodutos.domain.repository

import com.example.lojaprodutos.domain.model.Coupon

interface CouponRepository {
    suspend fun getCoupon(code: String): Coupon?
    suspend fun insertCoupon(coupon: Coupon)
}