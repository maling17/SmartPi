package com.example.smartpi.view.kelas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.adapter.SlotJamMultiAdapter
import com.example.smartpi.adapter.SlotJamSingleAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihJadwalBinding
import com.example.smartpi.model.*
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PilihJadwalActivity : AppCompatActivity() {

    private var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var availabilityList = ArrayList<AvailabilityItem>()
    private var jamList = ArrayList<AvailabilitySlotItem>()
    private var slotMap = HashMap<String, List<Any>>()

    private var scheduleList = ArrayList<Schedule?>()
    private var sortJamList = ArrayList<Schedule?>()
    var scheduleModel = ScheduleModel()
    var scheduleItem = Schedule()

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var user_avalaible_id = ""
    private var teacher_id = ""
    private var event_id = ""
    private var schedule_time = ""
    private var status = ""
    private var timeZone = ""
    var itemClicked = false
    var timeInMilliseconds: Long = 0
    var statusItem: Boolean = true

    private lateinit var binding: ActivityPilihJadwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihJadwalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        availabilityList.clear()

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()

        user_avalaible_id = intent.getStringExtra("user_avalaible_id").toString()
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"

        Log.d(TAG, "onCreate: $id  $token , $user_avalaible_id ")
        binding.rvSlot.layoutManager = GridLayoutManager(this, 4)

        scope.async {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val job1 = async { getJadwalGuru() }
                val job2 = async { checkSession() }

                job1.await()
                job2.await()
            }
        }

        calendarSesi1()

        binding.btnSesi1.setOnClickListener {
            changeBackgroundButtonSesi(binding.btnSesi1, binding.btnSesi2)
            binding.rvSlot.visibility = View.GONE
            itemClicked = false
            changeBackgroundButtonSesi1()
            scope.launch(Dispatchers.Main) { calendarSesi1() }
        }
        binding.btnSesi2.setOnClickListener {
            changeBackgroundButtonSesi(binding.btnSesi2, binding.btnSesi1)
            binding.rvSlot.visibility = View.GONE
            itemClicked = false
            changeBackgroundButtonSesi2()
            scope.launch(Dispatchers.Main) { calendarSesi2() }
        }
        binding.ivBackPilihJadwal.setOnClickListener { finish() }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getJadwalGuru() {

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()
        val events: ArrayList<EventDay> = ArrayList()

        binding.tvNamaTeacherPilihJadwal.text = data.name.toString()
        Picasso.get().load(data.avatar.toString()).into(binding.ivTeacherPilihJadwal)

        jamList.clear()
        val network = NetworkConfig().getTeacher().getTeacherSchedule(token, id)
        if (network.isSuccessful) {
            for (schedule in network.body()!!.availability!!) {
                val justTanggal = schedule!!.start!!.toDate().formatTo("yyyy-MM-dd")
                //convert tanggal start ke millis
                val tanggalSlot = schedule.start!!.toDate().formatTo("yyyy-MM-dd HH:mm")
                val hari: Int = schedule.start.toDate().formatTo("dd").toInt()
                val bulan: Int = schedule.start.toDate().formatTo("MM").toInt()
                val tahun: Int = schedule.start.toDate().formatTo("yyyy").toInt()
                val tanggalInMillis = convertToMillis(tanggalSlot)

                //ambil tanggal sekarang
                val myFormat = "yyyy-MM-dd HH:mm"
                val calendar = Calendar.getInstance()
                val time = calendar.time
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val curdate = sdf.format(time) //diconvert ke tanggal local
                val curDateinMillis = convertToMillis(curdate)

                val hasilDate = tanggalInMillis - curDateinMillis
                val tanggalJam = hasilDate / 3600000 //diubah dari millis ke jam
                if (tanggalJam >= 6) {
                    if (schedule.start.toDate()
                            .formatTo("yyyy-MM-dd") == justTanggal && schedule.status == 0
                    ) {
                        availabilityList.add(schedule)

//                        calendar.add(Calendar.DAY_OF_MONTH, availabilityList.size)
                        /*  val cal:Calendar = DateUtils.getCalendar()
                          cal.add(Calendar.DAY_OF_MONTH, 2)
                          val calendars: ArrayList<Calendar> = ArrayList()
                          calendars.add(cal)
                          binding.calendarViewSesi1.setHighlightedDays(calendars)*/

                        Log.d(TAG, "getJadwalGuru: $availabilityList")
//                        events.add(EventDay(cal, DrawableUtils.getCircleDrawable(this)))
                        binding.calendarViewSesi1.setEvents(events)
                    }

                }

            }

            if (availabilityList.isEmpty()) {

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Jadwal tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            Log.d(TAG, "getJadwalGuru: GAGAL")
        }


    }

    private fun calendarSesi1() {

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()
        val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, -1)

        binding.calendarViewSesi1.setMinimumDate(min)

        binding.calendarViewSesi1.setOnDayClickListener { eventDay ->
            binding.rvSlot.visibility = View.VISIBLE
            val clickedDayCalendar = eventDay.calendar.time
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )
            val currentLocalTime = calendar.time
            val date: DateFormat = SimpleDateFormat("z", Locale.getDefault())
            val localTime: String = date.format(currentLocalTime)
            timeZone = localTime.drop(3)
            eventDay.isEnabled

            val myFormat = "yyyy-MM-dd" // format tanggal
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            val curdate = sdf.format(clickedDayCalendar) //diconvert ke tanggal local

            itemClicked = false
            changeBackgroundButtonSesi1()
            scope.launch(Dispatchers.Main) { getSlotJadwal(id, curdate, timeZone) }
        }
    }

    private fun calendarSesi2() {

        val data = intent.getParcelableExtra<TeacherItem>("data")
        val id = data.id.toString()

        val min = Calendar.getInstance()

        min.add(Calendar.DAY_OF_MONTH, -1)

        binding.calendarViewSesi1.setMinimumDate(min)

        binding.calendarViewSesi1.setOnDayClickListener { eventDay ->

            binding.rvSlot.visibility = View.VISIBLE
            val clickedDayCalendar = eventDay.calendar.time
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )
            val currentLocalTime = calendar.time
            val date: DateFormat = SimpleDateFormat("z", Locale.getDefault())
            val localTime: String = date.format(currentLocalTime)
            timeZone = localTime.drop(3)

            eventDay.isEnabled

            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            val curdate = sdf.format(clickedDayCalendar) //diconvert ke tanggal local

            itemClicked = false
            changeBackgroundButtonSesi2()
            scope.launch(Dispatchers.Main) { getMultiSlotJadwal(id, curdate, timeZone) }
        }
    }

    private suspend fun getSlotJadwal(id: String, date: String, timeZone: String) {

        jamList.clear()
        val networkConfig =
            NetworkConfig().getTeacher().getTeacherScheduleAvailability(token, id, date, timeZone)

        if (networkConfig.isSuccessful) {

            if (networkConfig.body()!!.availability!!.isEmpty()) {

                binding.rvSlot.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Jam tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                for (slot in networkConfig.body()!!.availability!!) {

                    //convert tanggal start ke millis
                    val tanggalSlot = slot!!.start!!.toDate().formatTo("yyyy-MM-dd HH:mm")
                    val tanggalInMillis = convertToMillis(tanggalSlot)

                    //ambil tanggal sekarang
                    val myFormat = "yyyy-MM-dd HH:mm"
                    val calendar = Calendar.getInstance()
                    val time = calendar.time
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    val curdate = sdf.format(time) //diconvert ke tanggal local
                    val curDateinMillis = convertToMillis(curdate)

                    val hasilDate = tanggalInMillis - curDateinMillis
                    val tanggalJam = hasilDate / 3600000 //diubah dari millis ke jam

                    if (tanggalJam >= 6) {
                        jamList.add(slot)
                        val sortJamList = jamList.sortedBy { jamList -> jamList.start }
                        binding.rvSlot.visibility = View.VISIBLE

                        val singleAdapter = SlotJamSingleAdapter(sortJamList) {
                            teacher_id = it.teacherId.toString()
                            schedule_time = it.start.toString()
                            status = "1"
                            event_id = it.id.toString()
                            itemClicked = true

                            changeBackgroundButtonSesi1()
                        }
                        binding.rvSlot.adapter = singleAdapter

                    } else {
                        binding.rvSlot.visibility = View.GONE
                    }

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

    private suspend fun getMultiSlotJadwal(id: String, date: String, timeZone: String) {
        jamList.clear()
        val networkConfig =
            NetworkConfig().getTeacher().getTeacherScheduleAvailability(token, id, date, timeZone)

        if (networkConfig.isSuccessful) {

            if (networkConfig.body()!!.availability!!.isEmpty()) {

                binding.rvSlot.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Jam tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                for (slot in networkConfig.body()!!.availability!!) {

                    //convert tanggal start ke millis
                    val tanggalSlot = slot!!.start!!.toDate().formatTo("yyyy-MM-dd HH:mm")
                    val tanggalInMillis = convertToMillis(tanggalSlot)

                    //ambil tanggal sekarang
                    val myFormat = "yyyy-MM-dd HH:mm" // format tanggal
                    val calendar = Calendar.getInstance()
                    val time = calendar.time
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    val curdate = sdf.format(time) //diconvert ke tanggal local
                    val curDateinMillis = convertToMillis(curdate) // convert ke millis

                    val hasilDate = tanggalInMillis - curDateinMillis
                    val tanggalJam = hasilDate / 3600000 //diubah dari millis ke jam

                    if (tanggalJam >= 6) {
                        jamList.add(slot)
                        val sortJamList = jamList.sortedBy { jamList -> jamList.start }
                        binding.rvSlot.visibility = View.VISIBLE

                        val adapter = SlotJamMultiAdapter(sortJamList) {
                            val scheduleData = Schedule()
                            scheduleModel.userAvailableId = user_avalaible_id
                            scheduleModel.teacherId = it.teacherId.toString()

                            scheduleData.scheduleTime = it.start.toString()
                            scheduleData.status = "1"
                            scheduleData.eventId = it.id.toString()

                            //dicek jika di array ada yg sama dengan data yg diklik
                            if (scheduleList.contains(scheduleData)) {
                                scheduleList.remove(scheduleData)

                                if (scheduleList.isEmpty()) {
                                    itemClicked = false
                                    Log.d(TAG, "getMultiSlotJadwal: $scheduleList")
                                    changeBackgroundButtonSesi2()

                                } else {
                                    itemClicked = true
                                    Log.d(TAG, "getMultiSlotJadwal: $scheduleList")
                                    changeBackgroundButtonSesi2()
                                }

                            } else {
                                scheduleList.add(scheduleData)

                                Log.d(TAG, "getMultiSlotJadwal: $scheduleList")
                                itemClicked = true
                                changeBackgroundButtonSesi2()
                            }
                            scheduleModel.schedule = scheduleList

                        }

                        binding.rvSlot.adapter = adapter
                    } else {
                        binding.rvSlot.visibility = View.GONE
                    }

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

    private suspend fun createSchedule1Sesi() {

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

    private suspend fun createSchedule2Sesi() {

        val networkConfig = NetworkConfig().createSchedule().createScheduleSesi2(
            token,
            scheduleModel
        )
        try {
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
        } catch (e: Exception) {
            Log.e(TAG, "createSchedule2Sesi: ${e.message}")
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun convertToMillis(tanggal: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
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

    private fun changeBackgroundButtonSesi1() {
        if (!itemClicked) {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.grey))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.dark_grey))
            binding.btnKonfirmasiPilihJadwal.isEnabled = false
        } else {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.yellow))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.white))
            binding.btnKonfirmasiPilihJadwal.isEnabled = true

            binding.btnKonfirmasiPilihJadwal.setOnClickListener {

                scope.launch(Dispatchers.Main) {
                    createSchedule1Sesi()
                    Log.d(TAG, "onCreate: $event_id $user_avalaible_id $schedule_time")
                }

            }
        }

    }

    private fun changeBackgroundButtonSesi2() {
        if (!itemClicked) {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.grey))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.dark_grey))
            binding.btnKonfirmasiPilihJadwal.isEnabled = false
        } else {
            binding.btnKonfirmasiPilihJadwal.setBackgroundColor(resources.getColor(R.color.yellow))
            binding.btnKonfirmasiPilihJadwal.setTextColor(resources.getColor(R.color.white))
            binding.btnKonfirmasiPilihJadwal.isEnabled = true

            binding.btnKonfirmasiPilihJadwal.setOnClickListener {


                scope.launch(Dispatchers.Main) { createSchedule2Sesi() }

            }
        }

    }

    private fun changeBackgroundButtonSesi(buttonBlue: Button, buttonGrey: Button) {

        scope.launch(Dispatchers.Main) {
            val job1 = async {
                buttonBlue.setBackgroundColor(resources.getColor(R.color.light_blue))
                buttonBlue.setTextColor(resources.getColor(R.color.white))
            }
            val job2 = async {
                buttonGrey.setBackgroundColor(resources.getColor(R.color.grey))
                buttonGrey.setTextColor(resources.getColor(R.color.dark_grey))
            }

            job1.await()
            job2.await()
        }

    }
}