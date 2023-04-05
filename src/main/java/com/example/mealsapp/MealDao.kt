package com.example.mealsapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {
    @Query("Select * from meal")
    suspend fun getAll(): List<Meal>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meal: Meal)
    @Insert
    suspend fun insertAll(vararg users: Meal)
    @Query("SELECT * FROM meal WHERE meal LIKE '%'||:name||'%'")
    suspend fun getMealByName(name:String):List<Meal>
}