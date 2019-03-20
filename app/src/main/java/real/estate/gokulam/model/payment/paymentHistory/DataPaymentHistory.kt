package real.estate.gokulam.model.payment.paymentHistory

import real.estate.gokulam.model.Landdetail

data class DataPaymentHistory(
        val amount: Double,
        val booking_amount: Double,
        val booking_date: String,
        val collection_agent: Int,
        val created_at: String,
        val customer_id: Int,
        val due_amount: Double,
        val due_date: String,
        val facing: String,
        val id: Int,
        val interest: Double,
        val initial_pay: Double,
        val landdetail_id: Int,
        val no_of_years: Double,
        val payment_option: String,
        val plot_image: String,
        val plot_number: String,
        val register_by: Int,
        val register_date: String,
        val register_no: String,
        val square_feet: String,
        val status: String,
        val updated_at: String,
        val user_id: Int,
        val utr_no: String,
        val landdetail: Landdetail)