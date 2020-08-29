package com.dal.mobilecomputing.scheduletuner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.EditText

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */



class EventsFragment : Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    lateinit var _db: DatabaseReference
    //private var txtNewTaskDesc: TextView? = null
    var fab: FloatingActionButton? = null
    var EventName: EditText? = null
    var btnaddEvent: Button? =null
    lateinit var _adapter: ListAdapter
    var listviewTask: ListView?=null
    var imgbtnStartDate: ImageButton?=null
    var imgbtnStartTime: ImageButton?=null
    var imgbtnEndDate: ImageButton?=null
    var imgbtnEndTime: ImageButton?=null
    var tvStarttime: TextView?=null
    var tvStartDate: TextView?=null
    var tvEndtime: TextView?=null
    var tvEndDate: TextView?=null

    var _taskList: MutableList<Event>? = null
    private var mYear:Int? = null
    private var mMonth:Int? = null
    private var mDate:Int? = null
    private var mHour:Int? = null
    private var mMinute:Int? = null
    private var mDay:Int? = null
    private var textDate:TextView?=null
    val MONTHS =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_events, container, false)

            // _adapter = EventListAdaptor(this,_taskList!!)
      //  listviewTask!!.setAdapter(_adapter)
        imgbtnStartDate=view!!.findViewById(R.id.imgbtndate)
        imgbtnStartTime=view!!.findViewById(R.id.imgbtntime)
        imgbtnEndDate=view!!.findViewById(R.id.imgbtnEndDate)
        imgbtnEndTime=view!!.findViewById(R.id.imgbtnEndTime)
        tvStartDate=view!!.findViewById(R.id.tvStartDate)
        tvStarttime=view!!.findViewById(R.id.tvStartTime)
tvEndtime=view!!.findViewById(R.id.tvEndTime)
        tvStartDate=view!!.findViewById(R.id.tvStartDate)
        tvEndDate=view!!.findViewById(R.id.tvEndDate)
        imgbtnStartDate!!.setOnClickListener{
    // Get Current Date
    val c = Calendar.getInstance()
    mYear = c.get(Calendar.YEAR)
    mMonth = c.get(Calendar.MONTH)
    mDay = c.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog = activity?.let { it1 ->
        DatePickerDialog(
            it1,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            tvStartDate!!.setText(
                dayOfMonth.toString() + "-" + MONTHS[monthOfYear + 1] + "-" + year
            )
        }, mYear!!, mMonth!!, mDay!!
    )
    }
    datePickerDialog!!.show()
}
        imgbtnStartTime!!.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvStarttime!!.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        imgbtnEndTime!!.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvEndtime!!.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        imgbtnEndDate!!.setOnClickListener{
            // Get Current Date
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        tvEndDate!!.setText(
                            dayOfMonth.toString() + "-" +  MONTHS[monthOfYear + 1] + "-" + year
                        )
                    }, mYear!!, mMonth!!, mDay!!
                )
            }
            datePickerDialog!!.show()
        }


        fab=view!!.findViewById(R.id.fab)
        btnaddEvent=view!!.findViewById(R.id.btnAdd)

      /*  fab!!.setOnClickListener{
            showFooter()

        }*/
        btnaddEvent!!.setOnClickListener{
            addEvent()

        }

        return view

    }

   /* fun showFooter(){
            footer.visibility = View.VISIBLE
            btnaddEvent?.visibility = View.GONE
        }
*/

    fun addEvent(){

        firebaseAuth = FirebaseAuth.getInstance()
        val mUser = firebaseAuth!!.currentUser
        _db = FirebaseDatabase.getInstance().reference.child(mUser!!.uid)


        //Declare and Initialise the Task
        val event = Event.create()
        EventName=view!!.findViewById(R.id.editEventName)
        //Set Task Description and isDone Status
        event.eventName = EventName?.text.toString()
        event.startDate=tvStartDate?.text.toString()
        event.endDate=tvEndDate?.text.toString()
        event.startTime=tvStarttime?.text.toString()
        event.endTime=tvEndtime?.text.toString()


        //Get the object id for the new task from the Firebase Database
        val newEvent = _db.child(EventStatics.FIREBASE_EVENT).push()
        event.objectId = newEvent.key

        //Set the values for new task in the firebase using the footer form
        newEvent.setValue(event)

        //Hide the footer and show the floating button
        //footer.visibility = View.GONE
        btnaddEvent!!.visibility = View.VISIBLE

        //Reset the new task description field for reuse.
        EventName?.setText("")

        /*Toast.makeText(this, "Event added to the list successfully" + event.objectId, Toast.LENGTH_SHORT).show()*/
    }
}
