package bulat.ru.autofiscalization.providers

interface InstanceProvider<T> {
    fun get(): T?

    fun set(`object`: T?): InstanceProvider<T>
}