package com.example.smartpi.view.kelas

import android.app.AlertDialog
import android.content.DialogInterface
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class HistoryFragment : Fragment() {

    private var token = ""
    lateinit var preferences: Preferences
    private var historyList = ArrayList<HistoryItem>()
    private val TAG = "MyActivity"
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    lateinit var listViewType: ArrayList<Int>
    var countLoadMore by Delegates.notNull<Int>()
    var isLoading by Delegates.notNull<Boolean>()
    var isiRadioButton = ""
    var getScheduleId = ""

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
        preferences = Preferences(context!!)
        token = "Bearer ${preferences.getValues("token")}"

        hideBottomSheet()
        binding.pbHistory.visibility = View.VISIBLE

        scope.launch(Dispatchers.Main) {
            getHistory()
        }

    }

    private suspend fun getHistory() {
        historyList.clear()

        //untuk inisialiasi sliding up layout
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val bottomSheetBehaviorAlasan = BottomSheetBehavior.from(binding.llBermasalah)

        val networkConfig = NetworkConfig().getAfterClass().getHistory(token)

        try {
            if (networkConfig.isSuccessful) {
                binding.pbHistory.visibility = View.VISIBLE

                for (history in networkConfig.body()!!.data!!) {
                    historyList.add(history!!)
                }

                binding.rvHistory.adapter = ListHistoryAdapter(historyList) {

                    binding.tvNamaTeacherHistory.text = it.teacherName
                    binding.tvAsalGuruHistory.text = it.teacherOrigin
                    binding.tvNamaPaketHistory.text = it.packageName
                    binding.tvTanggalHistory.text =
                        it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")

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
                                        "Sudah",
                                        DialogInterface.OnClickListener { dialog, _ ->
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
                                        })
                                    .setNegativeButton("Belum",
                                        DialogInterface.OnClickListener { dialog, _
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
                                        })
                                val alertDialog: AlertDialog = alertDialogBuilder.create()
                                alertDialog.show()

                            } else {

                                // untuk kelas yang sudah dirate
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvKelasBermasalah.visibility = View.GONE
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.VISIBLE
                                Picasso.get().load(it.teacherAvatar).into(binding.ivTeacherHistory)
                                binding.rbGuru.isEnabled = false

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
                                Picasso.get().load(it.teacherAvatar).into(binding.ivTeacherHistory)
                                binding.tvLabelFeedback.visibility = View.GONE
                                binding.rbGuru.rating = 0f
                                binding.rbGuru.isEnabled = false

                            } else {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                        }
                    }

                }
            } else {
                binding.clHistoryEmpty.visibility = View.VISIBLE
                Log.d(TAG, "getHistory: History Gagal diambil")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getHistory: ${e.message}")
        }

    }

    private fun hideBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val bottomSheetBehaviorAlasan = BottomSheetBehavior.from(binding.llBermasalah)

        binding.btnLihatFeedback.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
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
    }

    private suspend fun inputKelasBermasalah(scheduleId: String, alasan: String) {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.llBermasalah)

        val networkConfig =
            NetworkConfig().getAfterClass().inputKelasBermasalah(token, scheduleId, alasan)
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

    }
}
