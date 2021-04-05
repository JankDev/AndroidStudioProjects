package pl.agh.robert_kraut_cz_8.model

data class CurrencyDetail(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<Rate>
)

data class Rate(
    val effectiveData: String,
    val mid: Double,
)