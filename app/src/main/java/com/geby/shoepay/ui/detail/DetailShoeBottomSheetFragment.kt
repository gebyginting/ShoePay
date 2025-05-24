package com.geby.shoepay.ui.detail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.geby.shoepay.R
import com.geby.shoepay.data.models.Shoe
import com.geby.shoepay.ui.payment.PaymentActivity
import com.geby.shoepay.utilities.Helper

class DetailShoeDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(shoe: Shoe): DetailShoeDialogFragment {
            val fragment = DetailShoeDialogFragment()
            val args = Bundle().apply {
                putParcelable("SHOE", shoe)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var quantity = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_detail_shoe)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val shoe = arguments?.getParcelable<Shoe>("SHOE") ?: return dialog

        val imgShoe = dialog.findViewById<ImageView>(R.id.imgShoe)
        val txtName = dialog.findViewById<TextView>(R.id.txtShoeName)
        val txtPrice = dialog.findViewById<TextView>(R.id.txtShoePrice)
        val txtQty = dialog.findViewById<TextView>(R.id.tvQuantity)
        val btnMinus = dialog.findViewById<ImageButton>(R.id.btnMinus)
        val btnPlus = dialog.findViewById<ImageButton>(R.id.btnPlus)
        val btnBuy = dialog.findViewById<Button>(R.id.btnBuy)

        Glide.with(requireContext())
            .load(shoe.shoeImage)
            .into(imgShoe)
        txtName.text = shoe.shoeName
        txtPrice.text = Helper.formatRupiah(shoe.shoePrice)
        txtQty.text = quantity.toString()

        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                txtQty.text = quantity.toString()
            }
        }

        btnPlus.setOnClickListener {
            quantity++
            txtQty.text = quantity.toString()
        }

        btnBuy.setOnClickListener {
            val intent = Intent(requireContext(), PaymentActivity::class.java).apply {
                putExtra(PaymentActivity.EXTRA_NAME, shoe.shoeName)
                putExtra(PaymentActivity.EXTRA_PRICE, shoe.shoePrice)
                putExtra(PaymentActivity.EXTRA_QUANTITY, quantity)
            }
            startActivity(intent)
            dismiss()
        }

        return dialog
    }
}
