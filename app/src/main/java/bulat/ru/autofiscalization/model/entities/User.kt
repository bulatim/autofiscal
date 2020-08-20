package bulat.ru.autofiscalization.model.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
class User {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var date: Date? = null
    var lastname = ""
    var firstname = ""
    var middlename = ""
    var positionname = ""
    @Ignore
    fun getSurnameWithInitial() = "$lastname ${if (firstname.isNotEmpty()) firstname.substring(0,1) + "." else ""}" +
                    " ${if (middlename.isNotEmpty()) middlename.substring(0,1) + "." else ""}"

}