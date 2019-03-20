package real.estate.gokulam.model.modelBooking;

import com.google.gson.annotations.SerializedName;

public class ModelBooking {
    @SerializedName("message")
    public String mMessage;
    @SerializedName("result")
    public InsertResult mResult;
    @SerializedName("status")
    public boolean mStatus;
}
