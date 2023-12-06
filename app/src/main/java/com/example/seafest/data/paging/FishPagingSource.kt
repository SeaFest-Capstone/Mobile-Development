package com.example.seafest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.ListFishItem

class FishPagingSource(private val apiService: ApiService, private var id: Int) : PagingSource<Int, ListFishItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    fun updateId(newId: Int) {
        id = newId
        invalidate() // Memastikan sumber daya di-refresh setelah id diperbarui
    }

    override fun getRefreshKey(state: PagingState<Int, ListFishItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListFishItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getListFish(position, params.loadSize, idHabitat = id).listFish
            val data = responseData?.filterNotNull() ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}

