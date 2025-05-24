package com.geby.shoepay.ui.payment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.geby.shoepay.R
import com.geby.shoepay.databinding.FragmentPaymentDetailDialogBinding
import com.geby.shoepay.ui.shoes.ShoesListActivity
import com.geby.shoepay.utilities.Helper.generateQrCode
import com.geby.shoepay.viewmodel.PaymentViewModel
import com.geby.shoepay.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentDetailDialogFragment : DialogFragment() {

    private var _binding: FragmentPaymentDetailDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentViewModel: PaymentViewModel
    private var countDownTimer: CountDownTimer? = null

    companion object {
        fun newInstance(reference: String): PaymentDetailDialogFragment {
            val fragment = PaymentDetailDialogFragment()
            val args = Bundle().apply {
                putString("REFERENCE", reference)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentDetailDialogBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireContext())
        paymentViewModel = viewModels<PaymentViewModel> { factory }.value

        binding.btnConfirm.setOnClickListener {
            val intent = Intent(requireContext(), ShoesListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
        binding
        getDetailPayment()
        return binding.root
    }

    private fun getDetailPayment() {
        val reference = arguments?.getString("REFERENCE") ?: return
        paymentViewModel.getDetailPayment(reference) // <-- kirim reference-nya

        paymentViewModel.detailPayment.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                val method = detail.data?.paymentMethod
                val va = detail.data?.payCode
                val qrUrl = detail.data?.qrUrl
                val amount = detail.data?.amount
                val fee = detail.data?.totalFee
                val item_reference = detail.data?.reference
                val status = detail.data?.status

                val total = fee?.let { it1 -> amount?.plus(it1) }

                binding.method.text = method
                if (method.equals("qrisc", ignoreCase = true)) {
                    showQRCode(qrUrl.toString())

                } else {
                    binding.va.text = va
                }
                binding.date.text = getTodayDate()
                binding.ref.text = item_reference
                binding.subtotal.text = amount.toString()
                binding.adminFee.text = fee.toString()
                binding.total.text = total.toString()
                binding.tvStatus.text = status


                when (status) {
                    "KADALUARSA" -> binding.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }

                val expiredTimestamp = detail.data?.expiredTime // dari API, dalam DETIK
                val currentUnix = System.currentTimeMillis() / 1000 // waktu sekarang, dalam detik

                if (expiredTimestamp != null) {
                    val timeLeftMillis = (expiredTimestamp - currentUnix) * 1000
                    if (timeLeftMillis > 0) {
                        startCountdown(timeLeftMillis)
                    } else {
                        binding.tvExpired.text = "Waktu Habis"
                    }
                }

                val instructionContainer = binding.instructionContainer
                binding.toggleInstructions.setOnClickListener {
                    if (instructionContainer.visibility == View.GONE) {
                        instructionContainer.visibility = View.VISIBLE
                        instructionContainer.removeAllViews()

                        val context = requireContext()

                        detail.data?.instructions?.forEach { instruction ->
                            val titleView = TextView(context).apply {
                                text = instruction?.title
                                textSize = 16f
                                setTypeface(null, Typeface.BOLD)
                            }
                            instructionContainer.addView(titleView)

                            // Tambah langkah-langkah
                            instruction?.steps?.forEachIndexed { index, step ->
                                val stepView = TextView(context).apply {
                                    text = "${index + 1}. ${step?.let { it1 -> HtmlCompat.fromHtml(it1, HtmlCompat.FROM_HTML_MODE_LEGACY) }}"
                                    textSize = 14f
                                }
                                instructionContainer.addView(stepView)
                            }
                        }

                        binding.toggleInstructions.text = "Sembunyikan Instruksi Pembayaran"
                    } else {
                        instructionContainer.visibility = View.GONE
                        binding.toggleInstructions.text = "Instruksi Pembayaran \uD83D\uDC47"
                    }
                }
            }
        }
    }

    private fun startCountdown(millis: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvExpired.text = "Kadaluarsa dalam: %02d:%02d".format(minutes, seconds)
            }

            override fun onFinish() {
                binding.tvExpired.text = "Waktu Habis"
                binding.tvStatus.text = "Status: Kadaluarsa"
            }
        }.start()
    }

    private fun showQRCode(qrUrl: String) {
        val qrImageView = binding.imgQrCode
        qrImageView.visibility = View.VISIBLE
        val qrBitmap = generateQrCode(qrUrl)
        qrImageView.setImageBitmap(qrBitmap)

        binding.va.visibility = View.GONE
        binding.tvVa.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}