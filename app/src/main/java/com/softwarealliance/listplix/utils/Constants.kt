package com.softwarealliance.listplix.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.softwarealliance.listplix.activities.MainActivity
import com.softwarealliance.listplix.activities.MyProfileActivity
import com.softwarealliance.listplix.activities.SignInActivity
import com.softwarealliance.listplix.activities.TaskListActivity
import com.softwarealliance.listplix.api.ApiClient
import com.softwarealliance.listplix.model.responseapi.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

object Constants{

    // API Constants
    const val BASE_URL = "https://473a-72-255-7-160.in.ngrok.io/TaskManager/public/api/"
    const val REGISTER  = "register"
    const val LOGIN  = "login"
    const val VERIFY  = "verify"
    const val RESEND_OTP  = "resend"
    const val FORGET_PASSWORD  = "forget_password"
    const val UPDATE_PASSWORD  = "update_password"
    const val GET_USER_INFO  = "get_user_info"
    const val PROJECT_BY_ID  = "project_by_id"
    const val GET_TASK_BY_ID  = "get_task_by_id"


    // digit + lowercase char + uppercase char + punctuation + symbol
    const val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"

    //    const val GMAIL_PATTERN = "^[a-z0-9](\\.?[a-z0-9]){5,}@gmail\\.com$"
    const val GMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+"


    fun isValidPattern(input: String?, CHECK_PATTERN: String?): Boolean {
        val pattern = Pattern.compile(CHECK_PATTERN)
        val matcher = pattern.matcher(input)
        return matcher.matches()
    }

    //A unique code for starting the activity for result
    const val MY_PROFILE_REQUEST_CODE: Int = 11

    const val CREATE_BOARD_REQUEST_CODE: Int = 12

