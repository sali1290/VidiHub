package com.e.domain.repo

interface GetDomainRepo {

    suspend fun getAllDomains(): MutableList<String>

    suspend fun getDomainInfo(): String

}