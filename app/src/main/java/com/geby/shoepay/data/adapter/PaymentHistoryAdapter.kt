package com.geby.shoepay.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geby.shoepay.data.models.PaymentHistory
import com.geby.shoepay.databinding.ItemPaymentHistoryBinding
import com.geby.shoepay.utilities.Helper

class PaymentHistoryAdapter(private val paymentHistoryList: List<PaymentHistory>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val binding = ItemPaymentHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        val paymentHistory = paymentHistoryList[position]
        holder.bind(paymentHistory)
    }

    override fun getItemCount(): Int = paymentHistoryList.size

    class PaymentHistoryViewHolder(private val binding: ItemPaymentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paymentHistory: PaymentHistory) {
            binding.tvPaymentMethod.text = paymentHistory.payment_method
            binding.tvAmount.text = Helper.rupiahFormat(paymentHistory.amount)
            binding.tvStatus.text = paymentHistory.status

            binding.tvStatus.setTextColor(
                if (paymentHistory.status == "Completed") Color.GREEN else Color.RED
            )
        }
    }
}
