package com.example.seafest.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.local.FishDatabase
import com.example.seafest.data.local.FishEntity

@OptIn(ExperimentalPagingApi::class)
class FishRemoteMediator(
    private val database: FishDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, FishEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FishEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getListFish(page, state.config.pageSize)
            val endOfPaginationReached = responseData.listFish?.isEmpty()
            val fishList = responseData.listFish?.map {
                FishEntity(
                    it?.id!!,
                    it.photoUrl,
                    it.habitat,
                    it.price,
                    it.description,
                    it.idHabitate,
                    it.nameFish,
                    it.benefit
                )
            }
//            println("loadType: $loadType, page: $page")
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.fishDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val keys = responseData.listFish?.map {
                    RemoteKeys(id = it?.id!!, prevKey = prevKey, nextKey = nextKey)
                }
                if (keys != null) {
                    database.remoteKeysDao().insertAll(keys)
                }
                if (fishList != null) {
                    database.fishDao().insertFish(fishList)
                }
            }
//            println("loadType: $loadType, page: $page, prevKey: $prevKey, nextKey: $nextKey, endOfPaginationReached: $endOfPaginationReached")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached!!)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FishEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FishEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, FishEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}