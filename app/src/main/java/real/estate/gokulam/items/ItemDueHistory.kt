package real.estate.gokulam.items

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import customs.CustomTextView
import customs.Fonts
import real.estate.gokulam.R
import real.estate.gokulam.model.payment.paymentHistory.HistoryOfPayment
import real.estate.gokulam.utils.MessageUtils

class ItemDueHistory(var activity: FragmentActivity, val dueHistory: List<HistoryOfPayment>) : RecyclerView.Adapter<ItemDueHistory.ItemDueHistory>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemDueHistory {
        return ItemDueHistory(LayoutInflater.from(activity).inflate(R.layout.item_due_history, null))
    }

    override fun getItemCount(): Int {
        return dueHistory.size
    }

    override fun onBindViewHolder(holder: ItemDueHistory, i: Int) {

        holder.item_due_si_no.typeface = MessageUtils.setType(activity, Fonts.REGULAR)
        holder.item_due_date.typeface = MessageUtils.setType(activity, Fonts.REGULAR)
        holder.item_due_payment_type.typeface = MessageUtils.setType(activity, Fonts.REGULAR)
        holder.item_due_due_amount.typeface = MessageUtils.setType(activity, Fonts.REGULAR)

        holder.item_due_si_no.text = (i + 1).toString()
        holder.item_due_date.text = dueHistory[i].bookingdate
        holder.item_due_payment_type.text = dueHistory[i].payment_type
        holder.item_due_due_amount.text = activity.getString(R.string.inr) + " " + dueHistory[i].bookingprice
    }


    class ItemDueHistory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_due_si_no = itemView.findViewById(R.id.item_due_si_no) as CustomTextView
        var item_due_date = itemView.findViewById(R.id.item_due_date) as CustomTextView
        var item_due_due_amount = itemView.findViewById(R.id.item_due_due_amount) as CustomTextView
        var item_due_payment_type = itemView.findViewById(R.id.item_due_payment_type) as CustomTextView

    }
}