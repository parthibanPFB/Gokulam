package real.estate.gokulam.model.payment.paymentHistory

data class ModelPaymentHistory(
        val `data`: Plotdetails,
        val message: String,
        val success: Boolean

)

class Plotdetails(val plotdetails: DataPaymentHistory, val date: Int,
                  val month: Int,
                  val year: Int, val payments: List<HistoryOfPayment>)


