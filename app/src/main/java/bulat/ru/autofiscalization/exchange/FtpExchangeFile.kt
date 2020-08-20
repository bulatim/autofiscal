package bulat.ru.autofiscalization.exchange

import bulat.ru.autofiscalization.exchange.base.IExchangeFile
import org.apache.commons.net.ftp.FTPClient
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class FtpExchangeFile: IExchangeFile {
    override fun getExchangeFile(): File? {
        val ftpClient = FTPClient()
        val hostname = ""
        ftpClient.connect(hostname)
        val username = ""
        val password = ""
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