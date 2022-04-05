package com.example.map1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class locationArray(@ColumnInfo var latitude:Double,
                         @ColumnInfo var longitude:Double, ){
    @PrimaryKey(autoGenerate = true)
    var id=0
}