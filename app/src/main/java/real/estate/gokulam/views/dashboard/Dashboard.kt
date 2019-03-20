package real.estate.gokulam.views.dashboard

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dashboard.*
import okhttp3.ResponseBody
import real.estate.gokulam.items.customer.ItemLandDashBoard
import real.estate.gokulam.model.dashboad.landInfo.ModelLandInfoDashBoard
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Dashboard : Fragment() {
    var dialog: Dialog? = null
    var snackbar: Snackbar? = null
    var adapter: ItemLandDashBoard? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        (activity as MainActivity).setNavigationBar(getString(real.estate.gokulam.R.string.dashboard), 0)
        if (MainActivity.action_settings != null) {
            MainActivity.action_settings.isVisible = false
        }
        val view: View = inflater.inflate(real.estate.gokulam.R.layout.fragment_dashboard, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        land_details_list?.layoutManager = GridLayoutManager(activity, 1) as RecyclerView.LayoutManager?
        land_details_list?.setHasFixedSize(false)


    }

    override fun onResume() {
        super.onResume()
        if (Utils.haveNetworkConnection(activity)) {
            no_data_dash.visibility = View.GONE
            land_details_list.visibility = View.VISIBLE
            onCalledLandInfo()
        } else {
            MessageUtils.showSnackBarAction(activity, snack_view_dashboard, activity!!.getString(real.estate.gokulam.R.string.check_inter_net))
            no_data_dash.visibility = View.VISIBLE
            land_details_list.visibility = View.GONE
            no_data_dash.setText(activity!!.getString(real.estate.gokulam.R.string.check_inter_net))
        }
    }

    private fun onCalledLandInfo() {
        dialog = MessageUtils.showDialog(activity)
        val hashMap = HashMap<String, Any>(0)
        Utils.getInstance(activity).callWebApi("propertylist", SessionManager.getToken(activity), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_dashboard, msg)
                    no_data_dash.visibility = View.VISIBLE
                    land_details_list.visibility = View.GONE
                    no_data_dash.text = msg
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelLand = Gson().fromJson<ModelLandInfoDashBoard>(response.body().string(), ModelLandInfoDashBoard::class.java)
                        if (modelLand.success) {
                            var count = 0
                            if (modelLand.data.landdetails != null) {
                                count = modelLand.data.landdetails.size
                            }
                            if (count != 0) {
                                adapter = ItemLandDashBoard(activity!!, modelLand.data.landdetails)
                                land_details_list?.adapter = adapter
                                land_details_list?.visibility = View.VISIBLE
                                no_data_dash?.visibility = View.GONE
                            } else {
                                land_details_list?.visibility = View.GONE
                                no_data_dash.visibility = View.VISIBLE
                                no_data_dash.text = modelLand.message

                            }
                        } else {
                            //snackbar = MessageUtils.showSnackBar(activity, snack_view_dashboard, modelLand.message)
                            no_data_dash.visibility = View.VISIBLE
                            land_details_list.visibility = View.GONE
                            no_data_dash.text = modelLand.message
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snack_view_dashboard, msg)

                        no_data_dash.visibility = View.VISIBLE
                        land_details_list.visibility = View.GONE
                        no_data_dash.text = msg
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        })

    }


    override fun onDestroyView() {
        super.onDestroyView()

        MessageUtils.dismissSnackBar(snackbar)
        MessageUtils.dissmiss(dialog)
    }
}
