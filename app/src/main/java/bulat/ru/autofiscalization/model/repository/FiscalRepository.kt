package bulat.ru.autofiscalization.model.repository

import bulat.ru.autofiscalization.model.dao.FiscalDao
import bulat.ru.autofiscalization.model.entities.Fiscal
import bulat.ru.autofiscalization.model.repository.base.BaseRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiscalRepository @Inject
internal constructor(private val fiscalDao: FiscalDao): BaseRepository<Fiscal> {

    override suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        async {
            fiscalDao.getById(id)
        }
    }

    override suspend fun insertAll(records: List<Fiscal>) = withContext(Dispatchers.IO) {
        async {
            fiscalDao.insertAll(records)
        }
    }

    suspend fun getNotFiscalized() = withContext(Dispatchers.IO) {
        async {
            fiscalDao.notFiscalized
        }
    }

    override suspend fun update(record: Fiscal) = withContext(Dispatchers.IO) {
        async {
            fiscalDao.update(record)
        }
    }

    override suspend fun insert(record: Fiscal) = withContext(Dispatchers.IO) {
        async {
            fiscalDao.insert(record)
        }
    }
}