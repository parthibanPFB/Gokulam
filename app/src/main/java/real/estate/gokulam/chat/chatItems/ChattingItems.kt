package real.estate.gokulam.chat.chatItems

import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sendbird.android.BaseChannel
import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.SendBird
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.chat.chatUtils.FileUtils
import real.estate.gokulam.chat.chatUtils.TextUtils
import java.io.File

class ChattingItems(var activity: FragmentActivity) : RecyclerView.Adapter<ChattingItems.ItemChating>() {

    private val mMessageList: MutableList<BaseMessage>? = null
    var mChannel: GroupChannel? = null
    private var mIsMessageListLoading: Boolean = false



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemChating {
        return ItemChating(LayoutInflater.from(activity).inflate(R.layout.chat_list_item_group_chat_user_me, null))
    }


    override fun getItemCount(): Int {
        return mMessageList?.size!!
    }

    override fun onBindViewHolder(holder: ItemChating, position: Int) {

        val message = mMessageList?.get(position)
        var isContinuous = false
        var isNewDay = false
        var isTempMessage = false
        var isFailedMessage = false
        var tempFileMessageUri: Uri? = null


    }


    fun setChannel(channel: GroupChannel) {
        mChannel = channel
    }


    fun load(channelUrl: String) {
        try {
            val appDir = File(activity.getCacheDir(), SendBird.getApplicationId())
            appDir.mkdirs()

            val dataFile = File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().userId + channelUrl) + ".data")

            val content = FileUtils.loadFromFile(dataFile)
            val dataArray = content.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            mChannel = GroupChannel.buildFromSerializedData(Base64.decode(dataArray[0], Base64.DEFAULT or Base64.NO_WRAP)) as GroupChannel

            // Reset message list, then add cached messages.
            mMessageList?.clear()
            for (i in 1 until dataArray.size) {
                mMessageList?.add(BaseMessage.buildFromSerializedData(Base64.decode(dataArray[i], Base64.DEFAULT or Base64.NO_WRAP)))
            }

            notifyDataSetChanged()
        } catch (e: Exception) {
            // Nothing to load.
        }

    }


    /**
     * Replaces current message list with new list.
     * Should be used only on initial load or refresh.
     */
    fun loadLatestMessages(limit: Int, handler: BaseChannel.GetMessagesHandler?) {
        if (mChannel == null) {
            return
        }

        if (isMessageListLoading()) {
            return
        }

        setMessageListLoading(true)
        mChannel!!.getPreviousMessagesByTimestamp(java.lang.Long.MAX_VALUE, true, limit, true, BaseChannel.MessageTypeFilter.ALL, null, BaseChannel.GetMessagesHandler { list, e ->
            handler?.onResult(list, e)

            setMessageListLoading(false)
            if (e != null) {
                e.printStackTrace()
                return@GetMessagesHandler
            }

            if (list.size <= 0) {
                return@GetMessagesHandler
            }

            for (message in mMessageList!!) {
                if (isTempMessage(message) /*|| isFailedMessage(message)*/) {
                    list.add(0, message)
                }
            }

            mMessageList.clear()

            for (message in list) {
                mMessageList.add(message)
            }

            notifyDataSetChanged()
        })
    }


    @Synchronized
    private fun isMessageListLoading(): Boolean {
        return mIsMessageListLoading
    }

    @Synchronized
    private fun setMessageListLoading(tf: Boolean) {
        mIsMessageListLoading = tf
    }

    /**
     * Notifies that the user has read all (previously unread) messages in the channel.
     * Typically, this would be called immediately after the user enters the chat and loads
     * its most recent messages.
     */
    fun markAllMessagesAsRead() {
        if (mChannel != null) {
            mChannel?.markAsRead()
        }
        notifyDataSetChanged()
    }


    fun isTempMessage(message: BaseMessage): Boolean {
        return message.messageId == 0L
    }



    class ItemChating(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text_group_chat_date = itemView.findViewById<CustomTextView>(R.id.text_group_chat_date)
        var card_group_chat_message = itemView.findViewById<CardView>(R.id.card_group_chat_message)
        var group_chat_message_container = itemView.findViewById<LinearLayout>(R.id.group_chat_message_container)

        var text_group_chat_message = itemView.findViewById<TextView>(R.id.group_chat_message_container)
        var text_group_chat_edited = itemView.findViewById<TextView>(R.id.text_group_chat_edited)
        var url_preview_container = itemView.findViewById<LinearLayout>(R.id.url_preview_container)

        var text_url_preview_site_name = itemView.findViewById<TextView>(R.id.text_url_preview_site_name)
        var text_url_preview_title = itemView.findViewById<TextView>(R.id.text_url_preview_title)
        var text_url_preview_description = itemView.findViewById<TextView>(R.id.text_url_preview_description)
        var image_url_preview_main = itemView.findViewById<TextView>(R.id.image_url_preview_main)


        var text_group_chat_time = itemView.findViewById<TextView>(R.id.text_group_chat_time)
        var text_group_chat_read_receipt = itemView.findViewById<TextView>(R.id.text_group_chat_read_receipt)

    }
}