package com.example.mealsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.hypot

class MainActivity2 : AppCompatActivity() {
    lateinit var tv: TextView
    lateinit var editText: EditText
    lateinit var retrieveBtn: Button
    lateinit var db: AppDatabase
    lateinit var mealDao: MealDao
    lateinit var saveBtn:Button
    lateinit var mealDataArray: MutableList<MealData>
    var indexOfData:Int = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mealDataArray = arrayListOf()

        if (savedInstanceState != null)
        {
            var dataToCheck = savedInstanceState.getParcelableArrayList<MealData>("key")
            if (dataToCheck != null) {
                for (meal in dataToCheck) {
                    println(meal.title)
                }
            }
        }

        retrieveBtn = findViewById(R.id.retrieve_button)
        editText = findViewById(R.id.search_field)
        tv = findViewById(R.id.data_viewer)
        saveBtn = findViewById(R.id.save_button)

        db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "database"
        ).build()
        mealDao = db.MealDao()

        retrieveBtn.setOnClickListener {

            var stb = StringBuilder()
            val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=${editText.text}"
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        var bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        parseJSON(stb)
                    }
                }
            }
        }

        saveBtn.setOnClickListener {
            runBlocking {
                launch {
                    for (meal in mealDataArray)
                    {
                        var measurementString = java.lang.String.join(",",meal.measurement)
                        var ingredientString = java.lang.String.join(",",meal.ingredients)
                        var meal = Meal(indexOfData,meal.title,meal.drink, meal.category, meal.area,meal.instruction,meal.mealThumb,meal.tags, meal.youtube,ingredientString,measurementString)
                        indexOfData++
                        mealDao.insertMeals(meal)

                    }
                }
            }
        }


    }

    suspend fun parseJSON(stb: java.lang.StringBuilder) {
        val json = JSONObject(stb.toString())
        mealDataArray.clear()
        var allBooks = java.lang.StringBuilder()
        var title: String = ""
        var drink: String? = null
        var category: String? = null
        var area: String? = null
        var instructions: String? = null
        var mealThumb: String? = null
        var tags: String? = null
        var youtube: String? = null
//        var source: String? = null
//        var imageSource: String? = null
//        var creativeCommons: String? = null
//        var dateModified: String? = null

        var jsonArray: JSONArray = json.getJSONArray("meals")
        for (i in 0..jsonArray.length() - 1) {
            var ingredients: MutableList<String?> = arrayListOf()
            var measurement: MutableList<String?> = arrayListOf()
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            for (key in meal.keys()) {
                if (key.equals("strmeal", true)) {
                    title = meal[key] as String
                } else if (key.contains("drink", true)) {
                    try {
                        drink = meal[key] as String
                    } catch (x: Exception) {
                        drink = null
                    }
                } else if (key.contains("category", true)) {
                    try {
                        category = meal[key] as String
                    } catch (x: Exception) {
                        category = null
                    }
                } else if (key.contains("area", true)) {
                    try {
                        area = meal[key] as String
                    } catch (x: Exception) {
                        area = null
                    }
                } else if (key.contains("instructions", true)) {
                    try {
                        instructions = meal[key] as String
                    } catch (x: Exception) {
                        instructions = null
                    }
                } else if (key.equals("strmealthumb", true)) {
                    try {
                        mealThumb = meal[key] as String
                    } catch (x: Exception) {
                        mealThumb = null
                    }
                } else if (key.contains("tags", true)) {
                    try {
                        tags = meal[key] as String
                    } catch (x: Exception) {
                        tags = null
                    }
                } else if (key.contains("youtube", true)) {
                    try {
                        youtube = meal[key] as String
                    } catch (x: Exception) {
                        youtube = null
                    }
                } else if (key.contains("ingredient", true)) {
                    try {
                        var temp = meal[key] as String
                        ingredients.add(temp)
                    } catch (x: Exception) {
                        var temp = null
                        ingredients.add(temp)
                    }
                } else if (key.contains("measure", true)) {
                    try {
                        var temp = meal[key] as String
                        measurement.add(temp)
                    } catch (x: Exception) {
                        var temp = null
                        measurement.add(temp)
                    }
                }
//                else if (key.contains("imagesource", true)) {
//                    try {
//                        imageSource = meal[key] as String
//                    } catch (x: Exception) {
//                        imageSource = null
//                    }
//                }
//                else if (key.contains("creative", true)) {
//                    try {
//                        creativeCommons = meal[key] as String
//                    } catch (x: Exception) {
//                        creativeCommons = null
//                    }
//                }
                //                else if (key.contains("datemodified", true)) {
//                    try {
//                        dateModified = meal[key] as String
//                    } catch (x: Exception) {
//                        dateModified = null
//                    }
//                }
            }

            allBooks.append("\"Meal Name\" :  \"$title\" \n ")
            if (drink != null) {
                allBooks.append("\"Drink Alternate\" : \"$drink\" \n")
            } else {
                allBooks.append("\"Drink Alternate\" : $drink \n")
            }
            if (category != null) {
                allBooks.append("\"Category\" : \"$category\" \n")
            } else {
                allBooks.append("\"Category\" : $category \n")
            }
            if (area != null) {
                allBooks.append("\"Area\" : \"$area\" \n")
            } else {
                allBooks.append("\"Area\" : $area \n")
            }
            if (instructions != null) {
                allBooks.append("\"Instructions\" : \"$instructions\" \n")
            } else {
                allBooks.append("\"Instructions\" : $instructions \n")

            }
            if (mealThumb != null) {
                allBooks.append("\"MealThumb\" : \"$mealThumb\" \n")
            } else {
                allBooks.append("\"MealThumb\" : $mealThumb \n")
            }
            if (tags != null) {
                allBooks.append("\"Tags\" : \"$tags\" \n")
            } else {
                allBooks.append("\"Tags\" : $tags \n")
            }
            if (youtube != null) {
                allBooks.append("\"Youtube\" : \"$youtube\" \n")
            } else {
                allBooks.append("\"Youtube\" : $youtube \n")
            }
            for (i in 0..ingredients.size - 1) {
                if (ingredients.get(i) != null) {
                    allBooks.append("\"Ingredient ${i + 1}\" : \"${ingredients.get(i)}\" \n")
                } else {
                    allBooks.append("\"Ingredient ${i + 1}\" : ${ingredients.get(i)} \n")
                }
            }
            for (i in 0..measurement.size - 1) {
                if (measurement.get(i) != null) {
                    allBooks.append("\"Measure ${i + 1}\" : \"${measurement.get(i)}\" \n")
                } else {
                    allBooks.append("\"Measure ${i + 1}\" : ${measurement.get(i)} \n")
                }
            }

//            allBooks.append("\" Source\" : \"$source\" \n")
//            allBooks.append("\" Image Source\" : \"$imageSource\" \n")
//            allBooks.append("\" Creative Commons Confirmed\" : \"$creativeCommons\" \n")
//            allBooks.append("\" Date Modified\" : \"$dateModified\" \n")

            allBooks.append("\n\n")
            var mealData = MealData(indexOfData+1,title,drink,category,area,instructions,mealThumb,tags,youtube,ingredients,measurement)
            mealDataArray.add(mealData)
        }
        tv.setText(allBooks)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("key",java.util.ArrayList<MealData>(mealDataArray))
    }
}