package com.example.smartpi.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.AvailabilitySlotItem
import java.text.SimpleDateFormat
import java.util.*

class SlotJamSingleAdapter(
    private var data: List<AvailabilitySlotItem>,
    private val listener: (AvailabilitySlotItem) -> Unit
) : RecyclerView.Adapter<SlotJamSingleAdapter.LeagueViewHolder>() {
    private lateinit var ContextAdapter: Context
    var row_index: Int? = null

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam = view.findViewById<TextView>(R.id.tv_slot_jam_list)
        val cvSlot = view.findViewById<CardView>(R.id.cv_slot_list)
        val llSlot = view.findViewById<LinearLayout>(R.id.ll_cv_slot)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlotJamSingleAdapter.LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.slot_list, parent, false)
        return SlotJamSingleAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: SlotJamSingleAdapter.LeagueViewHolder, position: Int) {

        val jam = data[position].start!!.toDate().formatTo("HH:mm")
        holder.tvJam.text = jam
        if (data[position].status == 1) {

            holder.llSlot.setBackgroundColor(Color.RED)
            Log.d("TAG", "onBindViewHolder: ${data[position].status} ")


        } else {
            holder.llSlot.setBackgroundColor(Color.WHITE)
            if (row_index == position) {
                holder.llSlot.setBackgroundColor(Color.parseColor("#ECEBFF"))
            } else {
                holder.llSlot.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            Log.d("TAG", "onBindViewHolder: ${data[position].status} ")
            holder.itemView.setOnClickListener {
                listener(data[position])
                row_index = position
                notifyDataSetChanged()
            }
        }

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


    override fun getItemCount(): Int = data.size

}