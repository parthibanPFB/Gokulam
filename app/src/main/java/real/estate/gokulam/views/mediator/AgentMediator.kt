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
import kotlinx.android.synthetic.main.fragment_agent_mediator.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.mediator.ItemMediatorCustomerDetails
import real.estate.gokulam.model.mediator.customerDetailsMediator.ModelCustomerDetailsInMediator
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass. at 01-03-2019
 *
 */
class AgentMediator : Fragment() {
    var dialog: Dialog? = null
    var snackBar: Snackbar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(real.estate.gokulam.R.string.booked_property))
        return inflater.inflate(R.layout.fragment_agent_mediator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booked_list_in_mediator?.layoutManager = LinearLayoutManager(activity)
        onCallBookedPropertyInfo()
        //MessageUtils.showDialog(activity!!)
    }

    private fun onCallBookedPropertyInfo() {
        val hashMap = HashMap<String, Any>()
        hashMap["mediator_id"] = SessionManager.getUserId(activity)
        //hashMap["user_id"] = "1"

        dialog = MessageUtils.showDialog(activity)
        val modelProperty: Call<ResponseBody> = Utils.getInstance(activity).callWebApi("mediatorbooking", SessionManager.getToken(activity), hashMap)
        modelProperty.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    no_booked_list?.visibility = View.VISIBLE
                    no_booked_list?.text = msg
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }


            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val bookedHistory: ModelCustomerDetailsInMediator = Gson().fromJson<ModelCustomerDetailsInMediator>(response.body().string(), ModelCustomerDetailsInMediator::class.java)
                        if (bookedHistory.status) {
                            var counts: Int = 0;
                            if (bookedHistory.customers != null) {
                                counts = bookedHistory.customers.size
                            }
                            if (counts != 0) {
                                booked_list_in_mediator?.visibility = View.VISIBLE
                                no_booked_list?.visibility = View.GONE
                                booked_list_in_mediator?.adapter = ItemMediatorCustomerDetails(activity!!, bookedHistory.customers)
                            } else {
                                booked_list_in_mediator?.visibility = View.GONE
                                no_booked_list?.visibility = View.VISIBLE
                            }
                        } else {
                            booked_list_in_mediator?.visibility = View.GONE
                            no_booked_list?.visibility = View.VISIBLE
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                        no_booked_list?.text = msg
                        no_booked_list?.visibility = View.VISIBLE
                    }
                } catch (ex: Exception) {
                    no_booked_list?.text = ex.message
                    no_booked_list?.visibility = View.VISIBLE
                    ex.printStackTrace()

                }
            }
        })
    }

}
