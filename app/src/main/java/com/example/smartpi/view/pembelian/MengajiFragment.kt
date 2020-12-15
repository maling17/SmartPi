package com.example.smartpi.view.pembelian

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListProgramsAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentMengajiBinding
import com.example.smartpi.model.ProgramsItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException


class MengajiFragment : Fragment() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var programsList = ArrayList<ProgramsItems>()
    private var _binding: FragmentMengajiBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMengajiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.rvMengaji.layoutManager = LinearLayoutManager(context)

        binding.pbMengaji.visibility = View.VISIBLE
        binding.rvMengaji.visibility = View.GONE

        scope.launch(Dispatchers.Main) {
            getMengaji()
        }
    }

    suspend fun getMengaji() {

        programsList.clear()
        val networkConfig = NetworkConfig().getPrograms().getMengaji()
        try {
            if (networkConfig.isSuccessful) {

                for (programs in networkConfig.body()!!.data!!) {
                    programsList.add(programs!!)
                }

                binding.pbMengaji.visibility = View.GONE
                binding.rvMengaji.visibility = View.VISIBLE
                binding.rvMengaji.adapter = ListProgramsAdapter(programsList) {
                    val intent =
                        Intent(activity, PilihPaketLanggananActivity::class.java)
                    intent.putExtra("id_program", it.id.toString())
                    startActivity(intent)

                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
}