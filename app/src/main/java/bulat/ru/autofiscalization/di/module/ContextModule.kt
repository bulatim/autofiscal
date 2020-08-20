package bulat.ru.autofiscalization.di.module

import android.content.Context
import bulat.ru.autofiscalization.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule constructor(private val context: Context) {

    @AppScope
    @Provides
    internal fun provideContext(): Context = context
}