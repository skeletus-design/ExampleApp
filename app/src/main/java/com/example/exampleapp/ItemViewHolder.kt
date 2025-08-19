package com.example.exampleapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ItemText: TextView = itemView.findViewById(R.id.text_view)
}