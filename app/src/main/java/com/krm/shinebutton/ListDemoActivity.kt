package com.krm.shinebutton

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import java.util.*

class ListDemoActivity : Activity() {
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_demo)
        listView = findViewById(R.id.list)
        listView.adapter = ListAdapter()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    var dataList: MutableList<Data> =
        ArrayList()

    internal inner class ListAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return dataList.size
        }

        override fun getItem(i: Int): Any {
            return dataList[i]
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @SuppressLint("InflateParams", "SetTextI18n")
        override fun getView(
            i: Int,
            view: View,
            viewGroup: ViewGroup
        ): View {
            val button: ShineButton = view.findViewById(R.id.po_image)
            val textView = view.findViewById<TextView>(R.id.text_item_id)
            textView.text = "ShineButton Position $i"
            button.setChecked(dataList[i].checked)
            button.setOnCheckStateChangeListener { _: View?, checked: Boolean ->
                dataList[i].checked = checked
            }
            return view
        }

        init {
            for (i in 0..19) {
                dataList.add(Data())
            }
        }
    }

    class Data {
        var checked = false
    }
}