package real.estate.gokulam.views.propertyInfo.media


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_site_photos.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.customer.ItemViewPagerImageView
import real.estate.gokulam.model.media.ModelMediaDetails
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * A simple [Fragment] subclass. at 28-02-2019
 *
 */

class SitePhotos : AppCompatActivity() {

    var dialog: Dialog? = null
    var snackbar: Snackbar? = null

    var landId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(real.estate.gokulam.R.layout.fragment_site_photos)

        val myObject: MutableList<String> = ArrayList()
        this.landId = intent.getStringExtra("landId")

        callPhotos()

        /*   myObject.add("https://images.unsplash.com/photo-1455218043782-149aa7dc42c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80")
           myObject.add("https://images.unsplash.com/photo-1456019794261-203be0304ce5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1355&q=80")
           myObject.add("https://images.unsplash.com/photo-1521233296957-c2d4e5874b48?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80")
           myObject.add("https://images.unsplash.com/photo-1522251519194-04481042f3e7?ixlib=rb-1.2.1&auto=format&fit=crop&w=564&q=80")
           myObject.add("https://images.unsplash.com/photo-1517505568456-f678c6821687?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
           myObject.add("https://images.unsplash.com/photo-1524084479181-0c08672ea4d2?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjF9&auto=format&fit=crop&w=500&q=60")
           myObject.add("https://images.unsplash.com/photo-1549737524-aef1a1f12ccd?ixlib=rb-1.2.1&dpr=1&auto=format&fit=crop&w=525&q=60")
           myObject.add("https://images.unsplash.com/photo-1500651230702-0e2d8a49d4ad?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60")
           myObject.add("https://images.unsplash.com/photo-1455218043782-149aa7dc42c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80")
           myObject.add("https://images.unsplash.com/photo-1456019794261-203be0304ce5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1355&q=80")
           myObject.add("https://images.unsplash.com/photo-1521233296957-c2d4e5874b48?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80")


           imageViewPager.adapter = ItemViewPagerImageView(this, myObject)*/


        SessionManager.getUserId(this.applicationContext)
    }

    private fun callPhotos() {
        dialog = MessageUtils.showDialog(this@SitePhotos)
        val hashMap = HashMap<String, Any>()
        hashMap["landId"] = landId
        Utils.getInstance(this).callWebApi("photos", SessionManager.getToken(this.applicationContext), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(this@SitePhotos, t!!.message)
                    // snackbar = MessageUtils.showSnackBar(this@SitePhotos, mSanckView_photos, msg)

                    no_images?.visibility = View.VISIBLE
                    no_images?.text = msg
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
                                no_images?.visibility = View.GONE
                                imageViewPager.visibility = View.VISIBLE
                                imageViewPager.adapter = ItemViewPagerImageView(this@SitePhotos, modelMedia.image)
                            } else {
                                no_images?.visibility = View.VISIBLE
                                no_images?.text = modelMedia.message
                                snackbar = MessageUtils.showSnackBar(this@SitePhotos, mSanckView_photos, modelMedia.message)
                            }
                        } else {
                            no_images?.visibility = View.VISIBLE
                            no_images?.text = modelMedia.message
                            //snackbar = MessageUtils.showSnackBar(this@SitePhotos, mSanckView_photos, modelMedia.message)
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        no_images?.visibility = View.VISIBLE
                        no_images?.text = msg
                        // snackbar = MessageUtils.showSnackBar(this@SitePhotos, mSanckView_photos, msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    no_images?.visibility = View.VISIBLE
                    no_images?.text = e.message
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
        overridePendingTransition(R.anim.right_to_left_end, R.anim.left_to_right_end)


    }

}
