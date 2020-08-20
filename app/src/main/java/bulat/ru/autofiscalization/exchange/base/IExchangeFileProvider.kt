package bulat.ru.autofiscalization.exchange.base

import java.io.File

interface IExchangeFileProvider {
    fun getExchangeFile(): File?
}