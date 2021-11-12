package com.sudo.jogingu.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sudo.domain.entities.Run
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.ItemRunBinding

class HomeListRunAdapter : ListAdapter<Run, HomeListRunAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean = oldItem.runId == newItem.runId
        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean = oldItem.hashCode() == newItem.hashCode()
    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeListRunAdapter.ViewHolder {
        return ViewHolder(ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class ViewHolder(private val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(run: Run){
            Glide.with(itemView)
                .load(R.drawable.avatar)
                .into(binding.itemImgUser)
            binding.tvItemNameUser.text = "Jogingu"
            binding.tvItemLocation.text = ""
            binding.tvItemNameRun.text = run.name
            binding.tvItemDistance.text = "%.2f km".format(run.distance)
            binding.tvItemPace.text = "%.2f km/h".format(run.avgSpeed)
            binding.tvItemTimeRun.text = "${run.timeRunning}"

            Glide.with(itemView)
                .load(run.imageInByteArray)
                .into(binding.imgItemImgMap)
        }
    }
    override fun onBindViewHolder(holder: HomeListRunAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}