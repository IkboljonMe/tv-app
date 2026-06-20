package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.HotelRepository
import javax.inject.Inject

class GetRoomDetailUseCase @Inject constructor(
    private val hotelRepository: HotelRepository
) {
    suspend operator fun invoke() = hotelRepository.getRoomDetail()
}