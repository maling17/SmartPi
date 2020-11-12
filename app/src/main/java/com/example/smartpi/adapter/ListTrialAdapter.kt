package com.example.smartpi.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTrial = view.findViewById<TextView>(R.id.tv_nama_trial)
        fun bidnItem(
            data: TrialItem,
            listener: (TrialItem) -> Unit,
            context: Context,
            position: Int
        ) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            tvTrial.text = data.namaPaket

            itemView.setOnClickListener {
                listener(data)

                alertDialogBuilder.setTitle("Persiapkan Laptop/Komputer Anda")

                alertDialogBuilder
                    .setMessage("Apakah Anda yakin memilih trial ini?")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Ya",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        val alertDialog: AlertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_trial, parent, false)
        return ListTrialAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
}