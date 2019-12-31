package com.example.fishkeeper.feed

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.fishkeeper.network.CatchResponse
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dateFormatted")
fun TextView.setDate(item:CatchResponse){
    val format = SimpleDateFormat("dd/MM/YY HH:mm")
    this.text = format.format(Date(item.timestamp.toLong()))
}