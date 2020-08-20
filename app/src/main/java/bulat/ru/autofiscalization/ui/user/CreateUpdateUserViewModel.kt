package bulat.ru.autofiscalization.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.model.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateUpdateUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    lateinit var saveOrUpdateStatusCallback: () -> Unit

    val lastNameText: MutableLiveData<String> = MutableLiveData()
    val firstNameText: MutableLiveData<String> = MutableLiveData()
    val middleNameText: MutableLiveData<String> = MutableLiveData()
    val positionText: MutableLiveData<String> = MutableLiveData()

    fun saveOrUpdateUser(user: User) {
        viewModelScope.launch {
            if (user.id != null)
                userRepository.update(user).await()
            else
                userRepository.insert(user).await()
            saveOrUpdateStatusCallback.invoke()
        }
    }

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            val user = userRepository.getById(userId).await()
            user?.let {
                lastNameText.value = it.lastname
                firstNameText.value = it.firstname
                middleNameText.value = it.middlename
                positionText.value = it.positionname
            }
        }
    }

    fun removeUser(userId: Long) {
        viewModelScope.launch {
            userRepository.deleteById(userId).await()
            saveOrUpdateStatusCallback.invoke()
        }
    }
}