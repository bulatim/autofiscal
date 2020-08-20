package bulat.ru.autofiscalization.exchange.base

import java.io.File

interface IExchangeFile {
    fun getExchangeFile(): File?
}