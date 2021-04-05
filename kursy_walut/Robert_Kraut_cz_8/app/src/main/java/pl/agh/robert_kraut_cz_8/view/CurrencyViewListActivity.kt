package pl.agh.robert_kraut_cz_8.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.domain.CurrencyService
import pl.agh.robert_kraut_cz_8.model.CurrencyRate

class CurrencyViewListActivity : AppCompatActivity() {
    private val currencyService = CurrencyService()
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencyview_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.currencyview_detail_container) != null) {
            twoPane = true
        }

        val rates = async { currencyService.getCurrencyListings() }
        setupRecyclerView(findViewById(R.id.currencyview_list), rates.await())
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<CurrencyRate>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, data, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: CurrencyViewListActivity,
        private val values: List<CurrencyRate>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as CurrencyRate
            if (twoPane) {
                val fragment = CurrencyViewDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(CurrencyViewDetailFragment.ARG_ITEM_ID, item.code)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.currencyview_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, CurrencyViewDetailActivity::class.java).apply {
                    putExtra(CurrencyViewDetailFragment.ARG_ITEM_ID, item.code)
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

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}