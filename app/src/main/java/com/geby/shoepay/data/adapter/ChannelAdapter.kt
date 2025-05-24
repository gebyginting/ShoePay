package com.geby.shoepay.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geby.shoepay.R
import com.geby.shoepay.response.DataItem

class ChannelAdapter(
    private val channels: List<DataItem>,
    private val onChannelSelected: (DataItem) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    private var selectedChannel = -1

    inner class ChannelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imgIcon)
        val tvName: TextView = view.findViewById(R.id.tvChannelName)
        val tvCode: TextView = view.findViewById(R.id.tvChannelCode)
        val radioButton: RadioButton = view.findViewById(R.id.radioButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = channels[position]
        holder.tvName.text = channel.name ?: "-"
        holder.tvCode.text = channel.code ?: "-"

        Glide.with(holder.itemView.context)
            .load(channel.iconUrl)
            .into(holder.icon)

        holder.radioButton.isChecked = selectedChannel == position

        val clickListener = {
            val previousPosition = selectedChannel
            selectedChannel = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedChannel)
            onChannelSelected(channel) // 🔥 Update ViewModel
        }

        holder.radioButton.setOnClickListener { clickListener() }
        holder.itemView.setOnClickListener { clickListener() }
    }

    fun getSelectedChannel(): DataItem? {
        return if (selectedChannel != -1) channels[selectedChannel] else null
    }
    override fun getItemCount(): Int = channels.size
}
