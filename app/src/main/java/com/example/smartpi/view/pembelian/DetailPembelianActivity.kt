package com.example.smartpi.view.pembelian

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.CreditCardActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.adapter.WalletAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailPembelianBinding
import com.example.smartpi.model.WalletItem
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.pembelian.virtulaccount.VirtualAccountActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailPembelianActivity : AppCompatActivity() {

    var nama_produk = ""
    var id_produk = ""
    var harga_produk = ""
    var durasi_produk = ""
    var codeWallet = ""

    var token = ""
    lateinit var preferences: Preferences

    var walletList = ArrayList<WalletItem>()
    var vaList = ArrayList<WalletItem>()

    lateinit var binding: ActivityDetailPembelianBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    var rekening = ""
    var tagihan = ""
    var tglExpired = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPembelianBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"

        id_produk = intent.getStringExtra("id")
        nama_produk = intent.getStringExtra("nama")
        harga_produk = intent.getStringExtra("harga")
        durasi_produk = intent.getStringExtra("durasi")

        binding.tvHargaPaketDetailPembayaran.text = harga_produk
        binding.tvMasaAktif.text = durasi_produk
        binding.tvNamaProduk.text = nama_produk
        binding.tvTotalPembayaran.text = harga_produk
        binding.tvNamaPaketDetailPembayaran.text = nama_produk

        //ambil tanggal sekarang
        val myFormat = "dd MMMM yyyy"
        val calendar = Calendar.getInstance()
        val time = calendar.time
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val curdate = sdf.format(time) //diconvert ke tanggal local

        binding.tvTanggalPemesanan.text = curdate
        bottomSheet()
        scope.launch {
            val job1 = async { getWallet() }
            val job2 = async { getVA() }
            job1.await()
            job2.await()
        }

        binding.ivBackDetailPaket.setOnClickListener {
            finish()
        }
        binding.btnBayar.setOnClickListener {
            scope.launch { checkVoucher() }
        }
        binding.clBankOther.setOnClickListener {
            scope.launch { requestXendit(binding.etKodeVoucher.text.toString()) }
        }
        binding.btnLanjutPembayaran.setOnClickListener {
            binding.btnLanjutPembayaran.visibility = View.GONE
            binding.pbPembayaran.visibility = View.VISIBLE
            when (codeWallet) {
                "OVO" -> {
                    scope.launch { requestOVO() }
                }
                "DANA" -> {
                    scope.launch { requestDana() }
                }
                "LINKAJA" -> {
                    scope.launch { requestLinkAja() }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun getWallet() {
        val bsbPembayaran = BottomSheetBehavior.from(binding.llWallet)
        val bsbInputNmr = BottomSheetBehavior.from(binding.llInputNomor)

        val networkConfig = NetworkConfig().getWallet().getWallet()
        if (networkConfig.isSuccessful) {
            for (wallet in networkConfig.body()!!.data!!) {
                walletList.add(wallet!!)
            }
            binding.rvEWallet.layoutManager = LinearLayoutManager(this)
            binding.rvEWallet.adapter = WalletAdapter(walletList) {

                when (it.code) {
                    "OVO" -> {
                        bsbInputNmr.state = BottomSheetBehavior.STATE_EXPANDED
                        bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.tvLabelNamaWallet.text = "Metode Pembayaran dengan OVO"
                        binding.tvLabelMasukanNmrHp.text =
                            "Masukan nomor handphone yang terhubung dengan akun OVO Anda"
                        codeWallet = it.code
                    }
                    "DANA" -> {
                        bsbInputNmr.state = BottomSheetBehavior.STATE_EXPANDED
                        bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.tvLabelNamaWallet.text = "Metode Pembayaran dengan Dana"
                        binding.tvLabelMasukanNmrHp.text =
                            "Masukan nomor handphone yang terhubung dengan akun DANA Anda."
                        codeWallet = it.code
                    }
                    "LINKAJA" -> {
                        bsbInputNmr.state = BottomSheetBehavior.STATE_EXPANDED
                        bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.tvLabelNamaWallet.text = "Metode Pembayaran dengan LinkAja"
                        binding.tvLabelMasukanNmrHp.text =
                            "Masukan nomor handphone yang terhubung dengan akun LinkAja Anda."
                        codeWallet = it.code
                    }
                    "GOPAY" -> {
                        scope.launch { requestGopay() }
                    }
                }
            }
        }
    }

    private suspend fun getVA() {

        val etKode = binding.etKodeVoucher.text
        val networkConfig = NetworkConfig().getWallet().getVA()
        if (networkConfig.isSuccessful) {
            for (wallet in networkConfig.body()!!.data!!) {
                vaList.add(wallet!!)
            }
            binding.rvVirtualAccount.layoutManager = LinearLayoutManager(this)
            binding.rvVirtualAccount.adapter = WalletAdapter(vaList) {
                when (it.code) {
                    "MANDIRI" -> {
                        scope.launch {

                            requestVA(it.code, etKode.toString())
                        }
                    }
                    "BNI" -> {
                        scope.launch {

                            requestVA(it.code, etKode.toString())
                        }
                    }
                    "BRI" -> {
                        scope.launch {
                            requestVA(it.code, etKode.toString())
                        }
                    }
                    "PERMATA" -> {

                        scope.launch {
                            requestVA(it.code, etKode.toString())
                        }
                    }
                    "BCA" -> {
                        val alertDialogBuilder: AlertDialog.Builder =
                            AlertDialog.Builder(this)

                        alertDialogBuilder.setTitle("Belum Tersedia")
                        alertDialogBuilder
                            .setMessage("Virtual Account Bank BCA saat ini tahap pengembangan")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Tutup",
                                DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                })
                        alertDialogBuilder.show()
                    }
                }

            }
        }
    }

    private fun bottomSheet() {

        val bsbPembayaran = BottomSheetBehavior.from(binding.llWallet)
        val bsbInputNmr = BottomSheetBehavior.from(binding.llInputNomor)

        bsbPembayaran.skipCollapsed = true
        bsbInputNmr.skipCollapsed = true

        binding.ivCancelPembelian.setOnClickListener {
            bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivCancelInputNomor.setOnClickListener {
            bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
        bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN

        bsbInputNmr.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bsbPembayaran.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


        bsbPembayaran.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private suspend fun requestOVO() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageBerhasil = "Silahkan cek aplikasi OVO Anda melanjutkan Pembayaran"
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestOVO(token, id_produk, etKode, etPhone)
        if (networkConfig.isSuccessful) {
            alertDialogPermintaanPembayaran("Berhasil", messageBerhasil)
            binding.btnLanjutPembayaran.visibility = View.VISIBLE
            binding.pbPembayaran.visibility = View.GONE
        } else {
            alertDialogPermintaanPembayaran("Gagal", messageGagal)
        }
    }

    private suspend fun requestDana() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageBerhasil = "Silahkan cek aplikasi DANA Anda melanjutkan Pembayaran"
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestDANA(token, id_produk, etKode, etPhone)
        if (networkConfig.isSuccessful) {
            val url = networkConfig.body()!!.data!!.checkoutUrl
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            binding.btnLanjutPembayaran.visibility = View.VISIBLE
            binding.pbPembayaran.visibility = View.GONE
        } else {
            alertDialogPermintaanPembayaran("Gagal", messageGagal)
        }
    }

    private suspend fun requestLinkAja() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageBerhasil = "Silahkan cek aplikasi LinkAja Anda melanjutkan Pembayaran"
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestLinkAja(token, id_produk, etKode, etPhone)
        if (networkConfig.isSuccessful) {
            val url = networkConfig.body()!!.data!!.checkoutUrl
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            binding.btnLanjutPembayaran.visibility = View.VISIBLE
            binding.pbPembayaran.visibility = View.GONE
        } else {
            alertDialogPermintaanPembayaran("Gagal", messageGagal)
        }
    }

    private suspend fun requestGopay() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageBerhasil = "Silahkan cek aplikasi Gopay Anda melanjutkan Pembayaran"
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestGopay(token, id_produk, etKode)
        if (networkConfig.isSuccessful) {
            val url = networkConfig.body()!!.data!!.actions!![1]!!.url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            binding.btnLanjutPembayaran.visibility = View.VISIBLE
            binding.pbPembayaran.visibility = View.GONE
        } else {
            alertDialogPermintaanPembayaran("Gagal", messageGagal)
        }
    }

    private fun alertDialogPermintaanPembayaran(status: String, message: String) {
        val bsbPembayaran = BottomSheetBehavior.from(binding.llWallet)
        val bsbInputNmr = BottomSheetBehavior.from(binding.llInputNomor)

        val alertDialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Permintaan Pembayaran $status")
        alertDialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener { dialog, _ ->
                    bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN
                    bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
                    dialog.dismiss()
                })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        val bsbPembayaran = BottomSheetBehavior.from(binding.llWallet)
        val bsbInputNmr = BottomSheetBehavior.from(binding.llInputNomor)

        if (bsbInputNmr.state == BottomSheetBehavior.STATE_EXPANDED) {
            bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN
        }
        if (bsbPembayaran.state == BottomSheetBehavior.STATE_EXPANDED) {
            bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            finish()
        }
    }

    private suspend fun requestVA(bank: String, kode: String) {
        val dialog = Dialog(this)
        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.pop_up_loading_virtual_account)
        dialog.setCancelable(true)

        val tvApakahYakin = dialog.findViewById<TextView>(R.id.tv_request)
        tvApakahYakin.text = "Requesting Virtual Account Info"

        dialog.show()

        val vaNetwork = NetworkConfig().getWallet().requestVA(token, id_produk, bank, kode)
        if (vaNetwork.isSuccessful) {
            val rekening = vaNetwork.body()!!.data!!.accountNumber.toString()
            val tglExpired = vaNetwork.body()!!.data!!.expirationDate.toString()
            val tagihan = vaNetwork.body()!!.data!!.expectedAmount.toString()

            Log.d("TAG", "getVA: $id_produk, $rekening , $tglExpired, $tagihan ")

            val intent = Intent(
                this@DetailPembelianActivity,
                VirtualAccountActivity::class.java
            )
            intent.putExtra("tgl", tglExpired)
            intent.putExtra("rekening", rekening)
            intent.putExtra("tagihan", tagihan)
            intent.putExtra("bank", bank)
            startActivity(intent)
            dialog.dismiss()
        } else {
            Log.d("TAG", "requestVA: GAGAL")
        }


    }

    private suspend fun requestXendit(kode: String) {

        val dialog = Dialog(this)
        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.pop_up_loading_virtual_account)
        dialog.setCancelable(true)

        val tvApakahYakin = dialog.findViewById<TextView>(R.id.tv_request)
        tvApakahYakin.text = "Send Request to Xendit"

        dialog.show()

        val networkConfig = NetworkConfig().getWallet().requestXendit(token, id_produk, kode)
        var url = ""

        if (networkConfig.isSuccessful) {
            url = networkConfig.body()!!.url!!
            val intent = Intent(
                this@DetailPembelianActivity,
                CreditCardActivity::class.java
            )
            intent.putExtra("url", url)
            startActivity(intent)
            dialog.dismiss()
        }
    }

    private suspend fun checkVoucher() {
        val bsbPembayaran = BottomSheetBehavior.from(binding.llWallet)

        val etKode = binding.etKodeVoucher.text.toString()
        val networkConfig = NetworkConfig().getWallet().checkVoucher(token, etKode, id_produk)
        if (networkConfig.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            bsbPembayaran.state = BottomSheetBehavior.STATE_EXPANDED

        }
    }
}

