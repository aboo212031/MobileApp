package com.example.mealsapp

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Meal(
    @PrimaryKey val id: Int,
    val Meal: String?,
    val DrinkAlternate: String?,
    val Category:String?,
    val Area:String?,
    val Instructions: String?,
    val MealThumb:String?,
    val Tags:String?,
    val Youtube: String?,
    val Ingredient:String,
    val Measure:String
)
