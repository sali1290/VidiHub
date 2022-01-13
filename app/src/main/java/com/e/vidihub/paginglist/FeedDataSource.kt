package com.e.vidihub.paginglist

import androidx.paging.PageKeyedDataSource
import com.e.domain.model.VideoListItemModel
import com.e.domain.usecase.GetAllVideosUseCase
import javax.inject.Inject


class FeedDataSource @Inject constructor(private val getAllVideosUseCase: GetAllVideosUseCase) :
    PageKeyedDataSource<Long, VideoListItemModel?>() {

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, VideoListItemModel?>
    ) {
    }

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, VideoListItemModel?>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, VideoListItemModel?>
    ) {
        TODO("Not yet implemented")
    }
}