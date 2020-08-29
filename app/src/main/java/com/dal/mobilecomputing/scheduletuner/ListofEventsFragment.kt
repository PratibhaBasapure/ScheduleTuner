package com.dal.mobilecomputing.scheduletuner

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListofEventsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListofEventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListofEventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View=  inflater.inflate(R.layout.fragment_listof_events, container, false)
        // Inflate the layout for this fragment
        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor"
        )

        // access the listView from xml file
        var mListView = view!!.findViewById<ListView>(R.id.listviewEvents)
       // var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
       // arrayAdapter =ArrayAdapter(activity, android.R.layout.simple_list_item_1, mListView)

       // mListView.adapter = arrayAdapter
        return view
    }

    }


