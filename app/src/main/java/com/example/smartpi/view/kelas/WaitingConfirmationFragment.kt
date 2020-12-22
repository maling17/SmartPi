package com.example.smartpi.view.kelas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListWaitingConfirmationAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentWaitingConfirmationBinding
import com.example.smartpi.model.WaitingItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class WaitingConfirmationFragment : Fragment() {
    private var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"

    private var waitingList = ArrayList<WaitingItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var _binding: FragmentWaitingConfirmationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWaitingConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        waitingList.clear()
        preferences = Preferences(requireContext())
        token = "Bearer ${preferences.getValues("token")}"
        binding.rvWaiting.layoutManager = LinearLayoutManager(context)

        scope.launch(Dispatchers.Main) {
            getWaitingConfirmation()
        }

    }

    private suspend fun getWaitingConfirmation() {
        val networkConfig = NetworkConfig().getAfterClass().getWaitingConfirmation(token)

        try {
            if (networkConfig.isSuccessful) {
                binding.pbWaiting.visibility = View.GONE
                binding.rvWaiting.visibility = View.VISIBLE

                for (waiting in networkConfig.body()!!.data!!) {
                    waitingList.add(waiting!!)
                }

                binding.rvWaiting.adapter = ListWaitingConfirmationAdapter(waitingList) {}
            } else {
                binding.pbWaiting.visibility = View.GONE
                binding.clWaitingEmpty.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.d(TAG, "${e.message}")
        }

    }
}