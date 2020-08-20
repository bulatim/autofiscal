package bulat.ru.autofiscalization.di.component

import android.content.SharedPreferences
import bulat.ru.autofiscalization.di.module.ApplicationModule
import bulat.ru.autofiscalization.di.module.NetworkModule
import bulat.ru.autofiscalization.di.module.ServiceModule
import bulat.ru.autofiscalization.di.module.ViewModelModule
import bulat.ru.autofiscalization.di.scope.AppScope
import bulat.ru.autofiscalization.service.FiscalService
import bulat.ru.autofiscalization.ui.MainActivity
import bulat.ru.autofiscalization.ui.main.MainFragment
import bulat.ru.autofiscalization.ui.settings.SettingsFragment
import bulat.ru.autofiscalization.ui.user.CreateUpdateUserFragment
import bulat.ru.autofiscalization.ui.user.UserListFragment
import dagger.Component
import javax.inject.Singleton

@AppScope
@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, ServiceModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: UserListFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: CreateUpdateUserFragment)
    fun inject(service: FiscalService)
    fun provideSharedPreferences(): SharedPreferences
}