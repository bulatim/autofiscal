package bulat.ru.autofiscalization.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import bulat.ru.autofiscalization.App
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.providers.InstanceProvider
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    lateinit var userProvider: InstanceProvider<User>
    @field:[Inject Named("exchangePeriod")]
    lateinit var provideExchangePeriod: InstanceProvider<Int>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            "exchange_status" -> {
                activity?.let { fragmentActivity ->
                    val isExchange = sharedPreferences.getBoolean(key, false)
                    if (userProvider.get() != null) {
                        if (isExchange)
                            (fragmentActivity.application as App).startService()
                        else {
                            Toast.makeText(context, "Пожалуйста подождите", Toast.LENGTH_LONG).show()
                            (fragmentActivity.application as App).stopService()
                        }
                    } else if (isExchange) {
                        sharedPreferences.edit().putBoolean(key, false).apply()
                        Toast.makeText(context, "Создайте пользователя", Toast.LENGTH_LONG).show()
                        setPreferencesFromResource(R.xml.preferences, null)
                    }
                }
            }
            "exchange_period" -> {
                val exchangePeriod = sharedPreferences.getString(key, "600")
                try {
                    provideExchangePeriod.set(exchangePeriod?.toInt() ?: 600)
                } catch (e: Exception) {
                    provideExchangePeriod.set(600)
                }
                (requireActivity().application as App).stopService()
                (requireActivity().application as App).startService()
            }
        }
    }
}