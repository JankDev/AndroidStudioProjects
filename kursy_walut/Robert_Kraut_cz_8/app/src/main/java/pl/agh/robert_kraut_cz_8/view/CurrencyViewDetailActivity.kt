package pl.agh.robert_kraut_cz_8.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import pl.agh.robert_kraut_cz_8.R

/**
 * An activity representing a single CurrencyView detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [CurrencyViewListActivity].
 */
class CurrencyViewDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencyview_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = CurrencyViewDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        CurrencyViewDetailFragment.ARG_ITEM_CODE,
                        intent.getStringExtra(CurrencyViewDetailFragment.ARG_ITEM_CODE)
                    )
                    putString(
                        CurrencyViewDetailFragment.ARG_ITEM_TABLE,
                        intent.getStringExtra(CurrencyViewDetailFragment.ARG_ITEM_TABLE)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.currencyview_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, CurrencyViewListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}