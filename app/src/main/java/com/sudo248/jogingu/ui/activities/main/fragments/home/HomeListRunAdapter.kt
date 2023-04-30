package com.sudo248.jogingu.ui.activities.main.fragments.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sudo248.domain.entities.UserRun
import com.sudo248.jogingu.R
import com.sudo248.jogingu.databinding.ItemRunBinding
import com.sudo248.jogingu.util.TimeUtil
import com.sudo248.jogingu.util.loadImageFromUrl
import timber.log.Timber

class HomeListRunAdapter : ListAdapter<UserRun, HomeListRunAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<UserRun>() {
        override fun areItemsTheSame(oldItem: UserRun, newItem: UserRun): Boolean {
            return oldItem.userId == newItem.userId && oldItem.run.runId == newItem.run.runId
        }

        override fun areContentsTheSame(oldItem: UserRun, newItem: UserRun): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRunBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val binding: ItemRunBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(userRun: UserRun) {
            if (userRun.userImageUrl == null) {
                Glide.with(itemView)
                    .load(R.drawable.avatar)
                    .into(binding.itemImgUser)
            } else {
                binding.itemImgUser.loadImageFromUrl(userRun.userImageUrl)
            }
            binding.tvItemNameUser.text = "${userRun.firstName} ${userRun.lastName}"

            userRun.run.also { run ->
                binding.tvItemLocation.text = run.location
                binding.tvItemNameRun.text = run.name
                binding.tvItemDistance.text = "%.2f km".format(run.distance / 1000f)
                binding.tvItemPace.text = "%.2f km/h".format(run.avgSpeed / 3.6f)
                binding.tvItemTimeRun.text = TimeUtil.parseTime(run.timeRunning, true)
                if (run.imageUrl != null) {
                    Timber.d("Sudoo: imageUrl: ${run.imageUrl}")
                    binding.imgItemImgMap.loadImageFromUrl(run.imageUrl)
                } else {
                    Glide.with(itemView)
                        .load(run.imageInByteArray)
                        .optionalFitCenter()
                        .into(binding.imgItemImgMap)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}