package bulat.ru.autofiscalization.exchange

import android.content.Context
import bulat.ru.autofiscalization.R
import bulat.ru.autofiscalization.exchange.base.IExchangeFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class TestExchangeFile: IExchangeFile {
    lateinit var context: Context

    override fun getExchangeFile(): File? {
        val testFile = File(context.cacheDir, "testkkm.txt")
        copyStreamToFile(context.resources.openRawResource(R.raw.testkkm), testFile)
        return testFile
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }
}