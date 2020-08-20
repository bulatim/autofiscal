package bulat.ru.autofiscalization.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bulat.ru.autofiscalization.model.converter.Converters
import bulat.ru.autofiscalization.model.dao.FiscalDao
import bulat.ru.autofiscalization.model.dao.UserDao
import bulat.ru.autofiscalization.model.entities.Fiscal
import bulat.ru.autofiscalization.model.entities.User

@Database(entities = [Fiscal::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fiscalDao(): FiscalDao
    abstract fun userDao(): UserDao
}