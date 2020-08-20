package bulat.ru.autofiscalization.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bulat.ru.autofiscalization.base.ViewModelFactory
import bulat.ru.autofiscalization.di.ViewModelKey
import bulat.ru.autofiscalization.di.scope.AppScope
import bulat.ru.autofiscalization.ui.main.MainViewModel
import bulat.ru.autofiscalization.ui.user.CreateUpdateUserViewModel
import bulat.ru.autofiscalization.ui.user.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    abstract fun bindUserListViewModel(userListViewModel: UserListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateUpdateUserViewModel::class)
    abstract fun bindCreateUpdateUserViewModel(createUpdateUserViewModel: CreateUpdateUserViewModel): ViewModel

    @AppScope
    @Binds abstract
    fun bindViewModelFactory(vmFactory: ViewModelFactory): ViewModelProvider.Factory
}