package com.example.smartpi.view.kelas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListHistoryAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentHistoryBinding
import com.example.smartpi.model.HistoryItem
import com.example.smartpi.utils.Preferences
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {

    private var token = ""
    lateinit var preferences: Preferences
    private var historyList = ArrayList<HistoryItem>()
    private val TAG = "MyActivity"
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var layoutManager: LinearLayoutManager
    private var page = 2
    private var totalPage: Int = 1
    private var isLoading = false
    var isiRadioButton = ""
    var getScheduleId = ""
    var feedback = ""
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = Preferences(requireContext())
        token = "Bearer ${preferences.getValues("token")}"
        hideBottomSheet()
        binding.pbHistory.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(context)

        /* binding.rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
             override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 Log.d("MainActivity", "onScrollChange: ")
                 val visibleItemCount = layoutManager.childCount
                 val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                 val total = ListHistoryAdapter(historyList) {}.itemCount
                 if (!isLoading && page < totalPage) {
                     if (visibleItemCount + pastVisibleItem >= total) {
                         page++
                         scope.launch(Dispatchers.Main) {
                             getHistoryNext()
                         }
                     }
                 }
                 super.onScrolled(recyclerView, dx, dy)
             }
         })*/

        binding.btnLoadmore.setOnClickListener {
            page++
            binding.pbLoading.visibility = View.VISIBLE
            binding.btnLoadmore.visibility = View.GONE
            scope.launch {
                getHistoryNext()
            }
        }
        scope.launch(Dispatchers.Main) {
            getHistory()
        }

    }

    private suspend fun getHistory() {
        historyList.clear()
        isLoading = true

        val parameters = HashMap<String, String>()
        parameters["page"] = page.toString()
        Log.d("PAGE", "$page")

        //untuk inisialiasi sliding up layout
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val bottomSheetBehaviorAlasan = BottomSheetBehavior.from(binding.llBermasalah)

        val networkConfig = NetworkConfig().getAfterClass().getHistory(token)

        try {
            if (networkConfig.isSuccessful) {
                for (history in networkConfig.body()!!.data!!) {
                    historyList.add(history!!)
                }
                binding.btnLoadmore.visibility = View.VISIBLE
                binding.rvHistory.adapter = ListHistoryAdapter(historyList) {

                    binding.tvNamaTeacherHistory.text = it.teacherName
                    binding.tvAsalGuruHistory.text = it.teacherOrigin
                    binding.tvNamaPaketHistory.text = it.packageName
                    binding.tvTanggalHistory.text =
                        it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
                    feedback = it.teacherFeedback.toString()
                    //cek status kalau 3 = sudah selesai , 6 = bermasalah
                    when (it.status) {
                        3 -> {

                            if (it.teacherRate.toString() == "null") { //cek sudah ada rating belum?
                                val alertDialogBuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(context)

                                alertDialogBuilder.setTitle("Kelas Selesai?")
                                alertDialogBuilder
                                    .setMessage("Apakah kelas sudah selesai?")
                                    .setCancelable(false)
                                    .setPositiveButton(
                                        "Sudah"
                                    ) { dialog, _ ->
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_EXPANDED

                                        binding.tvKelasBermasalah.visibility = View.GONE
                                        binding.clKesan.visibility = View.VISIBLE
                                        binding.btnSimpanHistory.visibility = View.VISIBLE
                                        binding.btnLihatFeedback.visibility = View.VISIBLE

                                        binding.rbGuru.isEnabled = true
                                        binding.rbGuru.isClickable = true
                                        binding.rbGuru.rating = 0f
                                        getScheduleId = it.id.toString()
                                        Picasso.get().load(it.teacherAvatar)
                                            .into(binding.ivTeacherHistory)

                                        binding.btnSimpanHistory.setOnClickListener {
                                            val etKesan = binding.etKesan.text
                                            val getRating = binding.rbGuru.rating

                                            scope.launch {      //api untuk rate kelas
                                                rateKelas(
                                                    getScheduleId,
                                                    getRating,
                                                    etKesan.toString()
                                                )
                                                getHistory()
                                            }
                                        }
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                        "Belum"
                                    ) { dialog, _
                                        ->
                                        bottomSheetBehaviorAlasan.state =
                                            BottomSheetBehavior.STATE_EXPANDED

                                        // untuk munculkan dan menghilangkan edit text alasan
                                        binding.rbLainnya.setOnClickListener {
                                            binding.etAlasan.visibility = View.VISIBLE
                                        }
                                        binding.rbJaringanBermasalah.setOnClickListener {
                                            binding.etAlasan.visibility = View.GONE
                                        }
                                        binding.rbGuruTidakHadir.setOnClickListener {
                                            binding.etAlasan.visibility = View.GONE
                                        }

                                        getScheduleId = it.id.toString()

                                        binding.btnSimpanHistoryBermasasalah.setOnClickListener {

                                            val intSelectButton =
                                                binding.rgAlasan.checkedRadioButtonId
                                            val radioButton: RadioButton =
                                                binding.root.findViewById(intSelectButton)

                                            when (radioButton.text.toString()) {
                                                "Jaringan bermasalah" -> isiRadioButton =
                                                    radioButton.text.toString()
                                                "Guru tidak hadir" -> isiRadioButton =
                                                    radioButton.text.toString()
                                                "Lainnya" -> isiRadioButton =
                                                    binding.etAlasan.text.toString()
                                            }

                                            scope.launch {
                                                inputKelasBermasalah(
                                                    getScheduleId,
                                                    isiRadioButton
                                                )
                                                getHistory()
                                            }
                                        }

                                        dialog.dismiss()
                                    }
                                val alertDialog: AlertDialog = alertDialogBuilder.create()
                                alertDialog.show()

                            } else {

                                // untuk kelas yang sudah dirate
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvKelasBermasalah.visibility = View.GONE
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.VISIBLE
                                Picasso.get().load(it.teacherAvatar)
                                    .into(binding.ivTeacherHistory)
                                binding.rbGuru.isEnabled = false


                                binding.btnLihatFeedback.setOnClickListener {
                                    val intent =
                                        Intent(activity, LihatFeedbackActivity::class.java)
                                    intent.putExtra("feedback", feedback)
                                    Log.d(TAG, "getHistory: $feedback")
                                    startActivity(intent)
                                }

                                if (it.teacherRate.toString() == "null") {
                                    binding.rbGuru.rating = 0f
                                } else {
                                    binding.rbGuru.rating = it.teacherRate!!.toFloat()
                                }
                            }

                        }
                        6 -> {
                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {

                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvKelasBermasalah.visibility = View.VISIBLE
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.GONE
                                Picasso.get().load(it.teacherAvatar)
                                    .into(binding.ivTeacherHistory)
                                binding.tvLabelFeedback.visibility = View.GONE
                                binding.rbGuru.rating = 0f
                                binding.rbGuru.isEnabled = false

                            } else {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                        }
                    }

                }
                binding.pbHistory.visibility = View.GONE
            } else {
                binding.clHistoryEmpty.visibility = View.VISIBLE
                binding.pbHistory.visibility = View.GONE
                isLoading = false

                Log.d(TAG, "getHistory: History Gagal diambil")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getHistory: ${e.message}")
        }

    }

    private suspend fun getHistoryNext() {
        isLoading = true

        val parameters = HashMap<String, String>()
        parameters["page"] = page.toString()
        Log.d("PAGE", "$page")
        //untuk inisialiasi sliding up layout
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val bottomSheetBehaviorAlasan = BottomSheetBehavior.from(binding.llBermasalah)

        val networkConfig = NetworkConfig().getAfterClass().getHistoryNextPage(token, parameters)
        try {
            if (networkConfig.isSuccessful) {
                binding.pbLoading.visibility = View.GONE
                binding.btnLoadmore.visibility = View.VISIBLE
                for (history in networkConfig.body()!!.data!!) {
                    historyList.add(history!!)
                }
                binding.btnLoadmore.visibility = View.VISIBLE
                binding.rvHistory.adapter = ListHistoryAdapter(historyList) {

                    binding.tvNamaTeacherHistory.text = it.teacherName
                    binding.tvAsalGuruHistory.text = it.teacherOrigin
                    binding.tvNamaPaketHistory.text = it.packageName
                    binding.tvTanggalHistory.text =
                        it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
                    feedback = it.teacherFeedback.toString()
                    //cek status kalau 3 = sudah selesai , 6 = bermasalah
                    when (it.status) {
                        3 -> {

                            if (it.teacherRate.toString() == "null") { //cek sudah ada rating belum?
                                val alertDialogBuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(context)

                                alertDialogBuilder.setTitle("Kelas Selesai?")
                                alertDialogBuilder
                                    .setMessage("Apakah kelas sudah selesai?")
                                    .setCancelable(false)
                                    .setPositiveButton(
                                        "Sudah"
                                    ) { dialog, _ ->
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_EXPANDED

                                        binding.tvKelasBermasalah.visibility = View.GONE
                                        binding.clKesan.visibility = View.VISIBLE
                                        binding.btnSimpanHistory.visibility = View.VISIBLE
                                        binding.btnLihatFeedback.visibility = View.VISIBLE

                                        binding.rbGuru.isEnabled = true
                                        binding.rbGuru.isClickable = true
                                        binding.rbGuru.rating = 0f
                                        getScheduleId = it.id.toString()
                                        Picasso.get().load(it.teacherAvatar)
                                            .into(binding.ivTeacherHistory)

                                        binding.btnSimpanHistory.setOnClickListener {
                                            val etKesan = binding.etKesan.text
                                            val getRating = binding.rbGuru.rating

                                            scope.launch {      //api untuk rate kelas
                                                rateKelas(
                                                    getScheduleId,
                                                    getRating,
                                                    etKesan.toString()
                                                )
                                                getHistory()
                                            }
                                        }
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(
                                        "Belum"
                                    ) { dialog, _
                                        ->
                                        bottomSheetBehaviorAlasan.state =
                                            BottomSheetBehavior.STATE_EXPANDED

                                        // untuk munculkan dan menghilangkan edit text alasan
                                        binding.rbLainnya.setOnClickListener {
                                            binding.etAlasan.visibility = View.VISIBLE
                                        }
                                        binding.rbJaringanBermasalah.setOnClickListener {
                                            binding.etAlasan.visibility = View.GONE
                                        }
                                        binding.rbGuruTidakHadir.setOnClickListener {
                                            binding.etAlasan.visibility = View.GONE
                                        }

                                        getScheduleId = it.id.toString()

                                        binding.btnSimpanHistoryBermasasalah.setOnClickListener {

                                            val intSelectButton =
                                                binding.rgAlasan.checkedRadioButtonId
                                            val radioButton: RadioButton =
                                                binding.root.findViewById(intSelectButton)

                                            when (radioButton.text.toString()) {
                                                "Jaringan bermasalah" -> isiRadioButton =
                                                    radioButton.text.toString()
                                                "Guru tidak hadir" -> isiRadioButton =
                                                    radioButton.text.toString()
                                                "Lainnya" -> isiRadioButton =
                                                    binding.etAlasan.text.toString()
                                            }

                                            scope.launch {
                                                inputKelasBermasalah(
                                                    getScheduleId,
                                                    isiRadioButton
                                                )
                                                getHistory()
                                            }
                                        }

                                        dialog.dismiss()
                                    }
                                val alertDialog: AlertDialog = alertDialogBuilder.create()
                                alertDialog.show()

                            } else {

                                // untuk kelas yang sudah dirate
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvKelasBermasalah.visibility = View.GONE
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.VISIBLE
                                Picasso.get().load(it.teacherAvatar)
                                    .into(binding.ivTeacherHistory)
                                binding.rbGuru.isEnabled = false


                                binding.btnLihatFeedback.setOnClickListener {
                                    val intent =
                                        Intent(activity, LihatFeedbackActivity::class.java)
                                    intent.putExtra("feedback", feedback)
                                    Log.d(TAG, "getHistory: $feedback")
                                    startActivity(intent)
                                }

                                if (it.teacherRate.toString() == "null") {
                                    binding.rbGuru.rating = 0f
                                } else {
                                    binding.rbGuru.rating = it.teacherRate!!.toFloat()
                                }
                            }

                        }
                        6 -> {
                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {

                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvKelasBermasalah.visibility = View.VISIBLE
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.GONE
                                Picasso.get().load(it.teacherAvatar)
                                    .into(binding.ivTeacherHistory)
                                binding.tvLabelFeedback.visibility = View.GONE
                                binding.rbGuru.rating = 0f
                                binding.rbGuru.isEnabled = false

                            } else {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                        }
                    }

                }
                binding.pbHistory.visibility = View.GONE
            } else {
                binding.pbHistory.visibility = View.GONE
                binding.pbLoading.visibility = View.GONE
                binding.btnLoadmore.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Sudah page terakhir",
                        Toast.LENGTH_LONG
                    ).show()
                }
                isLoading = false

                Log.d(TAG, "getHistory: Page Terakhir")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getHistory: ${e.message}")
        }
    }

    private fun hideBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val bottomSheetBehaviorAlasan = BottomSheetBehavior.from(binding.llBermasalah)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviorAlasan.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
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

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })


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

    private suspend fun rateKelas(scheduleId: String, rateTeacher: Float, feedback: String) {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val networkConfig =
            NetworkConfig().getAfterClass().rateClass(token, scheduleId, rateTeacher, feedback)
        try {
            if (networkConfig.isSuccessful) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Berhasil rate kelas",
                        Toast.LENGTH_LONG
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Gagal Rate Kelas",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    private suspend fun inputKelasBermasalah(scheduleId: String, alasan: String) {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.llBermasalah)

        val networkConfig =
            NetworkConfig().getAfterClass().inputKelasBermasalah(token, scheduleId, alasan)
        try {
            if (networkConfig.isSuccessful) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Berhasil input kelas bermasalah",
                        Toast.LENGTH_LONG
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Gagal  input kelas bermasalah",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}
