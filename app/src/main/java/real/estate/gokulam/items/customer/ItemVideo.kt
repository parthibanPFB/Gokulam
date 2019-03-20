package real.estate.gokulam.items.customer

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.appunite.appunitevideoplayer.PlayerActivity
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.model.media.Image
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.utils.Utils.BASE_URL_MEDIA

class ItemVideo(var activity: FragmentActivity, var videos: List<Image>) : RecyclerView.Adapter<ItemVideoHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemVideoHolder {
        return ItemVideoHolder(LayoutInflater.from(activity).inflate(R.layout.item_video, null))

    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: ItemVideoHolder, i: Int) {

        holder.item_parent_video.layoutParams = Utils.setParamsForListViewInAll(i, 20, "l")

        holder.video_title.text = videos[i].filename
        holder.item_parent_video.setOnClickListener {

            try {

                /*val intent = Intent(activity, VideoPlayerActivity::class.java)
                intent.putExtra("video_path", Utils.BASE_URL_MEDIA + videos[i].filename)
                intent.putExtra("file_name", videos[i].filename)
                activity.startActivity(intent)*/


                activity.startActivity(PlayerActivity.getVideoPlayerIntent(activity, BASE_URL_MEDIA + videos[i].filename, videos[i].filename))

                activity.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
            } catch (ex: Exception) {
                MessageUtils.showToastMessage(activity, "Something went wrong")
                ex.printStackTrace()
            }

        }
    }
}

class ItemVideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var item_parent_video = itemView.findViewById<LinearLayout>(R.id.item_parent_video)
    var video_title = itemView.findViewById<CustomTextView>(R.id.video_title)
}