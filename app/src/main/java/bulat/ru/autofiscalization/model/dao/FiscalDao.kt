package bulat.ru.autofiscalization.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bulat.ru.autofiscalization.model.entities.Fiscal

@Dao
interface FiscalDao {
    @get:Query("SELECT * FROM `Fiscal`")
    val all: LiveData<List<Fiscal>>

    @get:Query("SELECT * FROM `Fiscal` WHERE fiscalized = 0")
    val notFiscalized: List<Fiscal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: Fiscal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fiscals: List<Fiscal>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg fiscal: Fiscal): Int

    @Query("SELECT * FROM `Fiscal` WHERE id=:id")
    fun getById(id: Long): Fiscal
}