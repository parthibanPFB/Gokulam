package real.estate.gokulam.model.payment;

import com.google.gson.annotations.SerializedName;

public class ModelPaymentInfoPass {


    @SerializedName("code")
    public String mCode;
    @SerializedName("message")
    public String mMessage;
    @SerializedName("status")
    public boolean mStatus;

    @SerializedName("result")
    public PaymentResult paymentResult;

    class PaymentResult {
        @SerializedName("Invoice")
        public String Invoice;
        @SerializedName("Amount")
        public String Amount;
        @SerializedName("Payment_status")
        public String Payment_status;
    }
}
