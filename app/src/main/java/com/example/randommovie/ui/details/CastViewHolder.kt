package com.example.randommovie.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.randommovie.models.Cast
import ru.arston.randommovie.databinding.ItemCastBinding

class CastViewHolder(binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
    val viewModel = ViewModel()

    init {
        binding.data = viewModel
    }

    companion object {
        fun create(parent: ViewGroup): CastViewHolder {
            val binding =
                ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CastViewHolder(binding)
        }
    }

    class ViewModel{
        val name = ObservableField<String>()
        val heroName = ObservableField<String>()
        val avatar = ObservableField<String>()
        fun bind(cast: Cast){
            name.set(cast.name)
            heroName.set(cast.character)
            avatar.set(cast.profilePath)
        }
    }
}