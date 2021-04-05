package pl.agh.robert_kraut_cz_8.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.model.CurrencyRate

/**
 * A fragment representing a single CurrencyView detail screen.
 * This fragment is either contained in a [CurrencyViewListActivity]
 * in two-pane mode (on tablets) or a [CurrencyViewDetailActivity]
 * on handsets.
 */
class CurrencyViewDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: CurrencyRate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
          /*      item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
                    item?.content*/
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.currencyview_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.findViewById<TextView>(R.id.currencyview_detail).text = it.mid.toString()
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}