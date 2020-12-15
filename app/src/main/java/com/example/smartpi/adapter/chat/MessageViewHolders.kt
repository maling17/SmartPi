package com.example.smartpi.adapter.chat

import android.view.View
import android.widget.TextView
import com.example.smartpi.R
import com.example.smartpi.model.ChatDataItem

class MyMessageViewHolders(val view: View) : MessageViewHolder<ChatDataItem>(view) {

    private val messageContent = view.findViewById<TextView>(R.id.message)
    override fun bind(item: ChatDataItem) {

        messageContent.text = item.message
    }
}

class TeacherMessageViewHolders(val view: View) : MessageViewHolder<ChatDataItem>(view) {

    private val messageContent = view.findViewById<TextView>(R.id.teacher_message)
    override fun bind(item: ChatDataItem) {

        messageContent.text = item.message
    }


}