package pl.agh.robert_kraut_cz_8.view

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.*
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.domain.CurrencyService
import pl.agh.robert_kraut_cz_8.model.CurrencyOverview

class ConverterView : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val currencyService = CurrencyService()
    private var currencies: List<CurrencyOverview> = emptyList()
    private lateinit var selectedCurrency: CurrencyOverview

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter_view)

        val converterLayout = findViewById<ViewGroup>(R.id.converterLayout)
        val job = async(start = CoroutineStart.LAZY) { currencyService.getCurrencyListings() }
        supervisorScope {
            try {
                currencies = job.await()
                val spinner = findViewById<Spinner>(R.id.currencySpinner)
                val spinnerArrayAdapter = ArrayAdapter(
                    this@ConverterView,
                    android.R.layout.simple_spinner_item,
                    currencies.map { it.code })
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.adapter = spinnerArrayAdapter
                spinner.onItemSelectedListener = this@ConverterView

                val plnInput = findViewById<EditText>(R.id.convertPln)
                val otherCurrencyInput = findViewById<EditText>(R.id.convertOther)
                plnInput.addTextChangedListener {
                    if (currentFocus == plnInput && it.validNumber()) {
                        otherCurrencyInput.setText(
                            if (it?.isNotBlank() == true) (it.toString()
                                .toDouble() / selectedCurrency.mid)
                                .toString()
                            else ""
                        )
                    }
                }
                otherCurrencyInput.addTextChangedListener {
                    if (currentFocus == otherCurrencyInput && it.validNumber()) {
                        plnInput.setText(
                            if (it?.isNotBlank() == true) (it.toString()
                                .toDouble() * selectedCurrency.mid)
                                .toString()
                            else ""
                        )
                    }
                }
            } catch (ex: RuntimeException) {
                val errorText = TextView(this@ConverterView)
                errorText.text = job.getCompletionExceptionOrNull()?.message
                converterLayout.removeAllViewsInLayout()
                converterLayout.addView(errorText)
            }
        }
        return@runBlocking
    }

    private fun Editable?.validNumber() =
        if (this != null) toString().toDoubleOrNull() != null else false

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCurrency = currencies[position]
        val plnInput = findViewById<EditText>(R.id.convertPln)
        val otherCurrencyInput = findViewById<EditText>(R.id.convertOther)
        plnInput.setText("")
        otherCurrencyInput.setText("")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedCurrency = currencies.first()
    }


}