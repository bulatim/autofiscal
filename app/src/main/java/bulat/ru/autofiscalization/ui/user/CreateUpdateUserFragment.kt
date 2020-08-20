package bulat.ru.autofiscalization.ui.user

import androidx.lifecycle.Observer
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.base.BaseFragment
import bulat.ru.autofiscalization.databinding.CreateUpdateUserFragmentBinding
import bulat.ru.autofiscalization.model.entities.User
import kotlinx.android.synthetic.main.create_update_user_fragment.*
import javax.inject.Inject

class CreateUpdateUserFragment : BaseFragment() {
    private var userId = 0L
    private var isEdit = false
    private var isFirstStart = false
    lateinit var viewModel: CreateUpdateUserViewModel
    lateinit var binding: CreateUpdateUserFragmentBinding
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun newInstance(userId: Long, isFirstStart: Boolean): CreateUpdateUserFragment {
            val fragment = CreateUpdateUserFragment()
            if (userId != 0L) {
                fragment.userId = userId
                fragment.isEdit = true
            }
            fragment.isFirstStart = isFirstStart
            return fragment
        }
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
            CreateUpdateUserFragmentBinding.inflate(
                inflater,
                container,
                false
            )
        binding.lifecycleOwner = this
        if (!isFirstStart)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_update_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.remove)!!.isVisible = isEdit
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                lastName.setText(lastName.text.toString().trim())
                firstName.setText(firstName.text.toString().trim())
                middleName.setText(middleName.text.toString().trim())
                position.setText(position.text.toString().trim())

                if (lastName.text.toString().isNotEmpty() && firstName.text.toString().isNotEmpty()) {
                    val user = User().apply {
                        if (isEdit)
                            this.id = userId
                        this.lastname = lastName.text.toString()
                        this.firstname = firstName.text.toString()
                        this.middlename = middleName.text.toString()
                        this.positionname = position.text.toString()
                    }

                    viewModel.saveOrUpdateUser(user)
                }
                return true
            }
            R.id.remove -> {
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
                return true
            }
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateUpdateUserViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.saveOrUpdateStatusCallback = {
            activity?.finish()
        }
        if (isEdit)
            viewModel.getUserById(userId)
    }
}