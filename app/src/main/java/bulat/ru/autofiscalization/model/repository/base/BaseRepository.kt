package bulat.ru.autofiscalization.model.repository.base

import kotlinx.coroutines.Deferred

interface BaseRepository<T> {
    suspend fun getById(id: Long): Deferred<T?>
    suspend fun insertAll(records: List<T>): Deferred<Unit>
    suspend fun update(record: T): Deferred<Int>
    suspend fun insert(record: T): Deferred<Long>
}