package real.estate.gokulam.views.liveChat.chats1810034.customuserlist.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.sendbird.android.User
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.utils.ImageUtils
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.MessageUtils.log
import real.estate.gokulam.utils.PhotoViewerActivity
import real.estate.gokulam.views.liveChat.chats1810034.interfaces.OnclickEvent

@SuppressLint("SetTextI18n")
class UserListAdapter(val context: FragmentActivity, val userArrayList: ArrayList<User>, val onclickEvent: OnclickEvent)
    : RecyclerView.Adapter<UserListAdapter.Viewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserListAdapter.Viewholder {
        return Viewholder(LayoutInflater.from(context).inflate(R.layout.chat_adpater_user_list, p0, false))
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }


    override fun onBindViewHolder(p0: UserListAdapter.Viewholder, p1: Int) {
        try {
            log("where" + userArrayList[p1].nickname)
            if (userArrayList[p1].originalProfileUrl != null && !userArrayList[p1].originalProfileUrl.isEmpty()) {
                ImageUtils.displayRoundImageFromUrl(context, userArrayList[p1].originalProfileUrl, p0.usrimage)
            } else {
                p0.usrimage.setImageResource(R.drawable.add_user)
            }
            if (userArrayList[p1].nickname.length != 0) {
                p0.usrname.text = userArrayList[p1].nickname.substring(0, 1).toUpperCase() + userArrayList[p1].nickname.substring(1)
            }
            p0.constraintLayout.setOnClickListener {
                onclickEvent.clickPosition(p1.toString(), userArrayList.size.toString())
            }

            p0.usrimage.setOnClickListener {

                if (userArrayList[p1].originalProfileUrl != null && !userArrayList[p1].originalProfileUrl.isEmpty()) {
                    viewImageInDialogUrl(context, userArrayList[p1].originalProfileUrl, userArrayList[p1].nickname)
                } else {
                    MessageUtils.showToastMessage(context, "Profile Image Not Found")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun viewImageInDialogUrl(activity: FragmentActivity, originalProfileUrl: String?, nickname: String?) {
        val imagDialog = Dialog(activity, R.style.pauseDialog)
        imagDialog.setCancelable(true)
        imagDialog.setContentView(R.layout.chat_dialog_view_image)
        imagDialog.show()
        imagDialog.window!!.setBackgroundDrawableResource(R.color.dialog_trans)
        var imageUrl = ""
        val viewprofile = imagDialog.findViewById(R.id.viewimage_user_profile) as ImageView
        val viewprofiletitle = imagDialog.findViewById(R.id.tv_view_profile_title) as CustomTextView

        viewprofiletitle.text = nickname
        if (originalProfileUrl != null && !originalProfileUrl.isEmpty()) {
            ImageUtils.displayImageFromUrl(activity, originalProfileUrl, viewprofile, null)
        } else {
            viewprofile.setImageResource(R.drawable.ic_person)
        }


        viewprofile.setOnClickListener {
            if (imagDialog != null && imagDialog.isShowing) {
                imagDialog.dismiss()
            }

            if (originalProfileUrl != null && !originalProfileUrl.isEmpty()) {

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, viewprofile, "profile")
                val intent = Intent(activity, PhotoViewerActivity::class.java)
                intent.putExtra("url", originalProfileUrl)
                intent.putExtra("type", "image")
                activity.startActivity(intent, options.toBundle())
                activity.overridePendingTransition(0, 0)
            } else {
                Toast.makeText(activity, "Image Not found", Toast.LENGTH_SHORT).show()
            }
        }

    }


    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usrimage = itemView.findViewById<ImageView>(R.id.adp_userimageview)!!
        val usrname = itemView.findViewById<TextView>(R.id.adp_user_name)!!
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.adp_linear_layout)!!
    }
}