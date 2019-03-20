package real.estate.gokulam.chat.liveChat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.GroupChannel
import real.estate.gokulam.R
import real.estate.gokulam.chat.chatItems.ChattingItems
import real.estate.gokulam.chat.chatUtils.ConnectionManager
import real.estate.gokulam.views.MainActivity

/**
 * A simple [Fragment] subclass. at 04-03-2019
 *
 */
class ChattingPage : Fragment() {

    var chanel_id: String = ""


    private val CHANNEL_LIST_LIMIT = 30
    private val CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT"
    private val CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT"
    private var mChannel: GroupChannel? = null

    var chattingItemAdaptor: ChattingItems? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setNavigationBarDisable("Group Name")
        return inflater.inflate(R.layout.fragment_chating_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chanel_id = arguments?.getString("chanel_id").toString()
        Log.d("chanel_idsds", "" + chanel_id)
    }

    override fun onResume() {
        super.onResume()
        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, ConnectionManager.ConnectionManagementHandler {
            refresh()
        }, activity)

    }


    private fun refresh() {
        if (mChannel == null) {
            GroupChannel.getChannel(chanel_id, GroupChannel.GroupChannelGetHandler { groupChannel, e ->
                if (e != null) {
                    // Error!
                    e.printStackTrace()
                    return@GroupChannelGetHandler
                }

                mChannel = groupChannel

                chattingItemAdaptor!!.setChannel(mChannel!!)
                /*  mChatAdapter.setChannel(mChannel)
                  mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, BaseChannel.GetMessagesHandler { list, e -> mChatAdapter.markAllMessagesAsRead() })*/

                updateActionBarTitle()
            })
        } else {

            mChannel!!.refresh(GroupChannel.GroupChannelRefreshHandler { e ->
                if (e != null) {
                    // Error!
                    e.printStackTrace()
                    return@GroupChannelRefreshHandler
                }

                /*mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, BaseChannel.GetMessagesHandler { list, e -> mChatAdapter.markAllMessagesAsRead() })
                updateActionBarTitle()*/
            })
        }
    }

    private fun updateActionBarTitle() {

        if (activity != null) {

            (activity as MainActivity).setTitle(mChannel?.getName())
        }
    }

}
