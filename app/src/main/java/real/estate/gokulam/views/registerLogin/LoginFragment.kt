package real.estate.gokulam.views.registerLogin

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.sendbird.android.SendBird
import customs.AsteriskPasswordTransformationMethod
import customs.Fonts
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.model.login.ModelLogin
import real.estate.gokulam.model.login.collectionLogin.CollectionLoginModel
import real.estate.gokulam.utils.*
import real.estate.gokulam.utils.Utils.*
import real.estate.gokulam.views.MainActivity
import real.estate.gokulam.views.MainActivity.mAppEmailID
import real.estate.gokulam.views.MainActivity.mAppUserName
import real.estate.gokulam.views.colletionAgent.CollectionAgent
import real.estate.gokulam.views.dashboard.Dashboard
import real.estate.gokulam.views.liveChat.chats1810034.ConnectionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    var snackbar: Snackbar? = null
    var dialog: Dialog? = null
    var sessionManager: SessionManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setDisableToolBar()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //SendBird.init("2BD7BE55-AEFE-46A3-81EA-FF186CBE291F", activity!!.applicationContext)
        sessionManager = SessionManager(activity)
        password.transformationMethod = AsteriskPasswordTransformationMethod()
        password.typeface = MessageUtils.setType(activity, Fonts.REGULAR)


        checkLogin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user_name?.setText("9734432700")
            } else {
                user_name?.setText("9788686374")
            }
        }
        login.setOnClickListener {
            if (Utils.haveNetworkConnection(activity)) {

                val isCheck = checkLogin?.isChecked as Boolean
                Log.d("isChecksds", "" + isCheck)
                val mobileNumber = user_name.text.toString().trim()
                val password = password.text.toString().trim()
                if (validation(mobileNumber, password)) {
                    val hashMap = HashMap<String, Any>()
                    hashMap["username"] = mobileNumber
                    hashMap["password"] = password
                    hashMap["token_reference"] = "token_reference"
                    if (isCheck) {

                        callLoginAPI(hashMap, "adminlogin")
                    } else {

                        callLoginAPI(hashMap, "login")
                    }
                }
            } else {
                snackbar = MessageUtils.showSnackBarAction(activity, snack_view, getString(R.string.check_inter_net))
            }
        }
        register.setOnClickListener {
            FragmentCallUtils.passFragmentWithoutAnim(activity, RegistrationFragment())
        }
    }

    private fun callLoginAPI(hashMap: HashMap<String, Any>, url: String) {
        dialog = MessageUtils.showDialog(activity)
        Utils.getInstance(activity).callWebApi(url, "", hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    snackbar = MessageUtils.showSnackBar(activity, snack_view, msg)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                MessageUtils.dissmiss(dialog)

                try {

                    if (response!!.isSuccessful) {

                        if (url == "login") {
                            val modelLogin = Gson().fromJson<ModelLogin>(response.body().string(), ModelLogin::class.java)

                            if (modelLogin.success) {

                                /**
                                 * Send Bird Connection
                                 */

                                connectToSendBird( modelLogin.data.userdetail.mobile_no,modelLogin.data.userdetail.name)
//                                connectToSendBird("6381447812","ananth")
                                sessionManager?.createLoginSession(modelLogin.data.userdetail.name, modelLogin.data.userdetail.email, modelLogin.data.userdetail.mobile_no, modelLogin.data.userdetail.id.toString(), modelLogin.data.token, modelLogin.data.userdetail.user_type)

/*
                                sessionManager?.createLoginSession(modelLogin.data.userdetail.name, modelLogin.data.userdetail.email, modelLogin.data.userdetail.mobile_no, modelLogin.data.userdetail.id.toString(), modelLogin.data.token, modelLogin.data.userdetail.user_type)*/

                                mAppEmailID.text = modelLogin.data.userdetail.email
                                mAppUserName.text = modelLogin.data.userdetail.name

                                MessageUtils.showToastMessage(activity, modelLogin.message)


                            } else {
                                snackbar = MessageUtils.showSnackBar(activity, snack_view, modelLogin.message)
                            }
                        } else {
                            val modelLogin: CollectionLoginModel = Gson().fromJson<CollectionLoginModel>(response.body().string(), CollectionLoginModel::class.java)

                            if (modelLogin.success) {
                                mAppEmailID.text = modelLogin.data.userdetail.email
                                mAppUserName.text = modelLogin.data.userdetail.name
                                MessageUtils.showToastMessage(activity, modelLogin.message)
                                connectToSendBird( modelLogin.data.userdetail.mobile_no,modelLogin.data.userdetail.name)
//                                connectToSendBird("6381447812","ananth")
                                sessionManager?.createLoginSession(modelLogin.data.userdetail.name, modelLogin.data.userdetail.email, modelLogin.data.userdetail.mobile_no, modelLogin.data.userdetail.id.toString(), modelLogin.data.token, modelLogin.data.userdetail.user_type.toString())

                            } else {
                                snackbar = MessageUtils.showSnackBar(activity, snack_view, modelLogin.message)
                            }


                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        snackbar = MessageUtils.showSnackBar(activity, snack_view, msg)
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        })


    }


    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */
    fun connectToSendBird(userId: String, userNickname: String) {
        // Show the loading indicator
        dialog = MessageUtils.showDialog(activity)
        Log.d("sendBirs", "Connection");
        ConnectionManager.login(userId, SendBird.ConnectHandler { user, e ->
            // Callback received; hide the progress bar.
            if (e != null) {
                Log.d("sendBirs", "Connection erro");

                // Error!
                Toast.makeText(activity, "" + e.code + ": " + e.message, Toast.LENGTH_SHORT).show()

                // Show login failure snackbar
                /*showSnackbar("Login to SendBird failed")
                mConnectButton.setEnabled(true)
                PreferenceUtils.setConnected(false)*/
                return@ConnectHandler
            }
            Log.d("sendBirs", "Connection 11");

            /*PreferenceUtils.setConnected(true)
            CURRENT_USER = user*/
            // Update the user's nickname
            updateCurrentUserInfo(userNickname)
            updateCurrentUserPushToken()
            MessageUtils.dissmiss(dialog)
            // Proceed to MainActivity
            /*val intent = Intent(this@LoginActivity, GroupChannelActivity::class.java)
            startActivity(intent)*/



            if (SessionManager.getUserType(activity!!).toLowerCase() == USER_CUSTOMER.toLowerCase()) {
                FragmentCallUtils.passFragmentWithoutAnim(activity!!, Dashboard())
            } else if (SessionManager.getUserType(activity!!).toLowerCase() == USER_MEDIATOR.toLowerCase()) {
                FragmentCallUtils.passFragmentWithoutAnim(activity!!, Dashboard())
            } else if (SessionManager.getUserType(activity!!).toLowerCase() == USER_COLLECTION_AGENT.toLowerCase()) {
                FragmentCallUtils.passFragmentWithoutAnim(activity!!, CollectionAgent())
            }


        })
    }

    /**
     * Update the user's push token.
     */
    private fun updateCurrentUserPushToken() {
        Log.d("sendBirs", "Connection push");

        PushUtils.registerPushTokenForCurrentUser(activity, null)
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private fun updateCurrentUserInfo(userNickname: String) {
        Log.d("sendBirs", "Connection info");

        SendBird.updateCurrentUserInfo(userNickname, null, SendBird.UserInfoUpdateHandler { e ->
            if (e != null) {
                // Error!
                Toast.makeText(activity, "" + e.code + ":" + e.message, Toast.LENGTH_SHORT).show()

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed")

                return@UserInfoUpdateHandler
            }

            //PreferenceUtils.setUsername(userNickname)
        })
    }

    private fun validation(mobileNumber: String, password: String): Boolean {

        if (mobileNumber.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view, "Mobile Number can't be empty")
            return false
        } else if (mobileNumber.length != 10) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view, "Enter Valid Mobile Number ")
            return false
        } else if (password.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view, "Password can't be empty")
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        if (sessionManager?.checkLogin()!!) {
            connectToSendBird(SessionManager.getMobile(activity), SessionManager.getName(activity))
        }
    }
}