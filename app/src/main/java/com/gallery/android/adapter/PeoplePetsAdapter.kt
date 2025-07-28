package com.gallery.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.android.R
import com.gallery.android.databinding.ItemPeoplePetBinding

class PeoplePetsAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PeoplePetsAdapter.PeoplePetViewHolder>() {

    private var peoplePetsData = listOf<Pair<String, String>>()

    fun updateData(data: List<Pair<String, String>>) {
        peoplePetsData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeoplePetViewHolder {
        val binding = ItemPeoplePetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PeoplePetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeoplePetViewHolder, position: Int) {
        holder.bind(peoplePetsData[position])
    }

    override fun getItemCount(): Int = peoplePetsData.size

    inner class PeoplePetViewHolder(
        private val binding: ItemPeoplePetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(personData: Pair<String, String>) {
            val (name, imagePath) = personData
            
            // Set name
            binding.nameLabel.text = name
            
            // For now, use a placeholder image since we don't have face detection
            // In a real app, this would load the person's face thumbnail
            binding.imageView.setImageResource(R.drawable.ic_person_placeholder)

            // Click handling
            binding.root.setOnClickListener {
                onItemClick(name)
            }
        }
    }
}