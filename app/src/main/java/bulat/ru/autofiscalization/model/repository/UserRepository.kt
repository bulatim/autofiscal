package bulat.ru.autofiscalization.model.repository

import bulat.ru.autofiscalization.model.dao.UserDao
import bulat.ru.autofiscalization.model.entities.User
import bulat.ru.autofiscalization.model.repository.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject
internal constructor(private val userDao: UserDao): BaseRepository<User> {
    fun getAll() = userDao.all

    override suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        async {
            userDao.getById(id)
        }
    }

    override suspend fun insertAll(records: List<User>) = withContext(Dispatchers.IO) {
        async {
            userDao.insertAll(records)
        }
    }

    override suspend fun update(record: User) = withContext(Dispatchers.IO) {
        async {
            userDao.update(record)
        }
    }

    override suspend fun insert(record: User) = withContext(Dispatchers.IO) {
        async {
            userDao.insert(record)
        }
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        async {
            userDao.deleteById(id)
        }
    }

    suspend fun getFirstUser() = withContext(Dispatchers.IO) {
        async {
            userDao.getFirstUser()
        }
    }
}