package com.example.lojaprodutos.domain.usecase

import com.example.lojaprodutos.domain.model.Coupon
import com.example.lojaprodutos.domain.repository.CouponRepository

class AddCouponUseCase(
    private val couponRepository: CouponRepository
) {
    suspend operator fun invoke(coupon: Coupon) {
        couponRepository.insertCoupon(coupon)
    }
}
