package real.estate.gokulam.model.modelBooking;

import com.google.gson.annotations.SerializedName;

public class InsertResult {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("hash_key")
    private String mHashKey;
    @SerializedName("id")
    private String mId;
    @SerializedName("invoice")
    private String mInvoice;
    @SerializedName("invoice_amt")
    private String mInvoiceAmt;
    @SerializedName("merchant_display")
    private String mMerchantDisplay;
    @SerializedName("merchant_id")
    private String mMerchantId;
    @SerializedName("mobile_no")
    private String mMobileNo;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getHashKey() {
        return mHashKey;
    }

    public void setHashKey(String hashKey) {
        mHashKey = hashKey;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInvoice() {
        return mInvoice;
    }

    public void setInvoice(String invoice) {
        mInvoice = invoice;
    }

    public String getInvoiceAmt() {
        return mInvoiceAmt;
    }

    public void setInvoiceAmt(String invoiceAmt) {
        mInvoiceAmt = invoiceAmt;
    }

    public String getMerchantDisplay() {
        return mMerchantDisplay;
    }

    public void setMerchantDisplay(String merchantDisplay) {
        mMerchantDisplay = merchantDisplay;
    }

    public String getMerchantId() {
        return mMerchantId;
    }

    public void setMerchantId(String merchantId) {
        mMerchantId = merchantId;
    }

    public String getMobileNo() {
        return mMobileNo;
    }

    public void setMobileNo(String mobileNo) {
        mMobileNo = mobileNo;
    }
}
