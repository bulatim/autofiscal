package bulat.ru.autofiscalization.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.model.repository.UserRepository
import bulat.ru.autofiscalization.providers.InstanceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val userProvider: InstanceProvider<User>
) : ViewModel() {
    lateinit var updateCurrentCashierName: () -> Unit

    fun getCurrentCashier() {
        viewModelScope.launch {
            if (userProvider.get() == null) {
                val currentCashierId = sharedPreferences.getLong("currentCashierId", 0)
                val user =
                    if (currentCashierId == 0L) userRepository.getFirstUser().await() else userRepository.getById(
                        currentCashierId
                    ).await()
                userProvider.set(user)
            }
            updateCurrentCashierName.invoke()
        }
    }
}