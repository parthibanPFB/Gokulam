package real.estate.gokulam.views.mediator


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list_of_booked_property.*

import kotlinx.android.synthetic.main.fragment_property_info.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.customer.ItemBookedProperty
import real.estate.gokulam.model.modelBooking.bookedHistory.BookedHistory
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class MediatorBooked : Fragment() {
    /**
     * Show List of Booked Info from Mediator booking
     */

    var dialog: Dialog? = null
    var snackBar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.my_bookings))
        return inflater.inflate(R.layout.fragment_list_of_booked_property, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        no_booked_info?.text = "No Booked Details"
        booked_details?.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()

        if (Utils.haveNetworkConnection(activity)) {
            onCallBookedPropertyInfo()
        } else {
            snackBar = MessageUtils.showSnackBarAction(activity, snack_view_booked, activity!!.getString(R.string.check_inter_net))
        }
    }

    private fun onCallBookedPropertyInfo() {

        val hashMap = HashMap<String, Any>()
        hashMap["user_id"] = SessionManager.getUserId(activity)

        dialog = MessageUtils.showDialog(activity)
        val modelProperty: Call<ResponseBody> = Utils.getInstance(activity).callWebApi("booked_history", SessionManager.getToken(activity), hashMap)
        modelProperty.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    no_booked_info?.visibility = View.VISIBLE
                    no_booked_info?.text = msg
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val bookedHistory: BookedHistory = Gson().fromJson<BookedHistory>(response.body().string(), BookedHistory::class.java)
                        if (bookedHistory.success) {
                            var counts: Int = 0;
                            if (bookedHistory.data != null) {
                                counts = bookedHistory.data.size
                            }

                            if (counts != 0) {
                                booked_details?.visibility = View.VISIBLE
                                no_booked_info?.visibility = View.GONE

                                booked_details?.adapter = ItemBookedProperty(activity!!, bookedHistory.data)
                            } else {
                                booked_details?.visibility = View.GONE
                                no_booked_info?.visibility = View.VISIBLE
                            }
                        } else {
                            booked_details?.visibility = View.GONE
                            no_booked_info?.visibility = View.VISIBLE
                        }
                    } else {

                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snack_view_booked, msg)
                        no_booked_info?.text = msg
                        no_booked_info?.visibility = View.VISIBLE
                    }
                } catch (ex: Exception) {
                    no_booked_info?.text = ex.message
                    no_booked_info?.visibility = View.VISIBLE
                    ex.printStackTrace()

                    bottom_lay?.visibility = View.GONE
                }
            }
        })
    }
}
