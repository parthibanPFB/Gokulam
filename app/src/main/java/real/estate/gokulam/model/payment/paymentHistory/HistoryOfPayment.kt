package real.estate.gokulam.model.payment.paymentHistory

data class HistoryOfPayment(
        val bookingcategory: String,
        val bookingdate: String,
        val bookingprice: String,
        val created_at: String,
        val id: Int,
        val invoice_amount: String,
        val invoice_no: Int,
        val invoiceid: String,
        val merchant_id: String,
        val merchantname: String,
        val payment_detail: Any,
        val payment_reference: Any,
        val payment_status: String,
        val payment_type: String,
        val plot_id: Int,
        val signature_image: String,
        val updated_at: String,
        val user_id: Int
)