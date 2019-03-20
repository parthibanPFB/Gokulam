package real.estate.gokulam.model.mediator.plotDetails

import real.estate.gokulam.model.Landdetail

data class Plot(
        val amount: String,
        val booking_amount: String,
        val booking_date: String,
        val collection_agent: Int,
        val created_at: String,
        val customer_id: Int,
        val due_amount: String,
        val due_date: String,
        val facing: String,
        val id: Int,
        val initial_pay: String,
        val interest: String,
        val landdetail_id: Int,
        val no_of_years: String,
        val payment_option: String,
        val plot_image: String,
        val plot_number: String,
        val register_by: Int,
        val register_date: String,
        val register_no: Any,
        val square_feet: String,
        val status: String,
        val updated_at: String,
        val user_id: Int,
        val utr_no: String,
        val landdetail:Landdetail
)