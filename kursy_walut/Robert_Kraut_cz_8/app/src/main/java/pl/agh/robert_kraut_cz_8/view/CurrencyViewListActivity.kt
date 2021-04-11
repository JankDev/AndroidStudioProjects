package pl.agh.robert_kraut_cz_8.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.SelectClause1
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.domain.CountryFlagRetriever
import pl.agh.robert_kraut_cz_8.domain.CurrencyService
import pl.agh.robert_kraut_cz_8.model.CurrencyOverview
import java.lang.RuntimeException

class CurrencyViewListActivity : AppCompatActivity() {
    private val currencyService = CurrencyService()
    private var twoPane: Boolean = false

    /**
     * Experiment with kotlin's coroutines
     */
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencyview_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.currencyview_detail_container) != null) {
            twoPane = true
        }

        supervisorScope {
            val job = async(start = CoroutineStart.LAZY) { currencyService.getCurrencyListings() }
            val progressBar = ProgressBar(this@CurrencyViewListActivity)
            progressBar.visibility = View.VISIBLE
            val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
            val ratesList = findViewById<RecyclerView>(R.id.currencyview_list)
            replaceView(ratesList, progressBar, frameLayout)

            try {
                job.await()
                replaceView(progressBar, ratesList, frameLayout)
                setupRecyclerView(ratesList, job.getCompleted())
            } catch (ex: RuntimeException) {
                val errorText = TextView(this@CurrencyViewListActivity)
                errorText.text = job.getCompletionExceptionOrNull()?.message
                replaceView(progressBar, errorText, frameLayout)
            }
        }
    }

    private fun replaceView(oldView: View, newView: View, container: ViewGroup) {
        container.removeView(oldView)
        container.addView(newView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<CurrencyOverview>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, data, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: CurrencyViewListActivity,
        private val values: List<CurrencyOverview>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as CurrencyOverview
            if (twoPane) {
                val fragment = CurrencyViewDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(CurrencyViewDetailFragment.ARG_ITEM_CODE, item.code)
                        putString(CurrencyViewDetailFragment.ARG_ITEM_TABLE, item.table)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.currencyview_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, CurrencyViewDetailActivity::class.java).apply {
                    putExtra(CurrencyViewDetailFragment.ARG_ITEM_CODE, item.code)
                    putExtra(CurrencyViewDetailFragment.ARG_ITEM_TABLE, item.table)
                }
                v.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.currencyview_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.code
            holder.contentView.text = item.mid.toString()
            holder.countryFlagView.setImageResource(CountryFlagRetriever.getFlagForCode(item.code))
            holder.priceDirection.setImageResource(if (item.up == true) R.drawable.arrow_green else R.drawable.arrow_red)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val countryFlagView: ImageView = view.findViewById(R.id.countryFlag)
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
            val priceDirection: ImageView = view.findViewById(R.id.priceDirection)
        }
    }
}