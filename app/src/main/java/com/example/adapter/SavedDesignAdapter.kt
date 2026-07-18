package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.SavedDesign
import com.example.databinding.ItemSavedDesignBinding
import java.io.File

class SavedDesignAdapter(
    private var designs: List<SavedDesign>,
    private val onDeleteClick: (SavedDesign) -> Unit
) : RecyclerView.Adapter<SavedDesignAdapter.SavedDesignViewHolder>() {

    inner class SavedDesignViewHolder(val binding: ItemSavedDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedDesignViewHolder {
        val binding = ItemSavedDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedDesignViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedDesignViewHolder, position: Int) {
        val design = designs[position]
        val file = File(design.filePath)
        holder.binding.txtDesignFilename.text = file.name
        holder.binding.txtDesignPath.text = design.filePath

        holder.binding.btnDeleteDesign.setOnClickListener {
            onDeleteClick(design)
        }
    }

    override fun getItemCount(): Int = designs.size

    fun updateData(newDesigns: List<SavedDesign>) {
        this.designs = newDesigns
        notifyDataSetChanged()
    }
}
