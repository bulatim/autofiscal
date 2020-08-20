package bulat.ru.autofiscalization.di.module

import bulat.ru.autofiscalization.di.scope.AppScope
import bulat.ru.autofiscalization.network.SaluteCashApi
import bulat.ru.autofiscalization.utils.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module(includes = [ContextModule::class])
class NetworkModule {
    @AppScope
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @AppScope
    @Provides
    internal fun providePostApi(retrofit: Retrofit): SaluteCashApi {
        return retrofit.create(SaluteCashApi::class.java)
    }

    @AppScope
    @Provides
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}