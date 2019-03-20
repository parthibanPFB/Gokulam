package real.estate.gokulam.model.modelBooking.bookedHistory

data class BookedHistory(
        val `data`: List<BookedPropertyData>,
        val message: String,
        val success: Boolean
)