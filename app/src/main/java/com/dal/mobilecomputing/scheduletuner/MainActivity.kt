package com.dal.mobilecomputing.scheduletuner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlin.math.sign


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    //
    lateinit var toolbar: Toolbar
    lateinit var mNavDrawer: DrawerLayout
    lateinit var navigationView: NavigationView
 private var buttonBasic: Button?=null

    private var firebaseDatabaseReference: DatabaseReference? = null
    private var fireabseDatabase: FirebaseDatabase? = null
    private var firebaseAuth: FirebaseAuth? = null
   // private var btnSignout: Button? = null

    //UI elements
   // private var tvFirstName: TextView? = null
   // private var tvLastName: TextView? = null
   // private var tvEmail: TextView? = null
  //  private var tvEmailVerifiied: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        setContentView(R.layout.nav_drawer_layout)

        /*startMainActivity()


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mNavDrawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        var toggle = ActionBarDrawerToggle(
            this, mNavDrawer, toolbar
            , R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

buttonBasic=findViewById(R.id.buttonBasic)
        buttonBasic!!.setOnClickListener { startActivity(
            Intent(this@MainActivity,
                BasicActivity::class.java)
        ) }


        mNavDrawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GoalsFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_goals)
        }
*/






    }

    private fun startMainActivity() {
        fireabseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabaseReference = fireabseDatabase!!.reference!!.child("Users")
        firebaseAuth = FirebaseAuth.getInstance()

        //Schedule

/*


*/



        /*tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        btnSignout=findViewById<View>(R.id.sign_out_button) as Button
       btnSignout!!.setOnClickListener { signOut() }*/
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

        val intent=Intent(this@MainActivity ,LoginActivity::class.java)
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
        when (menuItem.itemId) {
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

    }


