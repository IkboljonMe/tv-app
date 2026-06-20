package com.karuhun.core.domain.usecase

import com.karuhun.core.common.Resource
import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.domain.repository.HotelRepository
import com.karuhun.core.model.Hotel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHotelProfileUseCase @Inject constructor(
    private val hotelRepository: HotelRepository
) {
    suspend operator fun invoke() : Flow<HotelProfile> {
        return hotelRepository.getHotelProfile()
    }
}