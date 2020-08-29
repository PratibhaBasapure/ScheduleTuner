package com.dal.mobilecomputing.scheduletuner

import android.widget.BaseAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView


    class EventListAdaptor(context: Context, taskList: MutableList<Event>) : BaseAdapter() {

        private val _inflater: LayoutInflater = LayoutInflater.from(context)
        private var _taskList = taskList

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val objectId: String = _taskList.get(position).objectId as String
            val itemText: String = _taskList.get(position).eventName as String
            val done: Boolean = _taskList.get(position).done as Boolean

            val view: View
            val listRowHolder: ListRowHolder
            if (convertView == null) {
                view = _inflater.inflate(R.layout.task_rows, parent, false)
                listRowHolder = ListRowHolder(view)
                view.tag = listRowHolder
            } else {
                view = convertView
                listRowHolder = view.tag as ListRowHolder
            }

          //  listRowHolder.desc.text = itemText
            listRowHolder.done.isChecked = done

            return view
        }

        override fun getItem(index: Int): Any {
            return _taskList.get(index)
        }

        override fun getItemId(index: Int): Long {
            return index.toLong()
        }

        override fun getCount(): Int {
            return _taskList.size
        }

        private class ListRowHolder(row: View?) {
           // val desc: TextView = row!!.findViewById(R.id.txtNewTaskDesc) as TextView
            val done: CheckBox = row!!.findViewById(R.id.chkDone) as CheckBox
            val remove: ImageButton = row!!.findViewById(R.id.btnRemove) as ImageButton
        }
    }
