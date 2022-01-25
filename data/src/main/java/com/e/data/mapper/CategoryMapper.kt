package com.e.data.mapper

import com.e.data.model.CategoryResponse
import com.e.domain.model.CategoryResponseModel
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toMapper(categoryResponse: CategoryResponse): CategoryResponseModel {
        return CategoryResponseModel(
            categoryResponse.cid ?: "",
            categoryResponse.name ?: "",
            categoryResponse.parent ?: ""
        )
    }

}