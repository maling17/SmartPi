package com.example.smartpi.view.pembelian.virtulaccount

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.adapter.pembayaran.VirtualAccountAdapter
import com.example.smartpi.databinding.ActivityVirtualAccountBinding
import com.example.smartpi.model.payment.VA.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VirtualAccountActivity : AppCompatActivity() {
    private var expandableListView: ExpandableListView? = null
    private var expandableListAdapter: ExpandableListAdapter? = null
    private var expandableListTitle: List<String>? = null
    private var expandableListDetail: HashMap<String, List<String>>? = null

    private var rekening = ""
    private var tagihan = ""
    private var tglExpired = ""
    private var bank = ""

    lateinit var binding: ActivityVirtualAccountBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVirtualAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tglExpired = intent.getStringExtra("tgl").toString()
        tagihan = intent.getStringExtra("tagihan").toString()
        rekening = intent.getStringExtra("rekening").toString()
        bank = intent.getStringExtra("bank").toString()
        numberToCurrency()
        convertToLocalDate()

        val model = when (bank) {
            "MANDIRI" -> {
                MandiriModel().getData()
            }
            "BNI" -> {
                BNIModel().getData()
            }
            "BRI" -> {
                BRIModel().getData()
            }
            "PERMATA" -> {
                PermataModel().getData()
            }
            else -> {
                DefaultBankModel().getData()
            }
        }

        expandableListView = findViewById<View>(R.id.expandableListView) as ExpandableListView
        expandableListDetail = model
        expandableListTitle = ArrayList(expandableListDetail!!.keys)
        expandableListAdapter =
            VirtualAccountAdapter(this, expandableListTitle, expandableListDetail)
        expandableListView!!.setAdapter(expandableListAdapter)

        binding.tvRekening.text = rekening
        binding.tvNamaBank.text = "Transfer ke: Bank $bank"

        binding.ivBackPembayaran.setOnClickListener { finish() }
        binding.tvSalinRekening.setOnClickListener {
            copyTextToClipboard(binding.tvRekening)
        }
        binding.tvSalinUang.setOnClickListener {
            copyTextToClipboard(binding.tvJumlahDibayar)
        }

        binding.expandableListView.setOnGroupClickListener(OnGroupClickListener { parent, _, groupPosition, _ ->
            setListViewHeight(parent, groupPosition)
            false
        })
    }

    private fun copyTextToClipboard(textView: TextView) {
        val textToCopy = textView.text
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text berhasil disalin", Toast.LENGTH_LONG).show()
    }

    private fun setListViewHeight(
        listView: ExpandableListView,
        group: Int
    ) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )

        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun convertToLocalDate() {
        val formattedDate = tglExpired.toDate().formatTo("dd MMMM yyyy HH:mm")
        binding.tvTanggalExpired.text = formattedDate
    }

    private fun numberToCurrency() {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        val currency = format.format(tagihan.toInt())
        binding.tvJumlahDibayar.text = currency
    }

    private fun String.toDate(
        dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
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