package com.llyod.data.datasource.forecast

import com.llyod.data.common.Mapper
import com.llyod.data.common.NetworkStatus
import com.llyod.data.entity.location.country.CountryList
import com.llyod.domain.entity.country.CountryListUI
import com.llyod.data.mapper.LocationDataMapperToDomain
import com.llyod.data.network.CountryService
import com.llyod.domain.common.Result
import com.llyod.domain.common.Result.Success
import com.llyod.domain.exception.NoNetworkException
import com.llyod.domain.exception.NotFoundException
import com.llyod.domain.exception.UnAuthorizedApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    private val mapperToDomain: Mapper<CountryList, CountryListUI>,
    private val countryService: CountryService,
    private val networkStatus: NetworkStatus) : LocationDataSource {

    override suspend fun getLocationList(): Result<CountryListUI?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(Dispatchers.IO) {
                    val response = countryService.getCountryList()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(
                                response.body()?.let { mapperToDomain.mapFrom(it) }
                            )
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }
}