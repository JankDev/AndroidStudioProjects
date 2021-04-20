package pl.agh.coronatracker.view_model

class CoronaSummaryViewModel(
    val regions: List<CoronaRegionSummaryViewModel>
)
class CoronaRegionSummaryViewModel(
    val icon: Int,
    val name: String,
    val newConfirmed: Int
)