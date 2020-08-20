package bulat.ru.autofiscalization.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import bulat.ru.autofiscalization.utils.SERVICE_STATUS_BROADCAST_RECEIVER

class ServiceStatusBroadcastReceiver(private val callback:(status: Boolean) -> Unit): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == SERVICE_STATUS_BROADCAST_RECEIVER) {
            val status = intent.getBooleanExtra("bulat.ru.autofiscalization.STATUS", false)
            callback.invoke(status)
        }
    }
}