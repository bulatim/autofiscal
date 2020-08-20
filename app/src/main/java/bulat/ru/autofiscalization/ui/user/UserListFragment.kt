package bulat.ru.autofiscalization.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bulat.ru.autofiscalization.base.BaseFragment
import bulat.ru.autofiscalization.databinding.UserFragmentBinding
import kotlinx.android.synthetic.main.user_fragment.*

class UserListFragment : BaseFragment() {
    lateinit var viewModel: UserListViewModel
    lateinit var binding: UserFragmentBinding

    companion object {
        const val ADD_USER_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            UserFragmentBinding.inflate(
                inflater,
                container,
                false
            )
        binding.userList.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addUser.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivityForResult(intent, ADD_USER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_USER_REQUEST_CODE) {
            viewModel.updateCurrentCashier()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserListViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.recyclerViewUpdateCallback = {
            binding.userList.adapter = viewModel.userListAdapter
        }
        viewModel.userDeleteCallback = { userId ->
            val alertDialog = context?.let {
                AlertDialog.Builder(it).create()
            }
            alertDialog?.apply {
                setTitle("Удалить пользователя?")
                setButton(AlertDialog.BUTTON_POSITIVE, "Да") { _, _ ->
                    viewModel.removeUser(userId)
                }
                setButton(AlertDialog.BUTTON_NEGATIVE, "Нет") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            alertDialog?.show()
        }
        viewModel.userClickCallback = {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("userId", it)
            startActivityForResult(intent, ADD_USER_REQUEST_CODE)
        }
        viewModel.updateCurrentCashier()
        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            viewModel.userListAdapter.updateUserList(it)
        })
    }
}