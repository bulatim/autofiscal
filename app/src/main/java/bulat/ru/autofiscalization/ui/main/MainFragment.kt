package bulat.ru.autofiscalization.ui.main

import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import bulat.ru.autofiscalization.App
import bulat.ru.autofiscalization.BuildConfig
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
    @Inject
    lateinit var sharedPreferences: SharedPreferences
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
            service_status.isChecked = (fragmentActivity.application as App).bound
        }
        serviceStatusBroadcastReceiver = ServiceStatusBroadcastReceiver {
            service_status.text = if (it) "Сервис запущен" else "Сервис не запущен"
            service_status.isChecked = it
            sharedPreferences.edit().putBoolean("exchange_status", it).apply()
        }
        service_status.setOnCheckedChangeListener { _, isExchange ->
            val hostname = sharedPreferences.getString("hostname", "")
            if (hostname.isNullOrEmpty() && !BuildConfig.DEBUG) {
                showToast("В настройках необходимо указать параметры соединения")
                service_status.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (userProvider.get() != null) {
                if (isExchange) {
                    if (!(requireActivity().application as App).bound)
                        (requireActivity().application as App).startService()
                } else {
                    if ((requireActivity().application as App).bound) {
                        showToast("Пожалуйста подождите")
                        (requireActivity().application as App).stopService()
                    }
                }
            } else if (isExchange) {
                showToast("Создайте пользователя")
            }
        }
        activity?.registerReceiver(
            serviceStatusBroadcastReceiver,
            IntentFilter(SERVICE_STATUS_BROADCAST_RECEIVER)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.updateCurrentCashierName = {
            currentCashier.text =
                userProvider.get()?.getSurnameWithInitial() ?: "Не выбран пользователь"
        }
        viewModel.getCurrentCashier()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::serviceStatusBroadcastReceiver.isInitialized)
            activity?.unregisterReceiver(serviceStatusBroadcastReceiver)
    }
}