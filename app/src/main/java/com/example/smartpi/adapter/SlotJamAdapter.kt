package com.example.smartpi.adapter

import android.content.Context
import android.graphics.Color
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

class SlotJamAdapter(
    private var data: List<AvailabilitySlotItem>,
    private val listener: (AvailabilitySlotItem) -> Unit
) : RecyclerView.Adapter<SlotJamAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context


    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSlot = view.findViewById<TextView>(R.id.tv_slot_jam_list)
        val cvSlot = view.findViewById<CardView>(R.id.cv_slot_list)
        val llSLot = view.findViewById<LinearLayout>(R.id.ll_cv_slot)

        fun bidnItem(
            data: AvailabilitySlotItem,
            listener: (AvailabilitySlotItem) -> Unit,
            context: Context,
            position: Int
        ) {
            val jam = data.start!!.toDate().formatTo("HH:mm")
            var selected: Boolean = true
            var selectedPos = RecyclerView.NO_POSITION

            tvSlot.text = jam
            itemView.setOnClickListener {
                listener(data)

                /*  if (selectedPos == position) {
                      llSLot.setBackgroundColor(Color.parseColor("#ECEBFF"))
                  } else {
                      llSLot.setBackgroundColor(Color.parseColor("#FFFFFF"))
                  }*/

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

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlotJamAdapter.LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.slot_list, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: SlotJamAdapter.LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)

        if (data[position].isSelected!!) {
            holder.llSLot.setBackgroundColor(Color.YELLOW)
        } else {
            holder.llSLot.setBackgroundColor(Color.WHITE)
        }
    }


    override fun getItemCount(): Int = data.size

}