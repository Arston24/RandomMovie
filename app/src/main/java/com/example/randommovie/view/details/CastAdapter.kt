package com.example.randommovie.view.details

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randommovie.models.Cast

class CastAdapter : RecyclerView.Adapter<CastViewHolder>() {

    var castList: ArrayList<Cast> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = castList[position]
        holder.viewModel.bind(cast)
    }
}