package bulat.ru.autofiscalization.exchange

import android.content.Context
import bulat.ru.autofiscalization.exchange.base.IExchangeFile
import javax.inject.Inject

class ExchangeFileFactory @Inject constructor(val mContext: Context) {
    var type = ExchangeFileType.TEST

    fun createExchangeFile(): IExchangeFile {
        return when (type) {
            ExchangeFileType.TEST ->
                TestExchangeFile().apply {
                    this.context = mContext
                }
            else -> FtpExchangeFile()
        }
    }

    enum class ExchangeFileType {
        FTP, TEST
    }
}