package real.estate.gokulam.views.liveChat.chats1810034.customuserlist

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.sendbird.android.ApplicationUserListQuery
import com.sendbird.android.GroupChannel
import com.sendbird.android.SendBird
import com.sendbird.android.User
import kotlinx.android.synthetic.main.activity_user_list.*
import real.estate.gokulam.R
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.views.liveChat.chats1810034.CreateGroupChannelActivity
import real.estate.gokulam.views.liveChat.chats1810034.GroupChannelActivity
import real.estate.gokulam.views.liveChat.chats1810034.GroupChannelListFragment.INTENT_REQUEST_NEW_GROUP_CHANNEL
import real.estate.gokulam.views.liveChat.chats1810034.GroupChatFragment
import real.estate.gokulam.views.liveChat.chats1810034.customuserlist.adapter.UserListAdapter
import real.estate.gokulam.views.liveChat.chats1810034.customuserlist.adapter.contactpojo.ContactPojo
import real.estate.gokulam.views.liveChat.chats1810034.interfaces.OnclickEvent


@Suppress("UNREACHABLE_CODE")
class UserListActivity : Fragment(), OnclickEvent {


    var userList = ArrayList<User>()
    var contacttemp = ArrayList<String>()
    var contactModelArrayList = ArrayList<ContactPojo>()
    var mUserListQuery: ApplicationUserListQuery? = null
    var userListAdapter: UserListAdapter? = null
    var TAG = "UserList"
    var username = ""
    var userimage = ""
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as GroupChannelActivity).settitle(getString(R.string.select_user))
        return inflater.inflate(R.layout.activity_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(activity)

        /* val curLog = CallLogHelper.getAllCallLogs(activity?.contentResolver)
         setCallLogs(curLog)*/

        /**
         * swiperefreshLayout
         */
        swipe_user_list?.setOnRefreshListener {
            try {
                getUserList()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        /**
         * addgroup
         */
        linearaddgroup?.setOnClickListener {
            try {
                startActivityForResult(Intent(activity, CreateGroupChannelActivity::class.java), INTENT_REQUEST_NEW_GROUP_CHANNEL)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

//        getcontactsList()
        showconfirmataiondialog()

    }

    private fun getcontactsList() {
        try {
            if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Contacts", "permission not permitted")
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, android.Manifest.permission.READ_CONTACTS)) {
                    Log.d("Contacts", "permission dialog")
                    /*  val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getActivity()?.getPackageName(), null));
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                          startActivityForResult(intent, 789)*/
                    settoContactToArrayList()
                } else {
                    Log.d("Contacts", "permission requested")
                    ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.READ_CONTACTS), 123)
                }
            } else {
                Log.d("Contacts", "permission allowed")
                settoContactToArrayList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun settoContactToArrayList() {
        Log.d("Contacts", "Read contacts")
        try {

            val phones = activity?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
            while (phones!!.moveToNext()) {
                val username = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contactModel = ContactPojo()
                if (phoneNumber.length > 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length - 10, phoneNumber.length)
                }
                contactModel.name = username
                contactModel.number = phoneNumber
                contactModelArrayList!!.add(contactModel)
                contacttemp.add(phoneNumber)
                Log.d("Contacts", username + "  " + phoneNumber)
            }
            phones.close()
            getUserList()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun getUserList() {
        dialog = MessageUtils.showDialog(activity)
        try {
            mUserListQuery = SendBird.createApplicationUserListQuery()
            mUserListQuery?.next { p0, p1 ->
                if (p1 != null) {
                    return@next
                } else {
                    userList = p0 as ArrayList<User>
                    setToADpter()
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun setToADpter() {
        if (dialog != null && dialog?.isShowing!!) {
            MessageUtils.dissmiss(dialog)
        }
        swipe_user_list?.isRefreshing = false
//        userList.clear()//Testing purpose
        try {
            if (userList.size != 0) {

                /**
                 * remove from contacts list
                 */
                val iterator = userList?.iterator()
                iterator.forEach {
                    Log.d("USERLIST", "" + SessionManager.getMobile(context).toString().trim())
                    Log.d("USERLIST", "" + it.userId.toString().trim())
                    if (SessionManager.getMobile(context).toString().trim().equals(it.userId.toString().trim())) {
                        iterator.remove()
                    } else {
                        // For Comparing with Phone Contacts ,Server Contacts;
                        /* if (contacttemp.contains(it.userId)) {
                             Log.d(TAG, " if " + it.userId)
                         } else {
                             Log.d(TAG, "" + it.userId)
                             iterator.remove()
                         }*/
                    }
                }

                if (userList.size != 0) {
                    tv_empty_usr_list?.visibility = View.GONE
                    recyler_userList?.visibility = View.VISIBLE
                    recyler_userList?.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true) as RecyclerView.LayoutManager?
                    userListAdapter = UserListAdapter(activity!!, userList, this)
                    recyler_userList?.adapter = userListAdapter
                    if (userList.size != 1) {
                        linearaddgroup?.visibility = View.VISIBLE
                    } else {
                        linearaddgroup?.visibility = View.GONE
                    }
                } else {
                    linearaddgroup?.visibility = View.GONE
                    recyler_userList?.visibility = View.GONE
                    tv_empty_usr_list?.visibility = View.VISIBLE
                }

            } else {
                linearaddgroup?.visibility = View.GONE
                recyler_userList?.visibility = View.GONE
                tv_empty_usr_list?.visibility = View.VISIBLE
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun clickPosition(position: String, name: String) {
        try {
            dialog = MessageUtils.showDialog(activity)
            val selected = ArrayList<String>()
            username = userList[position.toInt()].nickname
            userimage = userList[position.toInt()].originalProfileUrl
            selected.add(userList[position.toInt()].userId)
            selected.add(SessionManager.getMobile(activity).toString())
            createchennals(selected, true)
            Log.d(TAG, position + "" + name)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createchennals(selected: ArrayList<String>, b: Boolean) {
        try {
            /**
             * create only two users chats
             */
            GroupChannel.createChannelWithUserIds(selected, b, username, userimage, null, null, GroupChannel.GroupChannelCreateHandler { groupChannel, e ->
                if (e != null) {
                    // Error.
                    Log.d(TAG, "" + e)
                    return@GroupChannelCreateHandler
                    MessageUtils.dissmiss(dialog)
                } else {
                    MessageUtils.dissmiss(dialog)
                    val fragment = GroupChatFragment.newInstance(groupChannel.url)
                    val manager = fragmentManager
                    manager!!.beginTransaction()
                            .replace(R.id.container_group_channel, fragment)
                            .addToBackStack(null)
                            .commit()
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("GrChLIST", "" + requestCode)
        if (requestCode == INTENT_REQUEST_NEW_GROUP_CHANNEL) {
            Log.d("GrChLIST", "" + requestCode)
            if (resultCode == RESULT_OK) {
                // Channel successfully created
                // Enter the newly created channel.
                val newChannelUrl = data?.getStringExtra(CreateGroupChannelActivity.EXTRA_NEW_CHANNEL_URL)!!
                if (newChannelUrl != null) {
                    enterGroupChannel(newChannelUrl)
                }
            } else {
                Log.d("GrChLIST", "resultCode not STATUS_OK")
            }
        }

    }

    internal fun enterGroupChannel(channelUrl: String) {
        val fragment = GroupChatFragment.newInstance(channelUrl)
        fragmentManager!!.beginTransaction()
                .replace(R.id.container_group_channel, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showconfirmataiondialog() {
        try {
            val confirmatioandialog = Dialog(activity)
            confirmatioandialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmatioandialog.setContentView(R.layout.dialog_confirmation_permission)
            confirmatioandialog.window!!.setBackgroundDrawableResource(R.drawable.rounded_rect_border)

            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                confirmatioandialog.show()
                val title: TextView
                val reason1: TextView
                val reason2: TextView
                val confirmation: TextView
                title = confirmatioandialog.findViewById(R.id.permission_title)
                reason1 = confirmatioandialog.findViewById(R.id.reason_of_permis1)
                reason2 = confirmatioandialog.findViewById(R.id.reason_of_permis2)
                confirmation = confirmatioandialog.findViewById(R.id.confirmation_granted)
                title.text = getString(R.string.permissionfor_contacts)
                reason1.text = getString(R.string.reason_forpermission1)
                reason2.text = getString(R.string.reason_forpermission)
                confirmation.setOnClickListener {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                    if (confirmatioandialog != null && confirmatioandialog.isShowing) {
                        confirmatioandialog.dismiss()
                    }
                    getcontactsList()
                }

            } else {
                getcontactsList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

}
