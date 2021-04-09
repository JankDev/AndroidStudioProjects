package pl.agh.robert_kraut_cz_8

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pl.agh.robert_kraut_cz_8.view.CurrencyViewListActivity
import pl.agh.robert_kraut_cz_8.view.GoldViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onViewCurrencyButtonClick(view: View) {
        val intent = Intent(this, CurrencyViewListActivity::class.java)
        startActivity(intent)
    }

    fun onViewGoldButtonClick(view: View) {
        val intent = Intent(this, GoldViewActivity::class.java)
        startActivity(intent)
    }

    fun onExitButtonClick(view: View) {
        finishAffinity()
    }
}