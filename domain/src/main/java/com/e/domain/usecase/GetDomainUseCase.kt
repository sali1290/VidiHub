package com.e.domain.usecase

import com.e.domain.repo.DomainRepo
import javax.inject.Inject

class GetDomainUseCase @Inject constructor(private val domainRepo: DomainRepo) {

    suspend fun execute() = domainRepo.getDomainInfo()

}