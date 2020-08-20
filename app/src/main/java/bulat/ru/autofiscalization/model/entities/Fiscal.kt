package bulat.ru.autofiscalization.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Fiscal {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var sum = 0L
    var date: Date? = null
    var personalAccount: String? = null
    var address: String? = null
    var phone: String? = null
    var email: String? = null
    var fiscalized = false
}