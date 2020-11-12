package com.example.smartpi.view.pembelian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListProgramsAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentBahasaInggrisBinding
import com.example.smartpi.model.ProgramsItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class BahasaInggrisFragment : Fragment() {
    var programsList = ArrayList<ProgramsItems>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private var _binding: FragmentBahasaInggrisBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBahasaInggrisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.rvInggris.layoutManager = LinearLayoutManager(context)

        binding.pbInggris.visibility = View.VISIBLE
        binding.rvInggris.visibility = View.GONE

        scope.launch(Dispatchers.Main) {
            getInggris()
        }
    }

    suspend fun getInggris() {

        programsList.clear()
        val networkConfig = NetworkConfig().getPrograms().getInggris()
        if (networkConfig.isSuccessful) {

            for (programs in networkConfig.body()!!.data!!) {
                programsList.add(programs!!)
            }
            binding.pbInggris.visibility = View.GONE
            binding.rvInggris.visibility = View.VISIBLE

            binding.rvInggris.adapter = ListProgramsAdapter(programsList) {

            }
        }

    }
}