package com.example.smartpi.view.kelas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.models.SlideModel
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailKelasBinding
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailKelasActivity : AppCompatActivity() {

    var timeInMilliseconds: Long = 0
    var token = ""
    lateinit var preferences: Preferences
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: ActivityDetailKelasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailKelasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"

        scope.async(Dispatchers.Main) {

            val job1 = async { getDetailKelas() }
            val job2 = async { imageSlide() }
            val job3 = async { checkJamMasuk() }
            job1.join()
            job2.join()
            job3.join()
        }

        binding.ivBackDetailKelas.setOnClickListener { finish() }

        binding.btnBatalJadwal.setOnClickListener { buttonPopupBatal() }

        binding.btnPersiapanKelas.setOnClickListener { popUpSyarat() }

    }

    private fun imageSlide() {
        val reminder = ArrayList<SlideModel>()
        reminder.add(
            SlideModel(
                R.drawable.reminder1
            )
        )
        reminder.add(
            SlideModel(
                R.drawable.reminder2
            )
        )

        binding.sliderReminder.setImageList(reminder)
    }

    @SuppressLint("SetTextI18n")
    fun getDetailKelas() {
        val dataKelas = intent.getParcelableExtra<JadwalItem>("data")!!

        val jamMulai = dataKelas.scheduleTime.toString()
        val jamAkhir = dataKelas.scheduleEnd.toString()

        val jamStart = jamMulai.toDate().formatTo("HH:mm")
        val jamEnd = jamAkhir.toDate().formatTo("HH:mm")

        val tanggalKelas = jamMulai.toDate().formatTo("dd, MMMM yyyy")

        val urlPhoto = dataKelas.teacherAvatar.toString()

        val level = dataKelas.level.toString()
        if (level == "null") {
            binding.tvLevelKelas.text = "-"
        } else {
            binding.tvLevelKelas.text = level
        }
        if (dataKelas.sessionComplete.toString() == "null" && dataKelas.sessionAvailable.toString() == "null") {
            binding.tvSesiNow.text = "1/1"
        } else {
            binding.tvSesiNow.text = "${dataKelas.sessionComplete}/${dataKelas.sessionAvailable}"
        }

        binding.tvDetailKelas.text = dataKelas.packageName.toString()
        binding.tvJamKelas.text = "$jamStart-$jamEnd"
        binding.tvTanggalKelas.text = tanggalKelas
        binding.tvNamaGuru.text = dataKelas.teacherName.toString()

        if (dataKelas.teacherOrigin.toString() == "null") {
            binding.tvAsalGuru.visibility = View.INVISIBLE
        } else {
            binding.tvAsalGuru.text = dataKelas.teacherOrigin.toString()

        }

        if (urlPhoto == "null") {
            Picasso.get().load(R.drawable.ic_icon_person).into(binding.ivPhotoGuru)
        } else {
            Picasso.get().load(urlPhoto).into(binding.ivPhotoGuru)
        }


    }

    @SuppressLint("SetTextI18n")
    fun buttonPopupBatal() {
        val dataKelas = intent.getParcelableExtra<JadwalItem>("data")!!
        val tanggal = dataKelas.scheduleTime.toString()
        val kelas = dataKelas.packageName.toString()
        val tanggalConverted: String = tanggal.toDate().formatTo("dd, MMMM yyyy HH:mm")

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_up_batal_kelas)
        dialog.setCancelable(true)

        val tvApakahYakin = dialog.findViewById<TextView>(R.id.tv_apakah_yakin)
        val btnKembali = dialog.findViewById<Button>(R.id.btn_kembali_pop_up_batal)
        val btnBatal = dialog.findViewById<Button>(R.id.btn_batal_pop_up_batal)

        tvApakahYakin.text =
            "Apakah Anda yakin ingin membatalkan jadwal $kelas pada tanggal $tanggalConverted "
        btnKembali.setOnClickListener { dialog.dismiss() }

        btnBatal.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                batalJadwal(dialog)
            }
        }
        dialog.show()

    }

    fun popUpSyarat() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_up_persiapan)
        dialog.setCancelable(true)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        val btnMulaiKelas = dialog.findViewById<Button>(R.id.btn_masuk_kelas_pop_up)
        val ivCancel = dialog.findViewById<ImageView>(R.id.iv_cancel_popUp_masuk_kelas)
        ivCancel.setOnClickListener { dialog.dismiss() }

        btnMulaiKelas.setOnClickListener {

            alertDialogBuilder.setTitle("Persiapkan Laptop/Komputer Anda")

            alertDialogBuilder
                .setMessage("Untuk proses belajar, silahkan gunakan laptop dengan mengakses smartpi.id")
                .setCancelable(false)
                .setPositiveButton(
                    "Oke",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun String.toDate(
        dateFormat: String = "yyyy-MM-dd HH:mm:ss",
        timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    ): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

    @SuppressLint("SimpleDateFormat")
    fun checkJamMasuk() {

        val dataKelas = intent.getParcelableExtra<JadwalItem>("data")!!

        //ambil tanggal kelas
        val tanggal = dataKelas.scheduleTime.toString()
        val tanggalUser = tanggal.toDate().formatTo("yyyy-MM-dd HH:mm:ss")

        //ambil tanggal sekarang
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val tanggalNow = sdf.format(Date())

        //convert dari tanggal ke milisecoind
        val tanggalUserConverted = convertToMillis(tanggalUser)
        val tanggalNowConverted = convertToMillis(tanggalNow)

        val hasilTanggal = tanggalUserConverted - tanggalNowConverted
        val hasilDate = hasilTanggal / 3600000

        //jika jam kelas < 6 jam maka button batal hilang
        if (hasilDate <= 6) {
            binding.btnBatalJadwal.visibility = View.GONE
        } else {
            binding.btnBatalJadwal.visibility = View.VISIBLE
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun convertToMillis(tanggal: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(tanggal)
            timeInMilliseconds = mDate!!.time

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }

    suspend fun batalJadwal(dialog: Dialog) {
        val dataKelas = intent.getParcelableExtra<JadwalItem>("data")!!
        val idKelas = dataKelas.id.toString()

        val network = NetworkConfig().cancelSchedule().cancelJadwal(token, idKelas)

        if (network.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Kelas Berhasil Dibatalkan",
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }
            dialog.dismiss()
            finish()

        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Gagal Membatalkan Kelas",
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }


        }
    }
}