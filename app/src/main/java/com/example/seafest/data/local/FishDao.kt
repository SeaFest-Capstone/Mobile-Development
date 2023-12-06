package com.example.seafest.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFish(quote: List<FishEntity>)

    @Query("SELECT * FROM fish ")
    fun getAllFish(): PagingSource<Int, FishEntity>

    @Query("SELECT * FROM fish WHERE idHabitate= :id")
    fun getFish(id:Int?): PagingSource<Int, FishEntity>


    @Query("SELECT * FROM fish WHERE nameFish= :name")
    fun findFish(name:String): PagingSource<Int, FishEntity>

    @Query("DELETE FROM fish")
    suspend fun deleteAll()
}