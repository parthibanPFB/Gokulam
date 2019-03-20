package real.estate.gokulam.chat.liveChat


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.*
import kotlinx.android.synthetic.main.fragment_live_chat.*
import real.estate.gokulam.R
import real.estate.gokulam.chat.chatItems.ItemChatList
import real.estate.gokulam.chat.chatUtils.ConnectionManager
import real.estate.gokulam.views.MainActivity

/**
 * A simple [Fragment] subclass.
 *
 */
class LiveChat : Fragment() {

    val EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL"
    val INTENT_REQUEST_NEW_GROUP_CHANNEL = 302

    private val CHANNEL_LIST_LIMIT = 15
    private val CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST"
    private val CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_LIST"
    private var mChannelListQuery: GroupChannelListQuery? = null
    var itemChatAdapter: ItemChatList? = null

    var mUserListQuery: ApplicationUserListQuery? = null
    var userList = ArrayList<User>()
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        //connectToSendBird(activity,SessionManager.getMobile(activity),SessionManager.getName(activity))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.live_chat))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val mChannelUrl = arguments!!.getString(EXTRA_GROUP_CHANNEL_URL)
        Log.d("mChannelUrl sd", "" + mChannelUrl)*/

        chat_list?.layoutManager = LinearLayoutManager(activity)
        itemChatAdapter = ItemChatList(activity!!)
        chat_list?.adapter = itemChatAdapter
    }

    override fun onResume() {
        Log.d("LIFECYCLE", "GroupChannelListFragment onResume()")

        getUserList()

        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, { refresh() }, activity!!)

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(baseChannel: BaseChannel, baseMessage: BaseMessage) {}

            override fun onChannelChanged(channel: BaseChannel?) {
                //mChannelListAdapter.clearMap()
                //mChannelListAdapter.updateOrInsert(channel)
            }

            override fun onTypingStatusUpdated(channel: GroupChannel?) {
                //mChannelListAdapter.notifyDataSetChanged()
            }
        })

        super.onResume()
    }


    private fun refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT)
    }


    /**
     * Creates a new query to get the list of the user's Group Channels,
     * then replaces the existing dataset.
     *
     * @param numChannels The number of channels to load.
     */
    private fun refreshChannelList(numChannels: Int) {

        mChannelListQuery = GroupChannel.createMyGroupChannelListQuery()
        mChannelListQuery?.limit = numChannels

        mChannelListQuery?.next(GroupChannelListQuery.GroupChannelListQueryResultHandler { list, e ->
            if (e != null) {
                // Error!
                e.printStackTrace()
                return@GroupChannelListQueryResultHandler
            }



            Log.d("channelListsdsds", "" + list.size)
            //mChannelListAdapter.clearMap()
            //mChannelListAdapter.setGroupChannelList(list)

            if (list.size != 0) {
                no_chat_list?.visibility = View.GONE
                chat_list?.visibility = View.VISIBLE



                val iterator=list.iterator()
                iterator.forEach {

                    val members=it.members

                }
                itemChatAdapter?.addChatValues(list as java.util.ArrayList<GroupChannel>)

            } else {
                no_chat_list?.visibility = View.VISIBLE
                chat_list?.visibility = View.GONE
            }
        })

        /*if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false)
        }*/
    }

    private fun getUserList() {
        //dialog = MessageUtils.showDialog(activity)
        try {
            mUserListQuery = SendBird.createApplicationUserListQuery()
            mUserListQuery?.next { p0, p1 ->
                if (p1 != null) {
                    return@next
                } else {
                    userList = p0 as ArrayList<User>
                    //setToADpter()
                    Log.d("userListsdsd", "" + userList.size)
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}
