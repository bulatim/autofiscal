package bulat.ru.autofiscalization.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bulat.ru.autofiscalization.model.entities.Fiscal
import bulat.ru.autofiscalization.model.entities.User

@Dao
interface UserDao {
    @get:Query("SELECT * FROM `User`")
    val all: LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg user: User): Int

    @Query("SELECT * FROM `User` WHERE id=:id")
    fun getById(id: Long): User?

    @Query("DELETE FROM `User` WHERE id=:id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM `User` ORDER BY id ASC LIMIT 1")
    fun getFirstUser(): User?
}