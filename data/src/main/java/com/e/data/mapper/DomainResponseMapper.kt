package com.e.data.mapper

import com.e.data.model.DomainResponse
import com.e.domain.model.DomainResponseModel
import javax.inject.Inject

class DomainResponseMapper @Inject constructor() {

    fun toMapper(domainResponse: DomainResponse): DomainResponseModel {
        return DomainResponseModel(
            domainResponse.domainId ?: "",
            domainResponse.domain ?: "",
            domainResponse.hostname ?: "",
            domainResponse.country ?: "",
            domainResponse.comments ?: "",
            domainResponse.status ?: "",
            domainResponse.domainLogo ?: "",
        )
    }


}