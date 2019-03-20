package real.estate.gokulam.views.propertyInfo.media


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.appunite.appunitevideoplayer.PlayerActivity
import com.khizar1556.mkvideoplayer.MKPlayer
import real.estate.gokulam.R


/**
 * A simple [Fragment] subclass.
 *
 */
class VideoPlayerActivity : AppCompatActivity() {
    var mkplayer: MKPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_video_player)

        val video_path: String = intent.getStringExtra("video_path")
        val file_name: String = intent.getStringExtra("file_name")
        Log.d("video_pathsds", file_name + " \n " + video_path)
        /*mkplayer = MKPlayer(this)
        mkplayer?.play(video_path)*/

        //  berretplayer.setSource(Uri.parse("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4))


        /*val videoPlayerWidget = findViewById<MxVideoPlayerWidget>(R.id.mpw_video_player) as MxVideoPlayerWidget
        videoPlayerWidget.startPlay("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name")*/

        /*val jcVideoPlayerStandard = findViewById<JCVideoPlayerStandard>(R.id.videoplayer) as JCVideoPlayerStandard
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "Test")
        jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"))
*/
        startActivity(PlayerActivity.getVideoPlayerIntent(this.applicationContext, video_path, file_name))
    }

    override fun onPause() {
        super.onPause()
        mkplayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mkplayer?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mkplayer?.stop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(0, 0)
    }

}
