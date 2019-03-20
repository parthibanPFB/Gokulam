package real.estate.gokulam.views.propertyInfo


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_property_info.*
import kotlinx.android.synthetic.main.fragment_view_customer_details.*
import kotlinx.android.synthetic.main.fragment_view_register_and_due_dates.*
import kotlinx.android.synthetic.main.load_details.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.ItemDueHistory
import real.estate.gokulam.model.modelBooking.ModelBooking
import real.estate.gokulam.model.payment.paymentHistory.ModelPaymentHistory
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.MessageUtils.log
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.utils.Utils.DUE_AMOUNT_PAYMENT
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass. on 27-02-2019
 *
 */
class ViewRegisterAndDueDates : Fragment() {

    var dialog: Dialog? = null
    var snackbar: Snackbar? = null
    var id: String = ""
    var plot_number: String = ""
    var modelPaymentHistory: ModelPaymentHistory? = null
    var adapter: ItemDueHistory? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.due_date))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_register_and_due_dates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments!!.getString("id")
        plot_number = arguments!!.getString("plot_number")
        no_of_payment_details_customer.layoutManager = GridLayoutManager(activity, 1)


        done_payment?.setOnClickListener {

            try {
                if (modelPaymentHistory != null) {
                    val hashMap = HashMap<String, Any>()
                    hashMap["id"] = modelPaymentHistory!!.data.plotdetails.id
                    hashMap["user_id"] = SessionManager.getUserId(activity)
                    hashMap["amount"] = modelPaymentHistory!!.data.plotdetails.due_amount
                    hashMap["identity"] = "due_amount"

                    callHashKeyGenerateInServer(hashMap)

                } else {
                    snackbar = MessageUtils.showSnackBarAction(activity, snac_view_pay, "Something went wrong. Please try again")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        cancel_payment?.setOnClickListener {
            activity!!.onBackPressed()
        }

        expandable_customer?.setOnClickListener {
            log("no_of_payment_details_customer " + adapter?.itemCount)
            try {
                if (adapter != null) {
                    if (adapter?.itemCount != 0) {

                        if (due_details_lay_customer.visibility == View.VISIBLE) {
                            due_details_lay_customer.visibility = View.GONE
                            total_due_customer.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                        } else {
                            due_details_lay_customer.visibility = View.VISIBLE
                            total_due_customer.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)

                        }

                    } else {
                        MessageUtils.showSnackBar(activity!!, snac_view_pay, "Payment Details Not Found")
                    }
                } else {
                    MessageUtils.showSnackBar(activity!!, snac_view_pay, "Payment Details Not Found")
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                MessageUtils.showSnackBar(activity!!, view_due_details, "Payment Details Not Found")
            }
        }
    }


    override fun onResume() {
        super.onResume()

        if (Utils.haveNetworkConnection(activity)) {
            onCalledPayment()
        } else {
            snackbar = MessageUtils.showSnackBarAction(activity, snac_view_pay, activity!!.getString(R.string.check_inter_net))
        }

    }


    private fun onCalledPayment() {

        val hashMap = HashMap<String, Any>()
        hashMap["user_id"] = SessionManager.getUserId(activity)
        hashMap["id"] = id

        dialog = MessageUtils.showDialog(activity)
        val modelProperty: Call<ResponseBody> = Utils.getInstance(activity).callWebApi("payment_history", SessionManager.getToken(activity), hashMap)
        modelProperty.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    payment_details?.visibility = View.GONE
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    due_info_no_found?.visibility = View.VISIBLE
                    due_info_no_found?.text = msg
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        modelPaymentHistory = Gson().fromJson<ModelPaymentHistory>(response.body().string(), ModelPaymentHistory::class.java)
                        if (modelPaymentHistory!!.success) {
                            //9385494125
                            log("true")
                            pay_type?.text = modelPaymentHistory!!.data.plotdetails.payment_option

                            if (modelPaymentHistory!!.data.plotdetails.payment_option.toLowerCase() == "Online".toLowerCase()) {
                                bottom_pay?.visibility = View.VISIBLE
                            } else {
                                bottom_pay?.visibility = View.GONE
                            }
                            payment_details?.visibility = View.VISIBLE
                            due_info_no_found?.visibility = View.GONE
                            register_date_pay?.text = "Date : " + modelPaymentHistory!!.data.plotdetails.register_date
                            register_no_pay?.text = "No : " + modelPaymentHistory!!.data.plotdetails.register_no


                            pay_peroperty_name?.text = modelPaymentHistory!!.data.plotdetails.landdetail.layout_name + " - " + modelPaymentHistory!!.data.plotdetails.plot_number
                            pay_total_amount?.text = modelPaymentHistory!!.data.plotdetails.amount.toString()
                            pay_initial?.text = modelPaymentHistory!!.data.plotdetails.initial_pay.toString()

                            val remain: Double = modelPaymentHistory!!.data.plotdetails.amount - modelPaymentHistory!!.data.plotdetails.initial_pay

                            val totalPayAmount: Double = remain * modelPaymentHistory!!.data.plotdetails.interest / 100

                            total_paid_amount?.text = "" + (totalPayAmount + remain)

                            pay_remaining?.text = "" + totalPayAmount

                            pay_per_emi?.text = modelPaymentHistory!!.data.plotdetails.due_amount.toString()

                            total_due_customer?.text = (modelPaymentHistory!!.data.plotdetails.no_of_years * 12).toInt().toString()

                            no_of_due_customer?.text = (modelPaymentHistory!!.data.payments.size).toInt().toString()


                            //latLang = modelPaymentHistory!!.data.plotdetails.landdetail.lattitude + "," + modelPaymentHistory!!.data.plotdetails.landdetail.longitude
                            var paymentCount: Int = 0

                            if (modelPaymentHistory!!.data.payments != null) {
                                paymentCount = modelPaymentHistory!!.data.payments.size
                            }

                            if (paymentCount != 0) {
                                Collections.reverse(modelPaymentHistory!!.data.payments)
                                adapter = ItemDueHistory(activity!!, modelPaymentHistory!!.data.payments)
                                no_of_payment_details_customer?.adapter = adapter

                            }

                            /*val date: String = "" + modelPaymentHistory!!.data.date + "-" + modelPaymentHistory!!.data.month + "-" + modelPaymentHistory!!.data.year
                            val dateMilles: Long = DatetimeUtils.convertMillesFromDate(date, "dd-MM-yyyy")
                            val currentMilles: Long = System.currentTimeMillis()
                            if (currentMilles < dateMilles) {
                                due_date_crossed.visibility=View.GONE
                            }else{
                                due_date_crossed.visibility=View.VISIBLE
                            }*/
                        } else {
                            log("falseddd")
                            payment_details?.visibility = View.GONE
                            due_info_no_found?.text = modelPaymentHistory!!.message
                            due_info_no_found?.visibility = View.VISIBLE
                        }

                    } else {
                        log("sdassdsd")
                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snac_view_pay, msg)
                        due_info_no_found?.text = msg
                        due_info_no_found?.visibility = View.VISIBLE
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()

                    due_info_no_found?.text = ex.message
                    due_info_no_found?.visibility = View.VISIBLE
                    payment_details?.visibility = View.GONE
                    bottom_lay?.visibility = View.GONE
                }
            }
        })
    }


    private fun callHashKeyGenerateInServer(hashMap: HashMap<String, Any>) {
        Utils.getInstance(activity).callWebApi("booking_create", SessionManager.getToken(activity), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    payment_details?.visibility = View.GONE
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                try {
                    if (response!!.isSuccessful) {
                        val modelBooking: ModelBooking = Gson().fromJson<ModelBooking>(response.body().string(), ModelBooking::class.java)

                        if (modelBooking.mStatus) {

                            PropertyInformation().callPayment(activity!!, modelBooking.mResult, DUE_AMOUNT_PAYMENT)

                        } else {
                            snackbar = MessageUtils.showSnackBar(activity, snac_view_pay, modelBooking.mMessage)
                        }
                    } else {

                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snac_view_pay, msg)
                        due_info_no_found?.text = msg
                        due_info_no_found?.visibility = View.VISIBLE
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    snackbar = MessageUtils.showSnackBar(activity, snac_view_pay, ex.message)
                }
            }

        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        MessageUtils.dissmiss(dialog)
        MessageUtils.dismissSnackBar(snackbar)
    }
}
