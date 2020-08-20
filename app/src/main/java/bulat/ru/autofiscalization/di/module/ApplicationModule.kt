package bulat.ru.autofiscalization.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import bulat.ru.autofiscalization.di.scope.AppScope
import bulat.ru.autofiscalization.model.database.AppDatabase
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.providers.InstanceProvider
import bulat.ru.autofiscalization.providers.impl.InstanceProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class ApplicationModule {

    @AppScope
    @Provides
    internal fun provideDatabase(context: Context): AppDatabase {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "application_database")
            .build()
        return db
    }

    @AppScope
    @Provides
    internal fun provideFiscalDao(database: AppDatabase) = database.fiscalDao()

    @AppScope
    @Provides
    internal fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    internal fun provideUser(): InstanceProvider<User> = InstanceProviderImpl()

    @Provides
    @Singleton
    @Named("exchangePeriod")
    internal fun provideExchangePeriod(): InstanceProvider<Int> = InstanceProviderImpl()

    @AppScope
    @Provides
    internal fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("${context.packageName}_preferences",
            Context.MODE_PRIVATE
        )
    }
}