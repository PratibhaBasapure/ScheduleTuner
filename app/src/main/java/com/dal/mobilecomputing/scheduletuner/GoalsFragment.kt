package com.dal.mobilecomputing.scheduletuner

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import android.widget.ArrayAdapter

import com.google.firebase.database.FirebaseDatabase


/**
 * A simple [Fragment] subclass.
 */
class GoalsFragment : Fragment() {
    lateinit var _db: DatabaseReference
    //private var txtNewTaskDesc: TextView? = null
    var fab: FloatingActionButton? = null
    var GoalName: EditText? = null
    var btnaddGoal: Button? =null
    var edittotalhours: EditText?=null
    var spinnerhowoften: Spinner?=null

    var btnStartDate: ImageButton?=null
    var tvStartDate: TextView?=null
var sppiner_Text: String?=null

    var _taskList: MutableList<Goals>? = null
    private var mYear:Int? = null
    private var mMonth:Int? = null
    private var mDate:Int? = null
    private var mHour:Int? = null
    private var mMinute:Int? = null
    private var mDay:Int? = null
    private var textDate: TextView?=null
    val MONTHS =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View=  inflater.inflate(R.layout.fragment_goals, container, false)

        btnStartDate=view!!.findViewById(R.id.editStartDate)

        spinnerhowoften = view!!.findViewById(R.id.spinnerhowoften)
        btnaddGoal = view!!.findViewById(R.id.btnCreateGoal)
        val items = arrayOf("Once a week", "3 times a week", "5 times a week","Every day")
        val adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        spinnerhowoften!!.setAdapter(adapter)
        spinnerhowoften!!.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(arg0:AdapterView<*>, arg1:View, arg2:Int,
                                        arg3:Long) {
                sppiner_Text = spinnerhowoften!!.getItemAtPosition(3).toString()
            }
        })
        tvStartDate=view!!.findViewById(R.id.tvgoalsStartDate)
        GoalName=view!!.findViewById(R.id.editGoalName)

        edittotalhours=view!!.findViewById(R.id.editTotalHours)
        btnStartDate!!.setOnClickListener{
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

        btnaddGoal!!.setOnClickListener{
            addGoal()

        }
        // Inflate the layout for this fragment
        return view
    }
    fun addGoal(){

        firebaseAuth = FirebaseAuth.getInstance()
        val mUser = firebaseAuth!!.currentUser
        _db = FirebaseDatabase.getInstance().reference.child(mUser!!.uid)


        //Declare and Initialise the Task
        val goal = Goals.create()
        goal.goalName=GoalName?.text.toString()
        goal.howoften=sppiner_Text
        goal.startDate=tvStartDate?.text.toString()
        goal.totalhours=edittotalhours?.text.toString()

        //Get the object id for the new task from the Firebase Database
        val newGoals = _db.child(GoalStatics.FIREBASE_EVENT).push()
        goal.objectId = newGoals.key

        //Set the values for new task in the firebase using the footer form
        newGoals.setValue(goal)

        //Hide the footer and show the floating button
        //footer.visibility = View.GONE
        btnaddGoal!!.visibility = View.VISIBLE

        //Reset the new task description field for reuse.
        GoalName?.setText("")

        /*Toast.makeText(this, "Event added to the list successfully" + event.objectId, Toast.LENGTH_SHORT).show()*/
    }
}
