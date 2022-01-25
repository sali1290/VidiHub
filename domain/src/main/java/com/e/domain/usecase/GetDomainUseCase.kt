package com.e.domain.usecase

import com.e.domain.repo.GetDomainRepo
import javax.inject.Inject

class GetDomainUseCase @Inject constructor(private val getDomainRepo: GetDomainRepo) {

    suspend fun execute() = getDomainRepo.getDomainInfo()

}