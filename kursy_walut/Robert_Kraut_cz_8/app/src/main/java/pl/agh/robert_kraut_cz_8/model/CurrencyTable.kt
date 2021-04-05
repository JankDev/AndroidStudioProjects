package pl.agh.robert_kraut_cz_8.model

data class CurrencyTable(
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<CurrencyRate>
)
