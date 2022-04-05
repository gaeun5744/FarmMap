package com.example.map1

import androidx.room.*


@Dao
interface locationArrayDao {
    @Insert
    fun insert(location:locationArray)

    @Update
    fun update(location:locationArray)

    @Delete
    fun delete(location:locationArray)

    @Query("SELECT * FROM locationArray")
    fun getAll(): List<locationArray>

    @Query("SELECT * FROM locationArray WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<locationArray>

}