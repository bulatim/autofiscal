package bulat.ru.autofiscalization.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.databinding.ItemUserBinding
import bulat.ru.autofiscalization.model.entities.User

class UserListAdapter(private val userHandler: UserHandler) : Adapter<UserListAdapter.ViewHolder>() {
    private lateinit var userList: List<User>
    var currentCashierId = 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user, parent, false)
        binding.handler = userHandler
        return ViewHolder(binding, currentCashierId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return if (::userList.isInitialized) userList.size else 0
    }

    fun updateUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemUserBinding, private val currentCashierId: Long) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = UserViewModel()

        fun bind(user: User) {
            viewModel.bind(user)
            binding.imageView.visibility = if (user.id == currentCashierId) View.VISIBLE else View.GONE
            binding.user = user
            binding.viewModel = viewModel
        }
    }
}