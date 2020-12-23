package com.example.smartpi.view.kelas.filter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartpi.adapter.jadwal.WaktuHariAdapter
import com.example.smartpi.databinding.FragmentWaktuUmumBinding
import com.example.smartpi.model.FilterUmum
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.PilihPaketActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WaktuUmumFragment : Fragment() {

    val waktuList = ArrayList<String>()
    val hariList = ArrayList<String>()
    lateinit var preferences: Preferences

    var filterUmum = FilterUmum()

    private var _binding: FragmentWaktuUmumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaktuUmumBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isiArrayWaktuDanHari()
        preferences = Preferences(requireActivity().applicationContext)
        val getUserAvailable = preferences.getValues("user_available_id")

        // menampilkan rentang waktu yang akan diambil
        binding.rvRentangWaktuUmum.layoutManager = GridLayoutManager(context, 3)
        // menampilkan hari yang akan diambil
        binding.rvHariUmum.layoutManager = GridLayoutManager(context, 3)

        binding.rvRentangWaktuUmum.adapter = WaktuHariAdapter(waktuList) {
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )
            val currentLocalTime = calendar.time
            val date: DateFormat = SimpleDateFormat("z", Locale.getDefault())
            val localTime: String = date.format(currentLocalTime)

            filterUmum.time_zone = localTime.drop(3) //mengambil timezone cth: +07:00
            filterUmum.range_end = it.drop(3)  //mengambil value rentang waktu akhir
            filterUmum.range_start = it.dropLast(3) //mengambil value rentang waktu mulai

        }

        binding.rvHariUmum.adapter = WaktuHariAdapter(hariList) {
            filterUmum.hari = it
            filterUmum.kode_teacher = preferences.getValues("kode_teacher")
        }

        binding.btnTampilkanUmum.setOnClickListener {
            val intent = Intent(activity, PilihPaketActivity::class.java)
            intent.putExtra("filter", filterUmum)
            intent.putExtra("statusFilter", "1")
            intent.putExtra("user_available_id", getUserAvailable)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    fun isiArrayWaktuDanHari() {
        waktuList.clear()
        waktuList.add("00-04")
        waktuList.add("04-08")
        waktuList.add("08-12")
        waktuList.add("12-16")
        waktuList.add("16-20")
        waktuList.add("20-24")

        hariList.clear()
        hariList.add("Sunday")
        hariList.add("Monday")
        hariList.add("Tuesday")
        hariList.add("Wednesday")
        hariList.add("Thursday")
        hariList.add("Friday")
        hariList.add("Saturday")
    }
}