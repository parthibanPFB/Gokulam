package real.estate.gokulam.views.propertyInfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_property_info.*
import kotlinx.android.synthetic.main.view_property_info_details_more.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import real.estate.gokulam.R
import real.estate.gokulam.model.Landdetail
import real.estate.gokulam.model.Plotdetail
import real.estate.gokulam.model.modelBooking.InsertResult
import real.estate.gokulam.model.modelBooking.ModelBooking
import real.estate.gokulam.model.property.property_2.ModelProperty
import real.estate.gokulam.payment.fonePaisaPG
import real.estate.gokulam.utils.ImageUtils
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.utils.Utils.*
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropertyInformation : Fragment() {

    var dialog: Dialog? = null
    var snackbar: Snackbar? = null

    var landDetails: Landdetail? = null
    var plotDetails: Plotdetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.property_details))
        return inflater.inflate(R.layout.fragment_property_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*}

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.fragment_property_info)
    */
        cancel_propert_info?.setOnClickListener {
            activity!!.onBackPressed()
        }

        book_now?.setOnClickListener {
            if (Utils.haveNetworkConnection(activity)) {
                dialog = MessageUtils.showDialog(activity)
                val hashMap = HashMap<String, Any>()
                hashMap["id"] = arguments!!.getString("id")
                hashMap["user_id"] = SessionManager.getUserId(activity)
                hashMap["amount"] = java.lang.Double.parseDouble(landDetails!!.booking_amount)
                hashMap["identity"] = "booking"

                callIBookingProperty(hashMap)
                //  callpay(landDetails!!.booking_amount)
            } else {
                snackbar = MessageUtils.showSnackBarAction(activity, snack_view_property, getString(R.string.check_inter_net))
            }
        }

        map_goto.setOnClickListener {
            /* String uri = "http://maps.google.com/maps?saddr="+sourceLatitude+","+sourceLongitude+"&daddr="+destinationLatitude+","+destinationLongitude;
             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
             startActivity(intent);*/
            try {
                if (landDetails != null) {
                    val latLang: String = landDetails!!.lattitude + "," + landDetails!!.longitude
                    Log.d("latLangsdsd", "" + latLang)
                    val mapa: String = "http://maps.google.com/maps?q=loc:$latLang"
                    //Uri gmmIntentUri = Uri.parse("geo:11.247268683, 77.004409&q=11.247268683, 77.004409");
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapa))
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                    activity!!.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (Utils.haveNetworkConnection(activity)) {
            onCalledPropertyInfo()
        } else {
            snackbar = MessageUtils.showSnackBarAction(activity, snack_view_property, getString(R.string.check_inter_net))
        }
    }

    private fun callIBookingProperty(hashMap: HashMap<String, Any>) {
        Utils.getInstance(activity).callWebApi("booking_create", SessionManager.getToken(activity), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelBooking: ModelBooking = Gson().fromJson<ModelBooking>(response.body().string(), ModelBooking::class.java)

                        if (modelBooking.mStatus) {

                            callPayment(activity!!, modelBooking.mResult, FONEPAISAPG_RET_CODE)

                        } else {
                            snackbar = MessageUtils.showSnackBar(activity, snack_view_property, modelBooking.mMessage)
                        }


                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })
    }


    private fun onCalledPropertyInfo() {
        val id = arguments!!.getString("id")
        val hashMap = HashMap<String, Any>()
        hashMap["plot_id"] = id

        dialog = MessageUtils.showDialog(activity)
        val modelProperty = Utils.getInstance(activity).onCallPropertyInfo(SessionManager.getToken(activity), id, SessionManager.getUserId(activity))
        modelProperty.enqueue(object : Callback<ModelProperty> {
            override fun onFailure(call: Call<ModelProperty>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    show_more?.visibility = View.GONE
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    no_more_data?.visibility = View.VISIBLE
                    no_more_data?.text = msg
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ModelProperty>?, response: Response<ModelProperty>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelProperty: ModelProperty = response.body()
                        if (modelProperty.success) {

                            show_more?.visibility = View.VISIBLE
                            bottom_lay?.visibility = View.VISIBLE
                            no_more_data?.visibility = View.GONE

                            plotDetails = modelProperty.data.plotdetails
                            landDetails = modelProperty!!.data.landdetails

                            amount.text = plotDetails!!.amount + " /" + plotDetails!!.square_feet + " sf"
                            ImageUtils.setImageLive(plot_image, BASE_URL_IMAGE + plotDetails!!.plot_image, activity)
                            plot_no?.text = "Plot No : " + plotDetails!!.plot_number

                            land_name?.text = landDetails!!.layout_name
                            area_name?.text = landDetails!!.area + ", " + landDetails!!.layout_address
                            if (!plotDetails!!.facing.isEmpty() && plotDetails!!.facing != null) {
                                facing?.text = "  " + plotDetails!!.facing
                            } else {
                                facing?.visibility = View.GONE
                            }
                            booking_amount?.text = getString(R.string.inr) + " " + landDetails!!.booking_amount
                            opening_date?.text = landDetails!!.opening_date
                            registerNo?.text = landDetails!!.registeration_number.trim()
                            registerDate?.text = landDetails!!.registeration_date
                        } else {
                            show_more?.visibility = View.GONE
                            no_more_data?.visibility = View.VISIBLE
                            no_more_data?.text = modelProperty.message
                            //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, modelProperty.message)
                        }
                    } else {
                        show_more?.visibility = View.GONE
                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                        no_more_data?.text = msg
                        no_more_data?.visibility = View.VISIBLE
                    }
                } catch (ex: Exception) {
                    no_more_data?.text = ex.message
                    no_more_data?.visibility = View.VISIBLE
                    ex.printStackTrace()
                    show_more?.visibility = View.GONE
                    bottom_lay?.visibility = View.GONE
                }
            }
        })
    }

    fun callPayment(activity: FragmentActivity, proPayment: InsertResult, identity: Int) {

        val intent = Intent(activity, fonePaisaPG::class.java)
        try {
            val json_to_be_sent = JSONObject()
json_to_be_sent.put("identity",identity)
            json_to_be_sent.put("id", proPayment.getId())    // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_id", proPayment.getMerchantId())   // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_display", proPayment.getMerchantDisplay())  // Mandatory ..  change it to whatever you want to get it displayed
            json_to_be_sent.put("invoice", proPayment.getInvoice()) //mandatory  .. this is the unique reference against which you can enquire and it can be system generated or manually entered
            json_to_be_sent.put("mobile_no", proPayment.getMobileNo())    ///pass the mobile number if you have else send it blank and the customer will be prompted for the mobile no so that confirmation msg can be sent
            json_to_be_sent.put("email", proPayment.getEmail())          // pass email if an invoice details has to be mailed
            json_to_be_sent.put("invoice_amt", proPayment.getInvoiceAmt())    //pass the amount with two decimal rounded off
            json_to_be_sent.put("note", "")         // pass any notes if you need
            json_to_be_sent.put("payment_types", "")    // not mandatory . this is to restrict the payment types
            json_to_be_sent.put("addnl_info", "")          // pass any addnl data which u need to get baack
            //input for signing  API_KET#id#merchant_id#invoice#amount
            // String signed_ip =API_KEY+"#"+json_to_be_sent.getString("id")+"#"+json_to_be_sent.getString("merchant_id")+"#"+json_to_be_sent.getString("invoice")+"#"+json_to_be_sent.getString("invoice_amt")+"#";
            val signed_ip = proPayment.getHashKey()
            Log.d("signed_msgsds", "" + signed_ip)
            json_to_be_sent.put("sign", signed_ip)
            json_to_be_sent.put("Environment", CALL_PAY_MODE)  //mandatory   //Change it based on the environment you are using //iamchanged
            intent.putExtra("data", json_to_be_sent.toString())

            Log.e("dsgfsdfgdgh", json_to_be_sent.toString())

            activity.startActivityForResult(intent, identity)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun callpay(invoiceAmt: String) {

        Log.e("asdfsffgsh", "" + invoiceAmt)
        //val API_KEY = "08Z1782051U62BY9OUGW4XM67GF2004"
        val API_KEY = "08Z1782051U62BY9OUGW4XM67GF2004"

        val invoice_amt = java.lang.Double.parseDouble(invoiceAmt)
        Log.d("invoice_amtsdsd", "" + invoice_amt)
        val intent = Intent(activity!!, fonePaisaPG::class.java)
        val json_to_be_sent = JSONObject()
        try {
            json_to_be_sent.put("id", "FPTEST")    // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_id", "FPTEST")   // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_display", "fonepaisa")  // Mandatory ..  change it to whatever you want to get it displayed
            json_to_be_sent.put("invoice", System.currentTimeMillis().toString() + "FP") //mandatory  .. this is the unique reference against which you can enquire and it can be system generated or manually entered
            json_to_be_sent.put("mobile_no", "7204853405")    ///pass the mobile number if you have else send it blank and the customer will be prompted for the mobile no so that confirmation msg can be sent
            json_to_be_sent.put("email", "")          // pass email if an invoice details has to be mailed
            json_to_be_sent.put("invoice_amt", invoice_amt)    //pass the amount with two decimal rounded off
            json_to_be_sent.put("note", "")         // pass any notes if you need
            json_to_be_sent.put("payment_types", "")    // not mandatory . this is to restrict the payment types
            json_to_be_sent.put("addnl_info", "")          // pass any addnl data which u need to get baack
            //input for signing  API_KET#id#merchant_id#invoice#amount

            val signed_ip = API_KEY + "#" + json_to_be_sent.getString("id") + "#" + json_to_be_sent.getString("merchant_id") + "#" + json_to_be_sent.getString("invoice") + "#" + json_to_be_sent.getString("invoice_amt") + "#"

            val signed_msg = Utils.getSignedMsg(signed_ip)
            Log.d("signed_msg", "" + signed_msg);
            json_to_be_sent.put("sign", signed_msg)
            json_to_be_sent.put("Environment", CALL_PAY_MODE)  //mandatory   //Change it based on the environment you are using //iamchanged
            intent.putExtra("data", json_to_be_sent.toString())

/*            MainActivity.refreshViews(object : PaymentAfterRefreshInterface() {
                fun refreshViewsAndAPI(isSucess: Boolean) {
                    Log.d("isSucesssdsd", "" + isSucess)
                    if (isSucess) {
                        callAnimalsInfo()
                    }
                }
            })*/

            startActivityForResult(intent, FONEPAISAPG_RET_CODE)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }


/*
    override fun onDestroyView() {
        super.onDestroyView()
        MessageUtils.dissmiss(dialog)
        MessageUtils.dismissSnackBar(snackbar)
    }*/
}