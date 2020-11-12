package com.example.smartpi.view.kelas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.adapter.SlotJamAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihJadwalBinding
import com.example.smartpi.model.AvailabilityItem
import com.example.smartpi.model.AvailabilitySlotItem
import com.example.smartpi.model.TeacherItem
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PilihJadwalActivity : AppCompatActivity() {

    lateinit var selectedDates: List<Calendar>
    var events: List<EventDay> = ArrayList()
    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var availabilityList = ArrayList<AvailabilityItem>()
    private var jamList = ArrayList<AvailabilitySlotItem>()
    private var slotMap = HashMap<String, List<Any>>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    var user_avalaible_id = ""
    var teacher_id = ""
    var event_id = ""
    var schedule_time = ""
    var status = ""
    var itemClicked = false
    var timeInMilliseconds: Long = 0

    private lateinit var binding: ActivityPilihJadwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihJadwalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        availabilityList.clear()

        user_avalaible_id = intent.getStringExtra("user_avalaible_id").toString()
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        binding.rvSlot.layoutManager = GridLayoutManager(this, 4)

        scope.async {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val job1 = async { getJadwalGuru() }
                val job2 = async { checkSession() }

                job1.await()
                job2.await()
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getJadwalGuru() {

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()

        binding.tvNamaTeacherPilihJadwal.text = data.name.toString()
        Picasso.get().load(data.avatar.toString()).into(binding.ivTeacherPilihJadwal)

        jamList.clear()
        val network = NetworkConfig().getTeacher().getTeacherSchedule(token, id)
        if (network.isSuccessful) {
            for (schedule in network.body()!!.availability!!) {
                availabilityList.add(schedule!!)
            }
            if (availabilityList.isEmpty()) {

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Jadwal tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                calendarSesi1()
            }

        } else {
            Log.d(TAG, "getJadwalGuru: GAGAL")
        }


    }

    private fun calendarSesi1() {

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()

        val calendars: List<Calendar> = ArrayList()
        val tanggalStat = availabilityList[0].start.toString()
        val hariStart = tanggalStat.toDate().formatTo("dd").toInt()
        val bulanStart = tanggalStat.toDate().formatTo("MM").toInt()
        val tahunStart = tanggalStat.toDate().formatTo("yyyy").toInt()

//        calendar_view_sesi_1.setEvents(events)
        val min = Calendar.getInstance()
        min.set(tahunStart, bulanStart - 1, hariStart - 1)
        binding.calendarViewSesi1.setMinimumDate(min)

        binding.calendarViewSesi1.selectedDates = calendars

        binding.calendarViewSesi1.setDisabledDays(calendars)
        binding.calendarViewSesi1.setHighlightedDays(calendars)

        binding.calendarViewSesi1.setOnDayClickListener { eventDay ->
            val clickedDayCalendar = eventDay.calendar.time
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            val curdate = sdf.format(clickedDayCalendar)
            itemClicked = false
            changeBackgroundButton()
            scope.launch(Dispatchers.Main) { getSlotJadwal(id, curdate) }
        }
    }

    private suspend fun getSlotJadwal(id: String, date: String) {

        jamList.clear()
        val networkConfig =
            NetworkConfig().getTeacher().getTeacherScheduleAvailability(token, id, date)

        if (networkConfig.isSuccessful) {

            if (networkConfig.body()!!.availability!!.isEmpty()) {

                binding.rvSlot.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Jadwal tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                for (slot in networkConfig.body()!!.availability!!) {

                    jamList.add(slot!!)
                    val sortJamList = jamList.sortedBy { jamList -> jamList.start }

                    binding.rvSlot.visibility = View.VISIBLE

                    //ambil tanggal schedule
                    val tanggal = slot.start.toString()
                    val tanggalUser = tanggal.toDate().formatTo("yyyy-MM-dd HH:mm:ss")

                    //ambil tanggal sekarang
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val tanggalNow = sdf.format(Date())

                    //convert dari tanggal ke milisecoind
                    val tanggalUserConverted = convertToMillis(tanggalUser)
                    val tanggalNowConverted = convertToMillis(tanggalNow)

                    val hasilTanggal = tanggalUserConverted - tanggalNowConverted
                    val hasilDate = hasilTanggal / 3600000

                    //jika jam kelas < 6 jam maka jadwal tidak ada
                    if (hasilDate <= 6) {
                        binding.rvSlot.visibility = View.GONE
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                this,
                                "Minimal 6 jam sebelum membuat jadwal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.rvSlot.visibility = View.VISIBLE
                    }

                    binding.rvSlot.adapter = SlotJamAdapter(sortJamList) {
                        teacher_id = it.teacherId.toString()
                        schedule_time = it.start.toString()
                        status = "1"
                        event_id = it.id.toString()
                        itemClicked = true

                        changeBackgroundButton()
                    }
                    Log.d(TAG, "getSlotJadwal: ${slot.start!!.toDate().formatTo("dd-MM-yyyy")}")
                }
            }

        } else {

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Jam tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun checkSession() {

        val networkConfig = NetworkConfig().checkSession().checkSession(token, user_avalaible_id)

        if (networkConfig.isSuccessful) {
            if (networkConfig.body()!!.data!!.available == "1") {
                binding.llSesi2.visibility = View.GONE
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Cek sesi gagal",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun createScheduleSesi() {

        val networkConfig = NetworkConfig().createSchedule().createScheduleSesi1(
            token,
            user_avalaible_id,
            teacher_id,
            event_id,
            schedule_time,
            status
        )

        if (networkConfig.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Pembuatan Jadwal Berhasil",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Pembuatan Jadwal Gagal, Cek Koneksi",
                    Toast.LENGTH_LONG
                ).show()
            }
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

    private fun String.toDate(
        dateFormat: String = "yyyy-MM-dd HH:mm:ss",
        timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    ): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    private fun Date.formatTo(
        dateFormat: String,
        timeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

    private fun changeBackgroundButton() {
        if (!itemClicked) {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.grey))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.dark_grey))
            binding.btnKonfirmasiPilihJadwal.isEnabled = false
        } else {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.yellow))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.white))
            binding.btnKonfirmasiPilihJadwal.isEnabled = true

            binding.btnKonfirmasiPilihJadwal.setOnClickListener {

                scope.launch(Dispatchers.Main) { createScheduleSesi() }
                Log.d(TAG, "onCreate: $event_id")
            }
        }

    }

}