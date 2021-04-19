package pl.agh.coronatracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.agh.coronatracker.model.CountryWithTotalSummaries

class CountryAdapter(var dataSet: List<CountryWithTotalSummaries>, val context: Context) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val countrNameTextView: TextView
        val flagView: ImageView
        val activeCasesView: TextView

        init {
            countrNameTextView = view.findViewById(R.id.countryNameView)
            flagView = view.findViewById(R.id.flagView)
            activeCasesView = view.findViewById(R.id.activeCasesView)
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.country_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val country = dataSet[position]
        val countryName = country.country.country
        viewHolder.countrNameTextView.text = countryName
        viewHolder.flagView.setImageResource(country.countryFlag)
        viewHolder.activeCasesView.text = country.today.active.toString()
        viewHolder.itemView.setOnClickListener { goToDetails(countryName) }
    }

    private fun goToDetails(countryName: String) {
        val intent = Intent(context, SingleCountryActivity::class.java).apply {
            putExtra("countryName", countryName)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
