package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.databinding.ItemColorPaletteBinding

class ColorPaletteAdapter(private var colorPalette: List<String>) :
    RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemColorPaletteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(color: String) {
            if (color.isNotBlank()) {
                try {
                    binding.ivColorPalette.setBackgroundColor(Color.parseColor(color))
                } catch (e: IllegalArgumentException) {
                    Log.e("ColorPaletteAdapter", "Invalid color string: $color", e)
                    binding.ivColorPalette.setBackgroundColor(Color.GRAY) // Set default color
                }
            } else {
                Log.e("ColorPaletteAdapter", "Color string is empty or null")
                binding.ivColorPalette.setBackgroundColor(Color.GRAY) // Set default color
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemColorPaletteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorPalette[position])
    }

    override fun getItemCount(): Int {
        return colorPalette.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPalette: List<String>) {
        colorPalette = newPalette
        notifyDataSetChanged()
    }
}
