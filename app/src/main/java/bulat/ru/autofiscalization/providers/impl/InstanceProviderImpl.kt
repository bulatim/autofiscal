package bulat.ru.autofiscalization.providers.impl

import bulat.ru.autofiscalization.providers.InstanceProvider
import javax.inject.Inject

class InstanceProviderImpl<T> @Inject constructor(): InstanceProvider<T> {
    private var tObj: T? = null

    override fun get(): T? {
        return tObj
    }

    override fun set(`object`: T?): InstanceProvider<T> {
        this.tObj = `object`
        return this
    }
}