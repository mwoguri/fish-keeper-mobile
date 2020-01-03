package com.example.fishkeeper.newcatch

import android.util.Log
import android.widget.TextView
import androidx.databinding.InverseMethod
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.text.ParseException


object BindingUtils {
    private const val TAG = "BindingUtils"
    @JvmStatic
    @InverseMethod("intToString")
    fun stringToInt(input: String?): Int? {
        return try {
            parseInt(input)
        } catch (e: Throwable) {
            null
        }

    }



    @JvmStatic
    fun intToString(int: Int?): String {
        return int?.toString() ?: ""
    }


    @JvmStatic
    @InverseMethod("doubleToString")
    fun stringToDouble(view: TextView, oldValue: Double?, value: String?): Double? {
        Log.d(TAG, "${view.text}, $oldValue, $value in stringToDouble")
        return try {
            parseDouble(value)
        } catch (e: Throwable) {
            null
        }
    }

    @JvmStatic
    fun doubleToString(view: TextView, oldValue: Double?, value: Double?): String {
        Log.d(TAG, "${view.text}, $oldValue, $value in doubleToString")
        val string = value?.toString() ?: ""
        return if (string.endsWith("0")){
            Log.d(TAG, string.substring(0, string.length - 1) )
            string.substring(0, string.length - 1)
        }

        else {
            Log.d(TAG, string )
            string
        }



    }




}