package bulat.ru.autofiscalization.exchange

import android.content.Context
import android.content.SharedPreferences
import bulat.ru.autofiscalization.exchange.base.IExchangeFileProvider
import javax.inject.Inject

class ExchangeFileFactory @Inject constructor(private val mContext: Context,
                                              private val sharedPreferences: SharedPreferences) {
    var type = ExchangeFileType.TEST

    fun createExchangeFileProvider(): IExchangeFileProvider {
        return when (type) {
            ExchangeFileType.TEST ->
                TestExchangeFileProvider().apply {
                    this.context = mContext
                }
            else -> FtpExchangeFileProvider().apply {
                hostname = sharedPreferences.getString("hostname", "") ?: ""
                username = sharedPreferences.getString("username", "") ?: ""
                password = sharedPreferences.getString("password", "") ?: ""
                if (hostname == "")
                    throw Throwable("Не указан адрес FTP")
            }
        }
    }

    enum class ExchangeFileType {
        FTP, TEST
    }
}