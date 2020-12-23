package com.example.smartpi.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.ChatDataItem
import com.example.smartpi.utils.Preferences


class ChatAdapter(private val context: Context, var list: ArrayList<ChatDataItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var preferences: Preferences
    private var userId = 0

    //pesan yang dikirim
    private inner class MessageInViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView = itemView.findViewById(R.id.message)

        fun bind(position: Int) {
            val messageModel: ChatDataItem = list[position]
            messageTV.text = messageModel.message

        }

    }

    //pesan dari luar
    private inner class MessageOutViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView = itemView.findViewById(R.id.teacher_message)

        fun bind(position: Int) {
            val messageModel: ChatDataItem = list[position]
            messageTV.text = messageModel.message

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        preferences = Preferences(context)
        userId = preferences.getValues("user_id")!!.toInt()

        return if (viewType == userId) {
            MessageInViewHolder(
                LayoutInflater.from(context).inflate(R.layout.my_bubble_chat, parent, false)
            )
        } else MessageOutViewHolder(
            LayoutInflater.from(context).inflate(R.layout.teacher_bubble_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].userId == userId) {
            (holder as MessageInViewHolder).bind(position)
        } else {
            (holder as MessageOutViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].userId!!.toInt()
    }
}