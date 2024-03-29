package com.example.smartpi.view.pembelian

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.paket.ListProgramsAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentAkademikBinding
import com.example.smartpi.model.ProgramsItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class AkademikFragment : Fragment() {

    private val TAG = "MyActivity"
    private var programsList = ArrayList<ProgramsItems>()
    private var token = ""

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private var _binding: FragmentAkademikBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkademikBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.rvAkademik.layoutManager = LinearLayoutManager(context)
        binding.pbAkademik.visibility = View.VISIBLE
        binding.rvAkademik.visibility = View.GONE

        scope.launch(Dispatchers.Main) {
            getMatematika()
        }
    }

    private suspend fun getMatematika() {

        programsList.clear()
        val networkConfig = NetworkConfig().getPrograms().getMatematika()

        try {
            if (networkConfig.isSuccessful) {
                for (programs in networkConfig.body()!!.data!!) {
                    programsList.add(programs!!)
                }

                binding.pbAkademik.visibility = View.GONE
                binding.rvAkademik.visibility = View.VISIBLE
                binding.rvAkademik.adapter = ListProgramsAdapter(programsList) {

                    val intent =
                        Intent(activity, PilihPaketLanggananActivity::class.java)
                    intent.putExtra("id_program", it.id.toString())
                    Log.d(TAG, "getMatematika: ${it.id}")
                    startActivity(intent)
                }
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}