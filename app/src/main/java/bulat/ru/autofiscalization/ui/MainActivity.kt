package bulat.ru.autofiscalization.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.ui.main.MainFragment
import bulat.ru.autofiscalization.ui.settings.SettingsFragment
import bulat.ru.autofiscalization.ui.user.UserListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsFragment = SettingsFragment()
        val mainFragment = MainFragment()
        val usersFragment = UserListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_container, settingsFragment, SettingsFragment::class.java.simpleName)
            .hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_container, usersFragment, UserListFragment::class.java.simpleName)
            .hide(usersFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_container, mainFragment, MainFragment::class.java.simpleName).commit()
        var active: Fragment = mainFragment

        bottom_nav.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.main -> {
                    supportFragmentManager.beginTransaction().hide(active).show(mainFragment)
                        .commit()
                    active = mainFragment
                    mainFragment.viewModel.getCurrentCashier()
                    true
                }
                R.id.users -> {
                    supportFragmentManager.beginTransaction().hide(active).show(usersFragment)
                        .commit()
                    active = usersFragment
                    true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction().hide(active).show(settingsFragment).commit()
                    active = settingsFragment
                    true
                }
                else -> false
            }
        }
    }
}
