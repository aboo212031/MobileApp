package com.example.mealsapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class MealData(var id:Int,var title:String?,var drink:String?,var category:String?, var area:String?, var instruction:String?, var mealThumb: String?,var tags: String?, var youtube:String?,var ingredients:MutableList<String?>, var measurement:MutableList<String?>):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(drink)
        parcel.writeString(category)
        parcel.writeString(area)
        parcel.writeString(instruction)
        parcel.writeString(mealThumb)
        parcel.writeString(tags)
        parcel.writeString(youtube)
        parcel.writeStringList(ingredients)
        parcel.writeStringList(measurement)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealData> {
        override fun createFromParcel(parcel: Parcel): MealData {
            return MealData(parcel)
        }

        override fun newArray(size: Int): Array<MealData?> {
            return arrayOfNulls(size)
        }
    }
}
