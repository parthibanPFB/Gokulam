package real.estate.gokulam.items.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.model.modelBooking.bookedHistory.BookedPropertyData
import real.estate.gokulam.utils.*
import real.estate.gokulam.utils.Utils.BASE_URL_IMAGE
import real.estate.gokulam.views.propertyInfo.ViewRegisterAndDueDates

class ItemBookedProperty(var activity: FragmentActivity, var bookedProperty: List<BookedPropertyData>) : RecyclerView.Adapter<ItemBookedProperty.HoldeItemBookedProperty>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HoldeItemBookedProperty {
        return HoldeItemBookedProperty(LayoutInflater.from(activity!!).inflate(R.layout.item_booked, null))
    }

    override fun getItemCount(): Int {
        return bookedProperty.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HoldeItemBookedProperty, i: Int) {
        holder.parent_lay.layoutParams = Utils.setParamsForListViewInAll(i, bookedProperty.size, "c")

        val imgURL: String = BASE_URL_IMAGE + bookedProperty[i].landdetail.layout_image

        ImageUtils.setImageLive(holder.land_img, imgURL, activity)
        holder.booked_amount_booked.text = activity.getString(R.string.inr) + " " + bookedProperty[i].landdetail.booking_amount.trim()
        holder.booked_date.text = "Booked on " + DatetimeUtils.convertDates(bookedProperty[i].booking_date, "yyyy-MM-dd", "MMM dd,yyy")
        holder.land_status.text = bookedProperty[i].status

        holder.land_name_booked.text = bookedProperty[i].landdetail.layout_name + " - " + bookedProperty[i].plot_number
        holder.place_area_booked.text = bookedProperty[i].landdetail.layout_address
        holder.total_amount.text = activity.getString(R.string.inr) + " " + bookedProperty[i].amount + "/" + bookedProperty[i].square_feet + "sf"


        holder.parent_lay.setOnClickListener {

            if (SessionManager.getUserType(activity).equals(Utils.USER_CUSTOMER)) {
                if (bookedProperty[i].status.toLowerCase() != "Booked".toLowerCase()) {

                    val fragment = ViewRegisterAndDueDates()
                    val bundle: Bundle = Bundle()
                    bundle.putString("id", bookedProperty[i].id.toString())
                    bundle.putString("plot_number", bookedProperty[i].plot_number)
                    bundle.putString("plot_number", bookedProperty[i].plot_number)
                    fragment.arguments = bundle
                    FragmentCallUtils.passFragment(activity, fragment)
                } else {
                    MessageUtils.showToastMessage(activity, "This property not register")
                }
            } else if (SessionManager.getUserType(activity).equals(Utils.USER_MEDIATOR)) {

                MessageUtils.showToastMessage(activity, "This is Booked Mediator")
            }
        }

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
    }
}