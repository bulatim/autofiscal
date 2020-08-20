package bulat.ru.autofiscalization.exchange

import bulat.ru.autofiscalization.exchange.base.IExchangeFileProvider
import org.apache.commons.net.ftp.FTPClient
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class FtpExchangeFileProvider: IExchangeFileProvider {
    lateinit var hostname: String
    lateinit var username: String
    lateinit var password: String

    override fun getExchangeFile(): File? {
        val ftpClient = FTPClient()
        ftpClient.connect(hostname)
        ftpClient.login(username, password)
        ftpClient.enterLocalPassiveMode()
        if (ftpClient.listNames().isNotEmpty()) {
            val files = ftpClient.listFiles()
            files.forEach {
                val exchangeFile = File("")
                val outputStream1 =
                    BufferedOutputStream(FileOutputStream(exchangeFile))
                ftpClient.retrieveFile(it.name, outputStream1)
                outputStream1.close()
            }
        }
        return null
    }
}