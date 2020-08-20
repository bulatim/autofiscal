package bulat.ru.autofiscalization.ui.user

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.model.repository.UserRepository
import bulat.ru.autofiscalization.providers.InstanceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val userProvider: InstanceProvider<User>
) : ViewModel() {
    lateinit var recyclerViewUpdateCallback: () -> Unit
    lateinit var userDeleteCallback: (id: Long) -> Unit
    lateinit var userClickCallback: (id: Long) -> Unit

    private val userHandler = object : UserHandler {
        override fun onClick(user: User) {
            userClickCallback.invoke(user.id!!)
        }

        override fun onLongClick(view: View, user: User) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.currentCashier -> {
                        user.id?.let { idUser ->
                            sharedPreferences.edit().putLong("currentCashierId", idUser).apply()
                            updateCurrentCashier()
                        }
                        true
                    }
                    R.id.remove -> {
                        user.id?.let { idUser ->
                            userDeleteCallback.invoke(idUser)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.user_popup_menu)
            popupMenu.show()
        }
    }
    val userListAdapter: UserListAdapter = UserListAdapter(userHandler)

    fun getUsers(): LiveData<List<User>> {
        return userRepository.getAll()
    }

    fun removeUser(userId: Long) {
        viewModelScope.launch {
            userRepository.deleteById(userId).await()
            updateCurrentCashier()
        }
    }

    fun updateCurrentCashier() {
        val currentCashierId = sharedPreferences.getLong("currentCashierId", 0)
        viewModelScope.launch {
            val user =
                if (currentCashierId == 0L) userRepository.getFirstUser().await() else userRepository.getById(
                    currentCashierId
                ).await()
            userProvider.set(user)
            user?.id?.let { id ->
                userListAdapter.currentCashierId = id
            }
            recyclerViewUpdateCallback.invoke()
        }
    }
}