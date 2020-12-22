package com.example.smartpi.view.pembelian

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailPembelianActivity : AppCompatActivity() {

    private var nama_produk = ""
    private var id_produk = ""
    private var harga_produk = ""
    private var hargaProdukInt = ""
    private var durasi_produk = ""
    private var codeWallet = ""
    private var jenis = ""
    private var totalHarga = 0

    var token = ""
    lateinit var preferences: Preferences

    var walletList = ArrayList<WalletItem>()
    var vaList = ArrayList<WalletItem>()

    lateinit var binding: ActivityDetailPembelianBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

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
        jenis = intent.getStringExtra("jenis")

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
            scope.launch { checkEmail() }
        }
        binding.clBankOther.setOnClickListener {
            scope.launch { requestXendit(binding.etKodeVoucher.text.toString()) }
        }
        binding.btnEmail.setOnClickListener {
            scope.launch { updateEmail() }
        }

        binding.etEmail.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.etKodeVoucher.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.etNmrHpDetailPembayaran.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_NUMPAD_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }


        binding.btnLanjutPembayaran.setOnClickListener {
            binding.btnLanjutPembayaran.visibility = View.GONE
            binding.pbPembayaran.visibility = View.VISIBLE
            when (codeWallet) {
                "OVO" -> {
                    if (jenis == "paket") {
                        scope.launch { requestOVO() }
                    } else {
                        scope.launch { requestOVOGroup() }
                    }

                }
                "DANA" -> {
                    if (jenis == "paket") {
                        scope.launch { requestDana() }
                    } else {
                        scope.launch { requestDanaGroup() }
                    }
                }
                "LINKAJA" -> {
                    if (jenis == "paket") {
                        scope.launch { requestLinkAja() }
                    } else {
                        scope.launch { requestLinkAjaGroup() }
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private suspend fun getWallet() {
        walletList.clear()
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
                        if (jenis == "paket") {
                            scope.launch { requestGopay() }
                        } else {
                            scope.launch { requestGopayGroup() }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getVA() {

        vaList.clear()
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
                            if (jenis == "paket") {
                                requestVA(it.code, etKode.toString())
                            } else {
                                requestVAGroupClass(it.code, etKode.toString())
                            }

                        }
                    }
                    "BNI" -> {
                        scope.launch {
                            if (jenis == "paket") {
                                requestVA(it.code, etKode.toString())
                            } else {
                                requestVAGroupClass(it.code, etKode.toString())
                            }
                        }
                    }
                    "BRI" -> {
                        scope.launch {
                            if (jenis == "paket") {
                                requestVA(it.code, etKode.toString())
                            } else {
                                requestVAGroupClass(it.code, etKode.toString())
                            }
                        }
                    }
                    "PERMATA" -> {
                        scope.launch {
                            if (jenis == "paket") {
                                requestVA(it.code, etKode.toString())
                            } else {
                                requestVAGroupClass(it.code, etKode.toString())
                            }
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
        val bsbEmail = BottomSheetBehavior.from(binding.llEmail)
        bsbPembayaran.skipCollapsed = true
        bsbInputNmr.skipCollapsed = true
        bsbEmail.skipCollapsed = true

        binding.ivCancelPembelian.setOnClickListener {
            bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivCancelInputNomor.setOnClickListener {
            bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bsbPembayaran.state = BottomSheetBehavior.STATE_HIDDEN
        bsbInputNmr.state = BottomSheetBehavior.STATE_HIDDEN
        bsbEmail.state = BottomSheetBehavior.STATE_HIDDEN

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


        bsbEmail.addBottomSheetCallback(object :
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

    private suspend fun requestOVOGroup() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageBerhasil = "Silahkan cek aplikasi OVO Anda melanjutkan Pembayaran"
        val messageGagal = "Silahkan Cek kembali "

        val networkConfig =
            NetworkConfig().getWallet().requestOVOGroup(token, id_produk, "OVO", etKode, etPhone)
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

    private suspend fun requestDanaGroup() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestDanaGroup(token, id_produk, "DANA", etKode, etPhone)
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

    private suspend fun requestLinkAjaGroup() {
        val etPhone = binding.etNmrHpDetailPembayaran.text.toString()
        val etKode = binding.etKodeVoucher.text.toString()
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet()
                .requestLinkAjaGroup(token, id_produk, "LINKAJA", etKode, etPhone)
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
        val etKode = binding.etKodeVoucher.text.toString()
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

    private suspend fun requestGopayGroup() {
        val etKode = binding.etKodeVoucher.text.toString()
        val messageGagal = "Silahkan Cek kembali "
        val networkConfig =
            NetworkConfig().getWallet().requestGopayGroup(token, id_produk, "GOPAY", etKode)
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

    @SuppressLint("SetTextI18n")
    private suspend fun requestVAGroupClass(bank: String, kode: String) {
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

        val vaNetwork =
            NetworkConfig().getWallet().requestVAGroupClass(token, id_produk, bank, kode)
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

        hargaProdukInt = if (jenis == "paket") {
            harga_produk.drop(4).replace(".", "")
        } else {
            harga_produk
        }

        val etKode = binding.etKodeVoucher.text.toString()
        val networkConfig = NetworkConfig().getWallet().checkVoucher(token, etKode, id_produk)
        if (networkConfig.isSuccessful) {
            totalHarga =
                hargaProdukInt.toInt() - networkConfig.body()!!.data!!.voucherValue!!.toInt()
            if (totalHarga == 0) {
                PaymentFree()
            }
        } else {
            bsbPembayaran.state = BottomSheetBehavior.STATE_EXPANDED

        }
    }

    suspend fun checkEmail() {
        val bsbEmail = BottomSheetBehavior.from(binding.llEmail)
        val networkConfig = NetworkConfig().getUser().checkEmail(token)
        if (networkConfig.isSuccessful) {
            if (jenis == "trial") {
                ambilTrial()
            } else {
                checkVoucher()
            }

        } else {
            bsbEmail.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    suspend fun updateEmail() {
        val bsbEmail = BottomSheetBehavior.from(binding.llEmail)
        val bsbWallet = BottomSheetBehavior.from(binding.llWallet)
        val email = binding.etEmail.text.toString()
        val networkConfig = NetworkConfig().getUser().updateEmail(token, email)
        if (networkConfig.isSuccessful) {
            bsbEmail.state = BottomSheetBehavior.STATE_HIDDEN
            binding.pbEmail.visibility = View.GONE
            binding.btnEmail.visibility = View.VISIBLE
            bsbWallet.state = BottomSheetBehavior.STATE_EXPANDED
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, "Email berhasil didaftarkan", Toast.LENGTH_LONG).show()
            }

        } else {
            binding.pbEmail.visibility = View.GONE
            binding.btnEmail.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Email Gagal didaftarkan, Harap coba lagi atau cek koneksi anda",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun ambilTrial() {
        val networkConfig = NetworkConfig().packageTrial().ambilTrial(token, id_produk)
        if (networkConfig.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, "Ambil Trial Berhasil", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Ambil Trial Gagal, Silahkan coba lagi atau cek koneksi anda.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun PaymentFree() {
        val etKode = binding.etKodeVoucher.text.toString()
        val networkConfig = NetworkConfig().getWallet().paymentFree(token, etKode, id_produk)
        Log.d("TAG", "PaymentFree: $id_produk, $totalHarga,$etKode")
        if (networkConfig.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.d("TAG", "PaymentFree: ${networkConfig.body()}")
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, "Paket berhasil diambil", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Log.d("TAG", "PaymentFree :${networkConfig.body()}")
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Paket gagal diambil, silahkan cek koneksi dan coba lagi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

