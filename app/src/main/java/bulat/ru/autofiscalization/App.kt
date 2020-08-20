package bulat.ru.autofiscalization

import android.content.*
import android.os.IBinder
import androidx.multidex.MultiDexApplication
import bulat.ru.autofiscalization.di.component.AppComponent
import bulat.ru.autofiscalization.di.component.DaggerAppComponent
import bulat.ru.autofiscalization.di.module.ApplicationModule
import bulat.ru.autofiscalization.di.module.ContextModule
import bulat.ru.autofiscalization.receiver.ServiceStatusBroadcastReceiver
import bulat.ru.autofiscalization.service.FiscalService
import bulat.ru.autofiscalization.utils.SERVICE_STATUS_BROADCAST_RECEIVER
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class App : MultiDexApplication(), CoroutineScope {
    lateinit var component: AppComponent
    lateinit var contextModule: ContextModule
    var bound = false
    private val fiscalIntentService by lazy { Intent(this, FiscalService::class.java) }
    private var coroutineJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private lateinit var serviceStatusBroadcastReceiver: ServiceStatusBroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        contextModule = ContextModule(this)
        component = DaggerAppComponent.builder()
            .contextModule(contextModule)
            .build()
        /*if (component.provideSharedPreferences().getBoolean("exchange_status", false))
            startService()*/
        serviceStatusBroadcastReceiver = ServiceStatusBroadcastReceiver {
            bound = it
        }
        registerReceiver(serviceStatusBroadcastReceiver, IntentFilter(SERVICE_STATUS_BROADCAST_RECEIVER))
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(serviceStatusBroadcastReceiver)
    }

    fun startService() {
        launch {
            while (bound)
                delay(100)
            startService(fiscalIntentService)
        }
    }

    fun stopService() {
        launch {
            if (bound)
                stopService(fiscalIntentService)
        }
    }
}