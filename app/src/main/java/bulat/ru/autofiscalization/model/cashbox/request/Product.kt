package bulat.ru.autofiscalization.model.cashbox.request

data class Product(val name: String,
                   val price: Long,
                   val taxTypeId: Int)