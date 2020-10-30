package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.DataItem

class PhoneCodeAdapter(
    private var data: List<DataItem>,
    private val listener: (DataItem) -> Unit
) : RecyclerView.Adapter<PhoneCodeAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvKodeNegara = view.findViewById<TextView>(R.id.tv_kode_negara)
        private val tvNegara = view.findViewById<TextView>(R.id.tv_negara)

        fun bidnItem(
            data: DataItem,
            listener: (DataItem) -> Unit,
            context: Context,
            position: Int
        ) {

            val codeCountry="+"+data.phonecode.toString()
            tvKodeNegara.text = codeCountry
            tvNegara.text = data.name.toString()

            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhoneCodeAdapter.LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_phone_code, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: PhoneCodeAdapter.LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

}