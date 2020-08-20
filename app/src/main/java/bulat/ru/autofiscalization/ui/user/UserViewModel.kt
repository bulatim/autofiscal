package bulat.ru.autofiscalization.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bulat.ru.autofiscalization.model.entities.User

class UserViewModel: ViewModel() {
    val fullName = MutableLiveData<String>()
    val positionName = MutableLiveData<String>()

    fun bind(user: User) {
        fullName.value = user.getSurnameWithInitial()
        positionName.value = user.positionname
    }
}