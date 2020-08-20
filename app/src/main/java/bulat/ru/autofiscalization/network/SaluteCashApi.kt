package bulat.ru.autofiscalization.network

import bulat.ru.autofiscalization.model.cashbox.response.SaluteResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SaluteCashApi {
    @FormUrlEncoded
    @POST("/")
    fun sendRequest(@Field("jsonrpc") orderRequest: String): Deferred<SaluteResponse>

    @FormUrlEncoded
    @POST("/")
    fun printCheck(@Field("jsonrpc") commandRequest: String = "{\n" +
            "\"id\":7,\n" +
            "\"jsonrpc\":\"2.0\",\n" +
            "\"method\":\"printSlipCheck\"\n" +
            "}\n", @Field("check") check: String): Deferred<Any>
}