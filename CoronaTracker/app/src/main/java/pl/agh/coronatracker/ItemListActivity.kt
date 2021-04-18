package pl.agh.coronatracker

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.coronaApi

import pl.agh.coronatracker.dummy.DummyContent
import pl.agh.coronatracker.model.CoronaSummary
import pl.agh.coronatracker.model.CountrySummary

class ItemListActivity : AppCompatActivity() {
    private var twoPane: Boolean = false
    private lateinit var coronaSummary: CoronaSummary

    @ExperimentalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }

        supervisorScope {
            try {
                coronaSummary = withContext(Dispatchers.IO) {
                    coronaApi.getSummary()
                        .also { print(it) }
                        .takeIf { it.isSuccessful }
                        ?.body()
                        ?: throw RuntimeException("Error loading data")
                }
                setupRecyclerView(findViewById(R.id.item_list), coronaSummary.countries)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<CountrySummary>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, items, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: List<CountrySummary>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.countryView.text = item.country
            holder.newConfirmedView.text = item.newConfirmed.toString()
            holder.newDeathsView.text = item.newDeaths.toString()
            holder.totalConfirmedView.text = item.totalConfirmed.toString()
            holder.totalDeathsView.text = item.totalDeaths.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val countryView: TextView = view.findViewById(R.id.country)
            val newConfirmedView: TextView = view.findViewById(R.id.new_confirmed)
            val newDeathsView: TextView = view.findViewById(R.id.new_deaths)
            val totalConfirmedView: TextView = view.findViewById(R.id.total_confirmed)
            val totalDeathsView: TextView = view.findViewById(R.id.total_deaths)

        }
    }
}