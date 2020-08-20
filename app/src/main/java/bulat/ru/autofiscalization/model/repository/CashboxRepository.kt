package bulat.ru.autofiscalization.model.repository

import bulat.ru.autofiscalization.model.cashbox.request.SaluteRequest
import bulat.ru.autofiscalization.network.SaluteCashApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CashboxRepository @Inject
internal constructor(private val saluteCashApi: SaluteCashApi) {
    private val gson = Gson()

    suspend fun sendRequest(saluteRequest: SaluteRequest) = withContext(Dispatchers.IO) {
        saluteCashApi.sendRequest(gson.toJson(saluteRequest))
    }
}