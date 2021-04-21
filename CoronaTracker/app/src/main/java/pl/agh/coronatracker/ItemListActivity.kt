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
import android.widget.ImageView
import android.widget.TextView
import com.blongho.country_data.World
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.domain.CoronaService

import pl.agh.coronatracker.dummy.DummyContent
import pl.agh.coronatracker.view_model.CoronaRegionSummaryViewModel
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel

class ItemListActivity : AppCompatActivity() {
    private var twoPane: Boolean = false
    private lateinit var coronaSummary: CoronaSummaryViewModel
    private lateinit var coronaService: CoronaService

    @ExperimentalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        World.init(applicationContext)
        coronaService = CoronaService(this@ItemListActivity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }

        supervisorScope {
            try {
                coronaSummary = withContext(Dispatchers.IO) {
                    coronaService.getCoronaSummary()
                }
                setupRecyclerView(findViewById(R.id.item_list), coronaSummary.regions)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        items: List<CoronaRegionSummaryViewModel>
    ) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, items)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: List<CoronaRegionSummaryViewModel>,
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as CoronaRegionSummaryViewModel
            val intent = Intent(parentActivity, ItemDetailActivity::class.java).apply {
                putExtra("countryName", item.name)
            }
            parentActivity.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.iconView.setImageResource(item.icon)
            holder.regionView.text = item.name
            holder.newConfirmedView.text = item.newConfirmed.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val iconView: ImageView = view.findViewById(R.id.region_icon)
            val regionView: TextView = view.findViewById(R.id.region)
            val newConfirmedView: TextView = view.findViewById(R.id.new_confirmed)
        }
    }
}