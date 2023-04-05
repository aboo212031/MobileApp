package com.example.mealsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity3 : AppCompatActivity() {
    lateinit var db: AppDatabase
    lateinit var mealDao: MealDao
    lateinit var searchBar:EditText
    lateinit var searchBtn:Button
    lateinit var results:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        searchBar = findViewById(R.id.search_db)
        searchBtn = findViewById(R.id.search_db_btn)
        results = findViewById(R.id.db_result)

        db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "database"
        ).build()
        mealDao = db.MealDao()

        var textToSearch = searchBar.text.toString()

        searchBtn.setOnClickListener {
            runBlocking {
                launch {
                    var listOfMeals = mealDao.getMealByName(textToSearch)
                    println(listOfMeals.size)
                    var resultShower = java.lang.StringBuilder()
                    for (i in listOfMeals)
                    {
                        resultShower.append("\"Meal Name\" :  \"${i.Meal}\" \n ")
                        if (i.DrinkAlternate != null) {
                            resultShower.append("\"Drink Alternate\" : \"${i.DrinkAlternate}\" \n")
                        } else {
                            resultShower.append("\"Drink Alternate\" : ${i.DrinkAlternate}\n")
                        }
                        if (i.Category != null) {
                            resultShower.append("\"Category\" : \"${i.Category}\" \n")
                        } else {
                            resultShower.append("\"Category\" : ${i.Category} \n")
                        }
                        if (i.Area != null) {
                            resultShower.append("\"Area\" : \"${i.Area}\" \n")
                        } else {
                            resultShower.append("\"Area\" : ${i.Area} \n")
                        }
                        if (i.Instructions != null) {
                            resultShower.append("\"Instructions\" : \"${i.Instructions}\" \n")
                        } else {
                            resultShower.append("\"Instructions\" : ${i.Instructions} \n")

                        }
                        if (i.MealThumb != null) {
                            resultShower.append("\"MealThumb\" : \"${i.MealThumb}\" \n")
                        } else {
                            resultShower.append("\"MealThumb\" : ${i.MealThumb} \n")
                        }
                        if (i.Tags != null) {
                            resultShower.append("\"Tags\" : \"${i.Tags}\" \n")
                        } else {
                            resultShower.append("\"Tags\" : ${i.Tags} \n")
                        }
                        if (i.Youtube != null) {
                            resultShower.append("\"Youtube\" : \"${i.Youtube}\" \n")
                        } else {
                            resultShower.append("\"Youtube\" : ${i.Youtube} \n")
                        }
                        var ingredients = i.Ingredient.split(",")
                        for (i in 0..ingredients.size - 1) {
                            if (ingredients.get(i) != null) {
                                resultShower.append("\"Ingredient ${i + 1}\" : \"${ingredients.get(i)}\" \n")
                            } else {
                                resultShower.append("\"Ingredient ${i + 1}\" : ${ingredients.get(i)} \n")
                            }
                        }
                        var measurement = i.Measure.split(",")
                        for (i in 0..measurement.size - 1) {
                            if (measurement.get(i) != null) {
                                resultShower.append("\"Measure ${i + 1}\" : \"${measurement.get(i)}\" \n")
                            } else {
                                resultShower.append("\"Measure ${i + 1}\" : ${measurement.get(i)} \n")
                            }
                        }
                    }
                    results.text = resultShower
                }
            }
        }

    }
}