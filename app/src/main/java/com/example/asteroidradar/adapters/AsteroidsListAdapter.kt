package com.example.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.databinding.ListItemBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.asteroidradar.models.Asteroid

class AsteroidsListAdapter(
    private val asteroidItemClickListener: AsteroidItemClickListener
) : ListAdapter<Asteroid, AsteroidsListAdapter.AsteroidViewHolder>(DiffCallback) {
    class AsteroidViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, asteroidItemClickListener: AsteroidItemClickListener) {
            binding.asteroid = asteroid
            binding.clickListener = asteroidItemClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        )
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid, asteroidItemClickListener)
    }

    object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.name == newItem.name
        }

    }
}