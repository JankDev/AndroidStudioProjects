package pl.agh.robert_kraut_cz_8

import java.time.LocalDate

data class CurrencyTable(
    val table: String,
    val no: String,
    val effectiveDate: LocalDate,
    val rates: List<CurrencyRate>
)
