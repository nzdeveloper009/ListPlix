package com.softwarealliance.listplix.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.adapters.BoardItemsAdapter
import com.softwarealliance.listplix.api.ApiClient
import com.softwarealliance.listplix.model.responseapi.Message
import com.softwarealliance.listplix.model.responseapi.User
import com.softwarealliance.listplix.utils.Constants.CREATE_BOARD_REQUEST_CODE
import com.softwarealliance.listplix.utils.Constants.DOCUMENT_ID
import com.softwarealliance.listplix.utils.Constants.MY_PROFILE_REQUEST_CODE
import com.softwarealliance.listplix.utils.Constants.getBoardsList
import com.softwarealliance.listplix.utils.Constants.loadUserData
import com.softwarealliance.listplix.utils.LocalStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var localStorage: LocalStorage
    lateinit var apiClient:ApiClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localStorage = LocalStorage(this@MainActivity)
        Log.d("Tokeeeeeen", "onCreate: ${localStorage.fetchAuthToken()}")
        apiClient = ApiClient()

        setupActionBar()

        // Assign the NavigationView.OnNavigationItemSelectedListener to navigation view.
        nav_view.setNavigationItemSelectedListener(this)

        loadUserData(this,true)


    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        val toolbar_main_activity = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }


    /**
     * A function for opening and closing the Navigation Drawer.
     */
    private fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this@MainActivity, MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE
                )
            }

            R.id.nav_sign_out -> {
                Toast.makeText(this@MainActivity,"This is sign out",Toast.LENGTH_LONG).show()
                // Here sign outs the user from firebase in this device.
                localStorage.loggedIn = false
                localStorage.token = ""
                localStorage.email = ""


                // Send the user to the intro screen of the application.
                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            // Get the user updated details.
             loadUserData(this@MainActivity)
        } else if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_BOARD_REQUEST_CODE
        ) {
            // Get the latest boards list.
            getBoardsList(this@MainActivity,localStorage.userID)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    /**
     * A function to get the current user details.
     */
    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean)    {

//        mUserName = user.name

        // The instance of the header view of the navigation view.
        val headerView = nav_view.getHeaderView(0)

        // The instance of the user image of the navigation view.
        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)

        // Load the user image in the ImageView.
        Glide
            .with(this@MainActivity)
            .load(R.drawable.smile) // URL of the image
            .centerCrop() // Scale type of the image.
            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
            .into(navUserImage) // the view in which the image will be loaded.

        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name

        if (readBoardsList) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            getBoardsList(this@MainActivity,user.id)
        }
    }

    /**
     * A function to populate the result of BOARDS list in the UI i.e in the recyclerView.
     */
    fun populateBoardsListToUI(boardsList: ArrayList<Message>,mUserID: Int) {

        hideProgressDialog()
        if (boardsList.size > 0) {

            rv_boards_list.visibility = View.VISIBLE
            tv_no_boards_available.visibility = View.GONE

            rv_boards_list.layoutManager = LinearLayoutManager(this@MainActivity)
            rv_boards_list.setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            val adapter = BoardItemsAdapter(this@MainActivity, boardsList)
            rv_boards_list.adapter = adapter // Attach the adapter to the recyclerView.

            adapter.setOnClickListener(object :
                BoardItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Message) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(DOCUMENT_ID, model.id)
                    intent.putExtra("USERID", mUserID)
                    intent.putExtra("PROJECT_TITLE", model.project_title)
                    startActivity(intent)
                }
            })
        } else {
            rv_boards_list.visibility = View.GONE
            tv_no_boards_available.visibility = View.VISIBLE
        }
    }
}