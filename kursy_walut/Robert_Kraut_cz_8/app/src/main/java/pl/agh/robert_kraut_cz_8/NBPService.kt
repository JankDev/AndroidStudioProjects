package pl.agh.robert_kraut_cz_8

import android.content.Intent
import androidx.core.app.JobIntentService

class NBPService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        println("Handle some work")
    }
}