package com.geby.shoepay.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geby.shoepay.data.models.Shoe
import com.geby.shoepay.databinding.ItemShoesBinding
import com.geby.shoepay.utilities.Helper

class ShoeAdapter(
    private var shoes: List<Shoe>,
    private val itemClickListener: (Shoe) -> Unit) :
    RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder>() {

    inner class ShoeViewHolder(private val binding: ItemShoesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(shoe: Shoe, clickListener: (Shoe) -> Unit) {
            binding.tvName.text = shoe.shoeName
            binding.tvPrice.text = Helper.rupiahFormat(shoe.shoePrice)
            Glide.with(binding.root.context)
                .load(shoe.shoeImage)
                .into(binding.ivShoe)

            binding.root.setOnClickListener {
                clickListener(shoe)  // Kirim data saat item diklik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val binding = ItemShoesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        holder.bind(shoes[position], itemClickListener)
    }

    override fun getItemCount(): Int = shoes.size

    fun updateList(newShoes: List<Shoe>) {
        shoes = newShoes
        notifyDataSetChanged()
    }
}