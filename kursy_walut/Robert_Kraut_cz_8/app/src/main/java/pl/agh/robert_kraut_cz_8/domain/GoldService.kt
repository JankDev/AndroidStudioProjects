package pl.agh.robert_kraut_cz_8.domain

import pl.agh.robert_kraut_cz_8.config.retrofit

class GoldService {
    private val nbpApi = retrofit.create(NBPApi::class.java)

    suspend fun getGoldListings() =  try {
        nbpApi.getGoldPrice(30)
            .takeIf { it.isSuccessful }
            ?.body()
            ?: throw RuntimeException("Error loading gold price")
    }catch (ex: Exception) {
        throw RuntimeException(ex.message)
    }
}