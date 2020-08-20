package bulat.ru.autofiscalization.ui.main

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import bulat.ru.autofiscalization.App
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.base.BaseFragment
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.providers.InstanceProvider
import bulat.ru.autofiscalization.receiver.ServiceStatusBroadcastReceiver
import bulat.ru.autofiscalization.utils.SERVICE_STATUS_BROADCAST_RECEIVER
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : BaseFragment() {
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var userProvider: InstanceProvider<User>
    private lateinit var serviceStatusBroadcastReceiver: ServiceStatusBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { fragmentActivity ->
            service_status.text =
                if ((fragmentActivity.application as App).bound) "Сервис запущен" else "Сервис не запущен"
        }
        serviceStatusBroadcastReceiver = ServiceStatusBroadcastReceiver {
            service_status.text = if (it) "Сервис запущен" else "Сервис не запущен"
        }
        activity?.registerReceiver(serviceStatusBroadcastReceiver, IntentFilter(SERVICE_STATUS_BROADCAST_RECEIVER))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.updateCurrentCashierName = {
            currentCashier.text = userProvider.get()?.getSurnameWithInitial() ?: "Не выбран пользователь"
        }
        viewModel.getCurrentCashier()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::serviceStatusBroadcastReceiver.isInitialized)
            activity?.unregisterReceiver(serviceStatusBroadcastReceiver)
    }
}