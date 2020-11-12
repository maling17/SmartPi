package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.ProgramsItems
import com.squareup.picasso.Picasso

class ListProgramsAdapter(
    private var data: List<ProgramsItems>,
    private val listener: (ProgramsItems) -> Unit

) : RecyclerView.Adapter<ListProgramsAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPackage = view.findViewById<ImageView>(R.id.iv_package)

        fun bidnItem(
            data: ProgramsItems,
            listener: (ProgramsItems) -> Unit,
            context: Context,
            position: Int
        ) {

            Picasso.get().load(data.img).into(ivPackage)

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_package, parent, false)
        return ListProgramsAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
}