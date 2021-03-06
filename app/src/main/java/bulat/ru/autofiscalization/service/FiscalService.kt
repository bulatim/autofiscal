package bulat.ru.autofiscalization.service

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import android.util.Log
import bulat.ru.autofiscalization.App
import bulat.ru.autofiscalization.BuildConfig
import bulat.ru.autofiscalization.di.component.DaggerAppComponent
import bulat.ru.autofiscalization.di.module.ServiceModule
import bulat.ru.autofiscalization.exchange.ExchangeFileFactory
import bulat.ru.autofiscalization.model.cashbox.request.*
import bulat.ru.autofiscalization.model.entities.Fiscal
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.model.repository.CashboxRepository
import bulat.ru.autofiscalization.model.repository.FiscalRepository
import bulat.ru.autofiscalization.model.repository.UserRepository
import bulat.ru.autofiscalization.providers.InstanceProvider
import bulat.ru.autofiscalization.utils.SERVICE_STATUS_BROADCAST_RECEIVER
import bulat.ru.autofiscalization.utils.ddMMyyySimpleDateFormat
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class FiscalService : Service(), CoroutineScope {
    @Inject
    lateinit var cashboxRepository: CashboxRepository
    @Inject
    lateinit var fiscalRepository: FiscalRepository
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exchangeFileFactory: ExchangeFileFactory
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var userProvider: InstanceProvider<User>
    @field:[Inject Named("exchangePeriod")]
    lateinit var provideExchangePeriod: InstanceProvider<Int>
    private var coroutineJob = Job()
    private var isProcessExchange = false
    private var timer: Timer? = null
    private val intent = Intent().apply { action = SERVICE_STATUS_BROADCAST_RECEIVER }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun onCreate() {
        super.onCreate()
        (application as App).component.inject(this)
        changeStatus(true)
    }

    fun changeStatus(bound: Boolean) {
        intent.putExtra("bulat.ru.autofiscalization.STATUS", bound)
        sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("myLog", "onStartCommand")
        timer?.cancel()
        timer = Timer()
        if (provideExchangePeriod.get() == null) {
            val exchangePeriod = sharedPreferences.getString("exchange_period", "600")
            try {
                if (exchangePeriod != null)
                    provideExchangePeriod.set(exchangePeriod.toInt())
            } catch (e: Exception) {
                provideExchangePeriod.set(600)
            }
        }
        Log.d("myLog", "exchangePeriod: ${provideExchangePeriod.get()}")
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (userProvider.get() == null) {
                    launch {
                        val currentCashierId = sharedPreferences.getLong("currentCashierId", 0)
                        val user =
                            if (currentCashierId == 0L) userRepository.getFirstUser().await() else userRepository.getById(
                                currentCashierId
                            ).await()
                        user?.let {
                            userProvider.set(user)
                            startExchange()
                        }
                    }
                } else
                    startExchange()
            }
        }, 0, provideExchangePeriod.get()!! * 1000L)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return Binder()
    }

    private fun startExchange() {
        if (!isProcessExchange) {
            isProcessExchange = true
            exchangeFileFactory.type =
                if (BuildConfig.DEBUG)
                    ExchangeFileFactory.ExchangeFileType.TEST
                else
                    ExchangeFileFactory.ExchangeFileType.FTP
            exchangeFileFactory.createExchangeFile().getExchangeFile()?.let {
                parseFile(it)
                launch {
                    val notFiscalDocuments = fiscalRepository.getNotFiscalized().await()
                    if (notFiscalDocuments.isNotEmpty())
                        notFiscalDocuments.forEach { fiscalDocument ->
                            startFiscalize(fiscalDocument).await()
                        }
                    isProcessExchange = false
                }
            }
        }
    }

    private suspend fun startFiscalize(fiscal: Fiscal): Deferred<Int> {
        try {
            val saluteRequest = createRequest(fiscal)
            val response = cashboxRepository.sendRequest(saluteRequest).await()
            fiscal.fiscalized = response.error?.let {
                false
            } ?: run {
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fiscal.fiscalized = false
        }
        return fiscalRepository.update(fiscal)
    }

    private fun parseFile(file: File) {
        val contentFile = file.readText(Charset.forName("Windows-1251"))
        val fiscalItems = ArrayList<Fiscal>()
        contentFile.split("\n").forEach {
            if (it.isNotEmpty()) {
                val lineColumns = it.split(";")
                fiscalItems.add(
                    Fiscal().apply {
                        date = ddMMyyySimpleDateFormat.parse(lineColumns[0])
                        personalAccount = lineColumns[1]
                        address = lineColumns[2]
                        phone = lineColumns[3]
                        email = lineColumns[4]
                        sum = (lineColumns[5].replace(",", ".").toFloat() * 100).toLong()
                    })
            }
        }
        launch {
            fiscalRepository.insertAll(fiscalItems).await()
        }
    }

    private val orderProducts = ArrayList<OrderProduct>()

    private fun createRequest(fiscal: Fiscal): SaluteRequest {
        val productName = "Наименование продукта"
        val productPrice = fiscal.sum
        val product = Product(productName, productPrice, 1)
        val productCount = 1
        orderProducts.clear()
        orderProducts.add(OrderProduct(product, productCount))
        val json = Json(
            userProvider.get()!!.getSurnameWithInitial(),
            Json.TaxationSystem.OSN,
            orderProducts
        ).apply {
            cashlessSum = productPrice
        }
        val params = Params().apply {
            needPrint = false
            this.json = json
        }
        return SaluteRequest().apply {
            this.method = SaluteRequest.Method.createOrder
            this.params = params
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("myLog", "onDestroy")
        launch {
            while (isProcessExchange)
                delay(100)
            timer?.cancel()
            Log.d("myLog", "timer canceled")
            changeStatus(false)
        }
    }
}