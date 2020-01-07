package com.example.fishkeeper.newcatch

import androidx.databinding.InverseMethod
import java.lang.Integer.parseInt

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

}