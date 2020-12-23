package com.example.smartpi.adapter.jadwal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.AvailabilitySlotItem
import java.text.SimpleDateFormat
import java.util.*

class SlotJamMultiAdapter(
    private var data: List<AvailabilitySlotItem>,
    private val listener: (AvailabilitySlotItem) -> Unit
) : RecyclerView.Adapter<SlotJamMultiAdapter.LeagueViewHolder>() {
    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvJam = view.findViewById<TextView>(R.id.tv_slot_jam_list)
        private val llSlot = view.findViewById<LinearLayout>(R.id.ll_cv_slot)

        fun bidnItem(
            data: AvailabilitySlotItem,
            listener: (AvailabilitySlotItem) -> Unit,
        ) {
            val jam = data.start!!.toDate().formatTo("HH:mm")
            tvJam.text = jam
            var status = 0

            if (data.status.toString() == "1") {

                llSlot.setBackgroundColor(Color.RED)

            } else {
                llSlot.setBackgroundColor(Color.WHITE)
                itemView.setOnClickListener {
                    listener(data)

                    status = if (status == 0) {
                        llSlot.setBackgroundColor(Color.parseColor("#ECEBFF"))
                        1
                    } else {
                        llSlot.setBackgroundColor(Color.WHITE)

                        0
                    }

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

    }

    class SlotItemDiffCallback(
        private var oldSlotList: List<AvailabilitySlotItem>,
        private var newSlotList: List<AvailabilitySlotItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldSlotList.size
        }

        override fun getNewListSize(): Int {
            return newSlotList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldSlotList[oldItemPosition].id == newSlotList[newItemPosition].id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSlotList[oldItemPosition] == newSlotList[newItemPosition]
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.slot_list, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener)

    }

    override fun getItemCount(): Int = data.size

    fun submitList(availabilitySlotItemList: List<AvailabilitySlotItem>) {
        val oldList = data
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            SlotItemDiffCallback(
                oldList, availabilitySlotItemList
            )
        )
        data = availabilitySlotItemList
        diffResult.dispatchUpdatesTo(this)
    }

}