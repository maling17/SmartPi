package com.example.smartpi.adapter.paket

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.TrialItem

class ListTrialAdapter(
    private var data: List<TrialItem>,
    private val listener: (TrialItem) -> Unit

) : RecyclerView.Adapter<ListTrialAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTrial = view.findViewById<TextView>(R.id.tv_nama_trial)
        fun bindItem(
            data: TrialItem,
            listener: (TrialItem) -> Unit,
            context: Context,
        ) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            tvTrial.text = data.namaPaket

            itemView.setOnClickListener {
                listener(data)

                alertDialogBuilder.setTitle("Persiapkan Laptop/Komputer Anda")

                alertDialogBuilder
                    .setMessage("Apakah Anda yakin memilih trial ini?")
                    .setCancelable(false)
                    .setPositiveButton("Ya")
                    { dialog, _ ->
                        dialog.cancel()
                    }
                    .setNegativeButton("Tidak")
                    { dialog, _ ->
                        dialog.cancel()
                        val alertDialog: AlertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_trial, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, contextAdapter)
    }

    override fun getItemCount(): Int = data.size
}