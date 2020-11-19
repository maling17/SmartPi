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
        isLoading = false
        listViewType = ArrayList()
        countLoadMore = 0


        hideBottomSheet()

        scope.launch(Dispatchers.Main) {
            getHistory()
        }

    }

    suspend fun getHistory() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        val networkConfig = NetworkConfig().getAfterClass().getHistory(token)
        try {
            if (networkConfig.isSuccessful) {
                for (history in networkConfig.body()!!.data!!) {
                    historyList.add(history!!)
                }

                binding.rvHistory.adapter = ListHistoryAdapter(historyList) {

                    if (it.status == 6) {
                        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {

                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            binding.tvNamaTeacherHistory.text = it.teacherName
                            binding.tvAsalGuruHistory.text = it.teacherOrigin
                            binding.tvNamaPaketHistory.text = it.packageName
                            binding.tvKelasBermasalah.visibility = View.VISIBLE
                            binding.clKesan.visibility = View.GONE
                            binding.btnSimpanHistory.visibility = View.GONE
                            binding.btnLihatFeedback.visibility = View.GONE
                            binding.tvTanggalHistory.text =
                                it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
                            Picasso.get().load(it.teacherAvatar).into(binding.ivTeacherHistory)
                            binding.tvLabelFeedback.visibility = View.GONE
                            binding.rbGuru.setIsIndicator(false)
                            binding.rbGuru.isEnabled = false

                        } else {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    } else {
                        when {
                            it.teacherRate.toString() == "null" -> {
                                val alertDialogBuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(context)
                                alertDialogBuilder.setTitle("Kelas Selesai?")

                                alertDialogBuilder
                                    .setMessage("Apakah kelas sudah selesai?")
                                    .setCancelable(false)
                                    .setPositiveButton(
                                        "Sudah",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                            binding.tvNamaTeacherHistory.text = it.teacherName
                                            binding.tvAsalGuruHistory.text = it.teacherOrigin
                                            binding.tvKelasBermasalah.visibility = View.GONE
                                            binding.tvNamaPaketHistory.text = it.packageName
                                            binding.clKesan.visibility = View.VISIBLE
                                            binding.btnSimpanHistory.visibility = View.VISIBLE
                                            binding.btnLihatFeedback.visibility = View.VISIBLE
                                            binding.rbGuru.isEnabled = true
                                            binding.tvTanggalHistory.text =
                                                it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
                                            if (it.teacherRate.toString() == "null") {
                                                binding.rbGuru.rating = 0f
                                            } else {
                                                binding.rbGuru.rating = it.teacherRate!!.toFloat()
                                                binding.rbGuru.setIsIndicator(false)
                                            }

                                            val etKesan = binding.etKesan.text
                                            val getRating = binding.rbGuru.rating
                                            val getScheduleId = it.id.toString()
                                            binding.btnSimpanHistory.setOnClickListener {
                                                scope.launch {
                                                    rateKelas(
                                                        getScheduleId,
                                                        getRating.toString(),
                                                        etKesan.toString()
                                                    )
                                                }
                                            }

                                            Picasso.get().load(it.teacherAvatar)
                                                .into(binding.ivTeacherHistory)
                                            dialog.dismiss()
                                        })
                                    .setNegativeButton("Belum",
                                        DialogInterface.OnClickListener { dialog, id
                                            ->
                                            dialog.dismiss()
                                        })
                                val alertDialog: AlertDialog = alertDialogBuilder.create()
                                alertDialog.show()

                            }
                            bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN -> {

                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                binding.tvNamaTeacherHistory.text = it.teacherName
                                binding.tvKelasBermasalah.visibility = View.GONE
                                binding.tvAsalGuruHistory.text = it.teacherOrigin
                                binding.tvNamaPaketHistory.text = it.packageName
                                binding.clKesan.visibility = View.GONE
                                binding.btnSimpanHistory.visibility = View.GONE
                                binding.btnLihatFeedback.visibility = View.VISIBLE
                                binding.tvTanggalHistory.text =
                                    it.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
                                Picasso.get().load(it.teacherAvatar).into(binding.ivTeacherHistory)
                                binding.rbGuru.isEnabled = false
                                if (it.teacherRate.toString() == "null") {
                                    binding.rbGuru.rating = 0f
                                } else {
                                    binding.rbGuru.rating = it.teacherRate!!.toFloat()
                                }
                            }
                            else -> {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                        }

                    }

                }
            } else {
                Log.d(TAG, "getHistory: History Gagal diambil")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getHistory: ${e.message}")
        }

    }

    private fun hideBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clFeedback)
        binding.btnLihatFeedback.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

    suspend fun rateKelas(scheduleId: String, rateTeacher: String, feedback: String) {
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
}
