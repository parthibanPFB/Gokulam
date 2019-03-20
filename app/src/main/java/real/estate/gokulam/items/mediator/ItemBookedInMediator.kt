package real.est


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import customs.CustomTextView
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.model.mediator.plotDetails.Plot
import real.estate.gokulam.utils.*
import real.estate.gokulam.utils.Utils.BASE_URL_IMAGE
import real.estate.gokulam.views.mediator.ViewCustomerPropertyDetailsFromMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.set

class ItemBookedInMediator(var activity: FragmentActivity, var bookedProperty: ArrayList<Plot>) : RecyclerView.Adapter<ItemBookedInMediator.HoldeItemBookedProperty>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HoldeItemBookedProperty {
        return HoldeItemBookedProperty(LayoutInflater.from(activity!!).inflate(R.layout.item_booked, null))
    }

    override fun getItemCount(): Int {
        return bookedProperty.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HoldeItemBookedProperty, i: Int) {

        if (bookedProperty[i].status.toLowerCase() == "Booked".toLowerCase()) {
            holder.land_release.visibility = View.VISIBLE
        } else {
            holder.land_release.visibility = View.GONE
        }
        holder.parent_lay.layoutParams = Utils.setParamsForListViewInAll(i, bookedProperty.size, "c")

        val imgURL: String = BASE_URL_IMAGE + bookedProperty[i].landdetail.layout_image

        ImageUtils.setImageLive(holder.land_img, imgURL, activity)
        holder.booked_amount_booked.text = activity.getString(R.string.inr) + " " + bookedProperty[i].landdetail.booking_amount.trim()
        holder.booked_date.text = "Booked on " + DatetimeUtils.convertDates(bookedProperty[i].booking_date, "yyyy-MM-dd", "MMM dd,yyy")
        holder.land_status.text = bookedProperty[i].status
        holder.land_status.visibility = View.GONE

        holder.land_name_booked.text = bookedProperty[i].landdetail.layout_name + " - " + bookedProperty[i].plot_number
        holder.place_area_booked.text = bookedProperty[i].landdetail.layout_address
        holder.total_amount.text = activity.getString(R.string.inr) + " " + bookedProperty[i].amount + "/" + bookedProperty[i].square_feet + "sf"

        holder.parent_lay.setOnClickListener {

            if (bookedProperty[i].status.toLowerCase() != "Booked".toLowerCase()) {
                val fragment = ViewCustomerPropertyDetailsFromMediator()
                val bundle: Bundle = Bundle()
                bundle.putString("id", bookedProperty[i].id.toString())
                fragment.arguments = bundle
                FragmentCallUtils.passFragment(activity, fragment)
            } else {
                MessageUtils.showToastMessage(activity, "This property not register")
            }
        }



        holder.land_release.setOnClickListener {

            callReleaseBooked(bookedProperty[i].id.toString(), i)
        }

    }

    private fun callReleaseBooked(id: String, i: Int) {
        val dialog: Dialog = MessageUtils.showDialog(activity)
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = id
        Utils.getInstance(activity).callWebApi("release_booked", SessionManager.getToken(activity!!), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    MessageUtils.showToastMessage(activity, msg)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)

                try {
                    val plot: Plot? = null

                    bookedProperty[i] = plot!!

                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                    MessageUtils.showToastMessage(activity, ex.message)
                }
            }

        })
    }


    class HoldeItemBookedProperty(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parent_lay = itemView.findViewById<CardView>(R.id.parent_lay_booked)
        var land_img = itemView.findViewById<ImageView>(R.id.land_img)
        var booked_date = itemView.findViewById<CustomTextView>(R.id.booked_date)
        var land_name_booked = itemView.findViewById<CustomTextView>(R.id.land_name_booked)
        var place_area_booked = itemView.findViewById<CustomTextView>(R.id.place_area_booked)
        var total_amount = itemView.findViewById<CustomTextView>(R.id.total_amount)
        var booked_amount_booked = itemView.findViewById<CustomTextView>(R.id.booked_amount_booked)
        var land_status = itemView.findViewById<CustomTextView>(R.id.land_status)
        var land_release = itemView.findViewById<CustomTextView>(R.id.land_release)


    }
}