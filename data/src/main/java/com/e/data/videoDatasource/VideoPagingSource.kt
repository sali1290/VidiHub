package com.e.data.videoDatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.e.data.api.ApiService
import com.e.data.model.VideoListItem
import retrofit2.HttpException
import java.io.IOException


class VideoPagingSource(val apiService: ApiService) :
    PagingSource<Int, VideoListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoListItem> {
        //for first case it will be null, then we can pass some default value, in our case it's 1

        val page = params.key ?: 0
        return try {
            val response = apiService.getVideoPaging(page, 8)
            LoadResult.Page(
                response.body()!!, prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.body()!!.isEmpty()) null else page + 1
            )


        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoListItem>): Int? {
        TODO("Not yet implemented")
    }
}