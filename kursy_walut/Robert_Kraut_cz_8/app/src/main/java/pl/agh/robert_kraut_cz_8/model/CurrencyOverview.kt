package pl.agh.robert_kraut_cz_8.model

data class CurrencyOverview(
    val currency: String,
    val code: String,
    val mid: Double,
    val table: String? = null
) {
    fun withTable(table: String) = copy(table = table)
}
