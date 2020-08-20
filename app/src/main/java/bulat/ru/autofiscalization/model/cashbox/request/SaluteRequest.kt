package bulat.ru.autofiscalization.model.cashbox.request

class SaluteRequest {
    private val id = 1
    private val jsonrpc = "2.0"
    var params: Params? = null
    var method: Method? = null

    enum class Method {
        createOrder, printHtml
    }
}