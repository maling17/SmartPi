package com.example.smartpi.view.kelas.filter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartpi.adapter.WaktuHariAdapter
import com.example.smartpi.databinding.FragmentWaktuKhususBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.PilihPaketActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WaktuKhususFragment : Fragment() {

    private var _binding: FragmentWaktuKhususBinding? = null
    private val binding get() = _binding!!
    lateinit var preferences: Preferences
    var jamList = ArrayList<String>()
    var tanggal = ""
    var jam = ""
    var timeZone = ""
    var kode_teacher = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWaktuKhususBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)

        val getUserAvailable = preferences.getValues("user_available_id")
        kode_teacher = preferences.getValues("kode_teacher")!!

        binding.rvSlotFilterKhusus.layoutManager = GridLayoutManager(context, 4)
        settingCalendar()
        isiArrayJam()

        binding.btnTampilkanKhusus.setOnClickListener {
            val waktu = "$tanggal $jam"
            val intent = Intent(context, PilihPaketActivity::class.java)
            intent.putExtra("waktu", waktu)
            intent.putExtra("timezone", timeZone)
            intent.putExtra("kode_teacher", kode_teacher)
            intent.putExtra("statusFilter", "2")
            intent.putExtra("user_available_id", getUserAvailable)
            startActivity(intent)

        }

    }

    fun isiArrayJam() {

        jamList.add("05:00")
        jamList.add("05:30")
        jamList.add("06:00")
        jamList.add("06:30")
        jamList.add("07:00")
        jamList.add("07:30")
        jamList.add("08:00")
        jamList.add("08:30")
        jamList.add("09:00")
        jamList.add("09:30")
        jamList.add("10:00")
        jamList.add("10:30")
        jamList.add("11:00")
        jamList.add("11:30")
        jamList.add("10:00")
        jamList.add("12:30")
        jamList.add("13:00")
        jamList.add("13:30")
        jamList.add("14:00")
        jamList.add("14:30")
        jamList.add("15:00")
        jamList.add("15:30")
        jamList.add("16:00")
        jamList.add("16:30")
        jamList.add("17:00")
        jamList.add("17:30")
        jamList.add("18:00")
        jamList.add("18:30")
        jamList.add("19:00")
        jamList.add("19:30")
        jamList.add("20:00")
        jamList.add("20:30")
        jamList.add("21:00")
        jamList.add("21:30")
        jamList.add("22:00")
        jamList.add("22:30")
        jamList.add("23:00")
        jamList.add("23:30")

        binding.rvSlotFilterKhusus.adapter = WaktuHariAdapter(jamList) {
            val indexArrayList = jamList.indexOf(it) //mengindex dulu isi yang ada di array
            jam = "${jamList[indexArrayList]}:00"  //mengfilter dan mengambil value dari kategori

        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun settingCalendar() {

        val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, -1)

        binding.calendarViewFilter.setMinimumDate(min)

        binding.calendarViewFilter.setOnDayClickListener { eventDay ->
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
            tanggal = curdate

        }
    }
}