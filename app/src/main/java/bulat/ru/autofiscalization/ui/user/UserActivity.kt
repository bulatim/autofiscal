package bulat.ru.autofiscalization.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import bulat.ru.autofiscalization.R

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_container)

        val userId = intent.getLongExtra("userId", 0)
        val isFirstStart = intent.getBooleanExtra("isFirstStart", false)
        openFragment(CreateUpdateUserFragment.newInstance(userId, isFirstStart))
    }

    fun openFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .commit()
    }
}
