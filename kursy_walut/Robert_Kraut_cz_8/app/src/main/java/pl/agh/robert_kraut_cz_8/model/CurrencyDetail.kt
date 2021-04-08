package pl.agh.robert_kraut_cz_8.model

data class CurrencyDetail(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<Rate>
) {
    val todaysRate: Rate
        get() = rates.last()
    val yesterdaysRate: Rate
        get() = rates.takeLast(2).first()
}

data class Rate(
    val effectiveDate: String,
    val mid: Double,
)