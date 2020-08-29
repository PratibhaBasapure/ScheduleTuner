package com.dal.mobilecomputing.scheduletuner

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


abstract class BaseActivity : AppCompatActivity(), WeekView.EventClickListener,
    MonthLoader.MonthChangeListener, WeekView.EventLongPressListener,
    WeekView.EmptyViewLongPressListener, NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: Toolbar
    lateinit var mNavDrawer: DrawerLayout
    lateinit var navigationView: NavigationView
    private var buttonBasic: Button?=null

    private var firebaseDatabaseReference: DatabaseReference? = null
    private var fireabseDatabase: FirebaseDatabase? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var mWeekViewType = TYPE_THREE_DAY_VIEW
    var weekView: WeekView? = null
        private set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_base)
        setContentView(R.layout.nav_drawer_layout)
        //supportActionBar!!.hide()

        // Get a reference for the week view in the layout.
        weekView = findViewById(R.id.weekView)

        // Show a toast message about the touched event.
        weekView!!.setOnEventClickListener(this)

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView!!.setMonthChangeListener(this)

        // Set long press listener for events.
        weekView!!.eventLongPressListener = this

        // Set long press listener for empty view
        weekView!!.emptyViewLongPressListener = this

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false)

        startMainActivity()


   toolbar = findViewById(R.id.toolbar)
       setSupportActionBar(toolbar)
       // supportActionBar(toolbar).hide()

        mNavDrawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        var toggle = ActionBarDrawerToggle(
            this, mNavDrawer, toolbar
            , R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );


        mNavDrawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)




    }



    private fun startMainActivity() {
        fireabseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabaseReference = fireabseDatabase!!.reference!!.child("Users")
        firebaseAuth = FirebaseAuth.getInstance()

        //Schedule


    }



    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        weekView!!.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = weekday[0].toString()
                return weekday.toUpperCase() + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 AM" else "$hour AM"
            }
        }
    }

    protected fun getEventTitle(time: Calendar): String {
        return String.format(
            "Event of %02d:%02d %s/%d",
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            time.get(Calendar.MONTH) + 1,
            time.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        Toast.makeText(this, "Clicked " + event.name, Toast.LENGTH_SHORT).show()
    }

    override fun onEventLongPress(event: WeekViewEvent, eventRect: RectF) {
        Toast.makeText(this, "Long pressed event: " + event.name, Toast.LENGTH_SHORT).show()
    }

    override fun onEmptyViewLongPress(time: Calendar) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT)
            .show()
    }



    override fun onStart() {
        super.onStart()
        val mUser = firebaseAuth!!.currentUser
        val mUserReference = firebaseDatabaseReference!!.child(mUser!!.uid)
        // tvEmail!!.text = mUser.email
        //tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //      tvFirstName!!.text = snapshot.child("firstName").value as String
                //    tvLastName!!.text = snapshot.child("lastName").value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()

        val intent= Intent(this@BaseActivity ,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        //setupDateTimeInterpreter(menuItem.itemId == R.id.action_week_view)
        when (menuItem.itemId) {

            R.id.action_day_view -> {
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    menuItem.isChecked = !menuItem.isChecked
                    mWeekViewType = TYPE_DAY_VIEW
                    weekView!!.numberOfVisibleDays = 1

                    // Lets change some dimensions to best fit the view.
                    weekView!!.columnGap =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.textSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            12f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.eventTextSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            12f,
                            resources.displayMetrics
                        ).toInt()

                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()

            }
            R.id.action_three_day_view -> {
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    menuItem.isChecked = !menuItem.isChecked
                    mWeekViewType = TYPE_THREE_DAY_VIEW
                    weekView!!.numberOfVisibleDays = 3

                    // Lets change some dimensions to best fit the view.
                    weekView!!.columnGap =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.textSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            12f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.eventTextSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            12f,
                            resources.displayMetrics
                        ).toInt()
                }

            }
            R.id.action_week_view -> {
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    menuItem.isChecked = !menuItem.isChecked
                    mWeekViewType = TYPE_WEEK_VIEW
                    weekView!!.numberOfVisibleDays = 7

                    // Lets change some dimensions to best fit the view.
                    weekView!!.columnGap =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            2f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.textSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            10f,
                            resources.displayMetrics
                        ).toInt()
                    weekView!!.eventTextSize =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            10f,
                            resources.displayMetrics
                        ).toInt()
                }
            }

           /* R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
            }*/
            R.id.nav_goals -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GoalsFragment()).commit()
            }
            R.id.nav_events -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EventsFragment()).commit()
            }

            R.id.nav_trash -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TrashFragment()).commit()
            }
            R.id.nav_setting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingFragment()).commit()
            }
            R.id.nav_help -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HelpFragment()).commit()
            }
            R.id.nav_signout -> {
                signOut()
            }
        }
        mNavDrawer.closeDrawer(GravityCompat.START)
        return true;
    }

    companion object {
        private val TYPE_DAY_VIEW = 1
        private val TYPE_THREE_DAY_VIEW = 2
        private val TYPE_WEEK_VIEW = 3
    }
}
