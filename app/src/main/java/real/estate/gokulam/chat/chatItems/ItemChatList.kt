package real.estate.gokulam.chat.chatItems

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.sendbird.android.GroupChannel
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.chat.liveChat.ChattingPage
import real.estate.gokulam.utils.FragmentCallUtils
import real.estate.gokulam.utils.ImageUtils
import java.util.*

class ItemChatList(var activity: FragmentActivity) : RecyclerView.Adapter<ItemChatList.ItemChatListHolder>() {

    var chatItenValues = ArrayList<GroupChannel>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemChatListHolder {
        return ItemChatListHolder(LayoutInflater.from(activity).inflate(R.layout.item_chat_list, null))

    }

    override fun getItemCount(): Int {
        return chatItenValues.size
    }

    override fun onBindViewHolder(holder: ItemChatListHolder, i: Int) {

        Log.d("chatItenValuessds", "" + chatItenValues[i].name + "  " + chatItenValues[i].coverUrl)

        ImageUtils.setImageLive(holder.chat_img, chatItenValues[i].coverUrl, activity)

        holder.chat_user_name.text = chatItenValues[i].name
        holder.chat_item?.setOnClickListener {

            Log.d("sdsdsd", "" + chatItenValues[i].members.size)
            val fragment = ChattingPage()
            val bundle = Bundle()

            bundle.putString("chanel_id", chatItenValues[i].url.toString())
            fragment.arguments = bundle

            FragmentCallUtils.passFragment(activity, fragment)

        }
    }

    fun addChatValues(chatList: ArrayList<GroupChannel>) {
        chatItenValues = chatList
        notifyDataSetChanged()
    }

    class ItemChatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chat_user_name = itemView.findViewById<CustomTextView>(R.id.chat_user_name)
        var chat_img = itemView.findViewById<ImageView>(R.id.chat_img)
        var chat_item = itemView.findViewById<LinearLayout>(R.id.chat_item)
    }
}