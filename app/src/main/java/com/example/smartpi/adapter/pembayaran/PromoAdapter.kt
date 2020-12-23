package com.example.smartpi.adapter.pembayaran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.PromoItem
import com.squareup.picasso.Picasso

class PromoAdapter(
    private var data: List<PromoItem>,
    private val listener: (PromoItem) -> Unit
) : RecyclerView.Adapter<PromoAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivPromo = view.findViewById<ImageView>(R.id.iv_promo)

        fun bidnItem(
            data: PromoItem,
            listener: (PromoItem) -> Unit,
        ) {

            Picasso.get().load(data.gambar).into(ivPromo)
            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.promo_list, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

}