package bulat.ru.autofiscalization.model.cashbox.response

data class SaluteResponse(val id: Int,
                          val jsonrpc: String,
                          val result: String? = null,
                          val error: Error? = null)