package pl.agh.robert_kraut_cz_8.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.domain.CurrencyService
import pl.agh.robert_kraut_cz_8.model.CurrencyDetail

/**
 * A fragment representing a single CurrencyView detail screen.
 * This fragment is either contained in a [CurrencyViewListActivity]
 * in two-pane mode (on tablets) or a [CurrencyViewDetailActivity]
 * on handsets.
 */
class CurrencyViewDetailFragment : Fragment() {
    private val currencyService = CurrencyService()
    private var item: CurrencyDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking<Unit> {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_CODE) && it.containsKey(ARG_ITEM_TABLE)) {
                val currencyCode = it.getString(ARG_ITEM_CODE)!!
                val currencyTable = it.getString(ARG_ITEM_TABLE)!!
                item = withContext(Dispatchers.Default) {
                    currencyService.getCurrencyDetails(
                        currencyCode,
                        currencyTable
                    )
                }
                activity?.findViewById<Toolbar>(R.id.currency_detail_toolbar)?.title =
                    item?.code
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.currencyview_detail, container, false)

        item?.let {
            rootView.findViewById<TextView>(R.id.todaysRate).text = it.todaysRate.mid.toString()
            rootView.findViewById<TextView>(R.id.yesterdaysRate).text = it.yesterdaysRate.mid.toString()

            val last7Rates = it.rates.takeLast(7)
            val last7Chart = rootView.findViewById<AnyChartView>(R.id.last7Chart)
            APIlib.getInstance().setActiveAnyChartView(last7Chart)

            val last7LineChart = AnyChart.line()
            last7LineChart.data(last7Rates.map { rate ->
                ValueDataEntry(
                    rate.effectiveDate,
                    rate.mid
                )
            })
            last7LineChart.title(getString(R.string.last7_chart_title))
            last7Chart.setChart(last7LineChart)

            val last30Chart = rootView.findViewById<AnyChartView>(R.id.last30Chart)
            APIlib.getInstance().setActiveAnyChartView(last30Chart)
            val last30LineChart = AnyChart.line()

            last30LineChart.data(it.rates.map { rate ->
                ValueDataEntry(
                    rate.effectiveDate,
                    rate.mid
                )
            })
            last30LineChart.title(getString(R.string.last30_chart_title))
            last30Chart.setChart(last30LineChart)
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_CODE = "item_code"
        const val ARG_ITEM_TABLE = "item_table"
    }
}