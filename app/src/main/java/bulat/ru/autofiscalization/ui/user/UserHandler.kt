package bulat.ru.autofiscalization.ui.user

import android.view.View
import bulat.ru.autofiscalization.model.entities.User

interface UserHandler {
    fun onClick(user: User)
    fun onLongClick(view: View, user: User)
}