package bulat.ru.autofiscalization.di.module

import bulat.ru.autofiscalization.service.FiscalService
import dagger.Module
import dagger.Provides

@Module
class ServiceModule constructor(service: FiscalService) {
    private var fiscalService = service

    @Provides
    fun provideFiscalService() = fiscalService
}