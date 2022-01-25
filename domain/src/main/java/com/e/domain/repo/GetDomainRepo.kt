package com.e.domain.repo

import com.e.domain.model.DomainResponseModel

interface GetDomainRepo {

    suspend fun getAllDomains(): MutableList<String>

    suspend fun getDomainInfo(): DomainResponseModel

}