    private lateinit var apiClient:ApiClient
    fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {
        // Here we have received the document snapshot which is converted into the User Data model object.
//        val loggedInUser = getUserInfo(localStorage.token)
        var localStorage = LocalStorage(activity)
        apiClient = ApiClient()
        apiClient.getApiService(activity).requestGetUserDetail()
            .enqueue(object : Callback<ResponseUserDetail> {
                override fun onResponse(
                    call: Call<ResponseUserDetail>,
                    response: Response<ResponseUserDetail>
                ) {
                    if(response.isSuccessful){
                        val loggedInUser = response.body()?.user
                        localStorage.userID = loggedInUser?.id!!
                        Log.d("USERIDDT", "onResponse: ${localStorage.userID}")
                        when (activity) {
                            is SignInActivity -> {
                                activity.signInSuccess(loggedInUser!!)
                            }
                            is MainActivity -> {
                                activity.updateNavigationUserDetails(loggedInUser!!,readBoardsList)
                            }
                            is MyProfileActivity -> {
                                activity.setUserDataInUI(loggedInUser!!)
                            }
                        }
                        Log.d("SuccessDemoLog", "onResponse: ${response.body()?.user}")
                    } else {
                        Log.d("ErrorDemoLog", "onResponse: ${response.code()} ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                    Log.d("FailureDemoLog", "onResponse: ${t.message}")
                }

            })

    }

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult
    const val READ_STORAGE_PERMISSION_CODE = 1
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the extension of selected image.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    /**
     * A function to update the user profile data into the database.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        /*update(userHashMap)
        when (activity) {
            is MainActivity -> {
                activity.tokenUpdateSuccess()
            }
            is MyProfileActivity -> {
                activity.profileUpdateSuccess()
            }
        }*/
    }


    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val USERS: String = "users"

    // This  is used for the collection name for USERS.
    const val BOARDS: String = "boards"

    // Firebase database field names
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST: String = "taskList"
    const val ID: String = "id"
    const val EMAIL: String = "email"

    const val BOARD_DETAIL: String = "board_detail"

    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"

    const val BOARD_MEMBERS_LIST: String = "board_members_list"

    const val SELECT: String = "Select"
    const val UN_SELECT: String = "UnSelect"


    /**
     * A function to get the list of created boards from the database.
     */
    fun getBoardsList(activity: MainActivity,userID:Int) {

        // The collection name for BOARDS

        // Step 1: get Current User ID, Token
        // Step 2: Get User Details
        // Step 3: Create Instance of Boards ArrayList
        // Step 4: add Projects in the Boards Instance
        // Step 5: call populateBoardsListToUI and pass this boards instance as a paramter

        val apiClient = ApiClient()
        apiClient.getApiService(activity).requestForUserBoard(userID)
            .enqueue(object : Callback<ResponseGetProjectByID>{
                override fun onResponse(
                    call: Call<ResponseGetProjectByID>,
                    response: Response<ResponseGetProjectByID>
                ) {
                    Log.d("USERIDDD", "USERIDDD: $userID")

                    if(response.isSuccessful)
                    {
                        var projectList: ArrayList<Message> = ArrayList()
                        for ((index, value) in response.body()?.message?.withIndex()!!) {
//                            Log.d("ListWichList", "the element at $index is $value\n")
                            projectList.addAll(value)
                        }
//                        Log.d("ProjecTLIst", "onResponse: ${projectList}")
//                        Log.d("SuccessBoardResponse", "onResponse: ${response.body()?.message}")
                        // Here pass the result to the base activity.
                        activity.populateBoardsListToUI(projectList,userID)
                    } else {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        val error = jObjError.getString("error")
                        if(error.equals("\"Project not found.\""))
                        {
                            Toast.makeText(activity,"Project Not Found",Toast.LENGTH_LONG).show()
                        }
                        Log.d("ErrorBoardResponse", "ErrorBoardResponse: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseGetProjectByID>, t: Throwable) {
                    Log.d("FailureBoardResponse", "onResponse: ${t.message} ${t.printStackTrace()}")

                }

            })
    }

    /**
     * A function to get the Board Details.
     */
    fun getBoardDetails(activity: TaskListActivity, documentId: Int, mUserId: Int) {

        // Step 1: get user ID/token
        // Step 2: Get Project ID
        // Step 3: Create board instance of Task and save current user all information about projects/boards in this board
        // Step 4: current user board instance id compare with all database instance id
        // Step 5: pass this current user board instance to boardDetails as a parameter


        // Example:

/*        Log.e(activity.javaClass.simpleName, document.toString())

        val board = document.toObject(Board::class.java)!!
        board.documentId = document.id

        // Send the result of board to the base activity.
        activity.boardDetails(board)*/

        val apiClient = ApiClient()
        apiClient.getApiService(activity).requestForUserTasks(mUserId,documentId)
            .enqueue(object: Callback<ResponseGetTaskByID>{
                override fun onResponse(
                    call: Call<ResponseGetTaskByID>,
                    response: Response<ResponseGetTaskByID>
                ) {
                   if(response.isSuccessful)
                   {
                       activity.hideProgressDialog()
                       Log.d("TaskResponseBody", "TaskResponseBody: ${response}\n${response.body()}\b${response.body()?.task}")
                       var projectList: ArrayList<Task> = ArrayList()
                       for ((index, value) in response.body()?.task?.withIndex()!!) {
                            Log.d("ListWichListTask", "the element at $index is $value\n")
                           projectList.add(value)
                       }

                       activity.setupMemberTaskList(projectList)
                   }else {
                       /*val jObjError = JSONObject(
                           response.errorBody()!!.string()
                       )
                       val error = jObjError.getString("error")
                       if(error.equals("\"task not found.\""))
                       {
                           Toast.makeText(activity,"task not found",Toast.LENGTH_LONG).show()
                       }*/
                       activity.hideProgressDialog()
                       Log.d("ErrorTaskResponse", "ErrorBoardResponse: ${response.errorBody()} ${response.code()}")
                   }
                }

                override fun onFailure(call: Call<ResponseGetTaskByID>, t: Throwable) {

                }

            })


    }

}