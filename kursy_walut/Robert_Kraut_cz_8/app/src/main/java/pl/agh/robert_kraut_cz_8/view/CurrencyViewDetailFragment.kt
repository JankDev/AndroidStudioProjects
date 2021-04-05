package pl.agh.robert_kraut_cz_8.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
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
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
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
            rootView.findViewById<TextView>(R.id.currencyview_detail).text = it.code
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_CODE = "item_code"
        const val ARG_ITEM_TABLE = "item_table"
    }
}