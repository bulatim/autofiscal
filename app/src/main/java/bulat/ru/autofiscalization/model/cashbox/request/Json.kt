package bulat.ru.autofiscalization.model.cashbox.request

class Json(
    var cashier: String,
    var taxationSystem: TaxationSystem,
    var orderProductList: List<OrderProduct>
) {
    var moneySum: Int = 0
    var cashlessSum: Long = 0
    var paymentType = "INCOME"
    var tagFiscalDocumentReport = "ORDER_REPORT"
    var additionalUserDetail: AdditionalUserDetail? = null

    enum class TaxationSystem {
        OSN
    }
}