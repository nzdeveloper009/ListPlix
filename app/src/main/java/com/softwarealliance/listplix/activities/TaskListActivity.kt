package com.softwarealliance.listplix.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.adapters.TaskListItemsAdapter
import com.softwarealliance.listplix.model.responseapi.Message
import com.softwarealliance.listplix.model.responseapi.Task
import com.softwarealliance.listplix.utils.Constants.BOARD_DETAIL
import com.softwarealliance.listplix.utils.Constants.DOCUMENT_ID
import com.softwarealliance.listplix.utils.Constants.getBoardDetails
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {

    // A global variable for Board Details.
    private lateinit var mBoardDetails: Message

    // A global variable for board document id as mBoardDocumentId
    private var mBoardDocumentId: Int = 0

    private var mUserId: Int = 0
    private var mProjectTitle: String? = null

    // A global variable for Assigned Members List.
    private lateinit var mAssignedMembersList: ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        if (intent.hasExtra(DOCUMENT_ID)) {
            mBoardDocumentId = intent.getIntExtra(DOCUMENT_ID, 0)
            mUserId = intent.getIntExtra("USERID",0)
            mProjectTitle = intent.getStringExtra("PROJECT_TITLE")
        }

        Log.d("ParametersTask", "ParametersTask: $mBoardDocumentId\n$mUserId")
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        getBoardDetails(this@TaskListActivity, mBoardDocumentId, mUserId)
//        boardDetails()

    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_task_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mProjectTitle
        }

        toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_members -> {

                val intent = Intent(this@TaskListActivity, MembersActivity::class.java)
                intent.putExtra(BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && (requestCode == MEMBERS_REQUEST_CODE || requestCode == CARD_DETAILS_REQUEST_CODE)
        ) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            getBoardDetails(this@TaskListActivity, mBoardDocumentId, mUserId)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }


    /**
     * A function to setup assigned members task list into recyclerview.
     */
    fun setupMemberTaskList(list: ArrayList<Task>) {

        // Call the function to setup action bar.
        setupActionBar()

        mAssignedMembersList = list
        hideProgressDialog()

        val adapter = TaskListItemsAdapter(this@TaskListActivity, list)
        rv_task_list.adapter = adapter
    }

    /**
     * A companion object to declare the constants.
     */
    companion object {
        //A unique code for starting the activity for result
        const val MEMBERS_REQUEST_CODE: Int = 13

        const val CARD_DETAILS_REQUEST_CODE: Int = 14
    }

}