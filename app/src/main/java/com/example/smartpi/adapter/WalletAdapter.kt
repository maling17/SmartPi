package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.WalletItem
import com.squareup.picasso.Picasso

class WalletAdapter(
    private val data: List<WalletItem>,
    private val listener: (WalletItem) -> Unit
) : RecyclerView.Adapter<WalletAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivLogo = view.findViewById<ImageView>(R.id.iv_logo_wallet)
        val tvNama = view.findViewById<TextView>(R.id.tv_nama_wallet)

        fun bindItem(
            data: WalletItem,
            listener: (WalletItem) -> Unit,
            context: Context,
            position: Int
        ) {

            tvNama.text = data.name
            Picasso.get().load(data.logo).into(ivLogo)

            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.pembayaran_item, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size


}