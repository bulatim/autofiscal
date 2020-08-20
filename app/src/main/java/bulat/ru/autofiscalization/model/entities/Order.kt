package bulat.ru.autofiscalization.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Order {
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(index = true)
    var id: Long = 1
    var date: Date? = null
    var cashier: String? = null
    var cashlessSum: Long = 0
    var moneySum: Long = 0
    var fisDrvNumber: String? = null
    var totalSum: Long = 0
    var placePayment: String? = null
    var numberShift: Int? = null
    var paymentType: String? = null
    var numberOrder: Int? = null
    var fisDocNumber: Int? = null
    var fisDocSignature: Long? = null
    var printStatusCode: Int? = null
}