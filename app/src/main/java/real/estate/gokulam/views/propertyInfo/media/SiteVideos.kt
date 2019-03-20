package real.estate.gokulam.views.propertyInfo.media


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_site_videos.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.customer.ItemVideo
import real.estate.gokulam.model.media.ModelMediaDetails
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass. at 28-02-219
 *
 */
class SiteVideos : AppCompatActivity() {
    var dialog: Dialog? = null
    var snackbar: Snackbar? = null
    var landId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(real.estate.gokulam.R.layout.fragment_site_videos)

        site_videos?.layoutManager = LinearLayoutManager(this.applicationContext)

        no_videos?.visibility = View.GONE


        //site_videos?.adapter = ItemVideo(this)

        landId = intent.getStringExtra("landId")

        callPhotos()

    }


    private fun callPhotos() {
        dialog = MessageUtils.showDialog(this@SiteVideos)
        val hashMap = HashMap<String, Any>()
        hashMap["landId"] = landId
        Utils.getInstance(this).callWebApi("videos", SessionManager.getToken(this.applicationContext), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(this@SiteVideos, t!!.message)
                    no_videos?.visibility = View.VISIBLE
                    no_videos?.text = msg
                    //snackbar = MessageUtils.showSnackBar(this@SiteVideos, mSanckView_videos, msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelMedia: ModelMediaDetails = Gson().fromJson<ModelMediaDetails>(response.body().string(), ModelMediaDetails::class.java)
                        if (modelMedia.status) {
                            var count = 0
                            if (modelMedia.image != null) {
                                count = modelMedia.image.size
                            }
                            if (count != 0) {
                                site_videos?.visibility = View.VISIBLE
                                no_videos?.visibility = View.GONE
                                site_videos?.adapter = ItemVideo(this@SiteVideos, modelMedia.image)
                            } else {
                                no_videos?.visibility = View.VISIBLE
                                no_videos?.text = modelMedia.message
                                snackbar = MessageUtils.showSnackBar(this@SiteVideos, mSanckView_videos, modelMedia.message)
                            }
                        } else {
                            no_videos?.visibility = View.VISIBLE
                            no_videos?.text = modelMedia.message
                            //snackbar = MessageUtils.showSnackBar(this@SiteVideos, mSanckView_videos, modelMedia.message)
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        no_videos?.visibility = View.VISIBLE
                        no_videos?.text = msg
                        //snackbar = MessageUtils.showSnackBar(this@SiteVideos, mSanckView_videos, msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    no_videos?.visibility = View.VISIBLE
                    no_videos?.text = e.message
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        MessageUtils.dismissSnackBar(snackbar)
        MessageUtils.dissmiss(dialog)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //overridePendingTransition(real.estate.gokulam.R.anim.left_to_right_end, real.estate.gokulam.R.anim.left_to_right_start)
        overridePendingTransition(R.anim.right_to_left_end, R.anim.left_to_right_end)
    }


}
