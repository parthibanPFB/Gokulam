package real.estate.gokulam.model.modelBooking.bookedHistory

import real.estate.gokulam.model.Landdetail

data class BookedPropertyData(
        val amount: String,
        val booking_amount: String,
        val booking_date: String,
        val created_at: String,
        val customer_id: Int,
        val facing: String,
        val id: Int,
        val landdetail: Landdetail,
        val landdetail_id: Int,
        val plot_image: String,
        val plot_number: String,
        val square_feet: String,
        val status: String,
        val updated_at: String,
        val user_id: Int,
        val payment_option: String,
        val initial_pay: String,
        val no_of_years: String,
        val interest: String,
        val due_amount: String,
        val due_date: String,
        val utr_no: String,
        val collection_agent: String,
        val register_date: String,
        val register_by: String,
        val register_no: String


)