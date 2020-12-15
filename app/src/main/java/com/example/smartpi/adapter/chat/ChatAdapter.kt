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


class ChatAdapter(context: Context, list: ArrayList<ChatDataItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context: Context
    var list: ArrayList<ChatDataItem>
    lateinit var preferences: Preferences
    var user_id = 0

    private inner class MessageInViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView = itemView.findViewById(R.id.message)
        fun bind(position: Int) {
            val messageModel: ChatDataItem = list[position]
            messageTV.text = messageModel.message

        }

    }

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
        user_id = preferences.getValues("user_id")!!.toInt()


        return if (viewType == user_id) {
            MessageInViewHolder(
                LayoutInflater.from(context).inflate(R.layout.my_bubble_chat, parent, false)
            )
        } else MessageOutViewHolder(
            LayoutInflater.from(context).inflate(R.layout.teacher_bubble_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].userId == user_id) {
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

    init { // you can pass other parameters in constructor
        this.context = context
        this.list = list
    }
}