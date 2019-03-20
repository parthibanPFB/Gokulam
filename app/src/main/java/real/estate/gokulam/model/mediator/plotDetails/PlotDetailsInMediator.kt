package real.estate.gokulam.model.mediator.plotDetails

data class PlotDetailsInMediator(
        val message: String,
        val plots: ArrayList<Plot>,
        val status: Boolean
)