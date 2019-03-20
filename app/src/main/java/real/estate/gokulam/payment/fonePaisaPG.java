package real.estate.gokulam.payment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import real.estate.gokulam.R;
import real.estate.gokulam.utils.MessageUtils;

/**
 * Created by nischal on 11/21/2016.
 */
public class fonePaisaPG extends AppCompatActivity {
    private String test_CBSU = "https://test.fonepaisa.com/portal/ASDPay/success";
    private String test_CBFU = "https://test.fonepaisa.com/portal/ASDPay/success";
    private String test_PGURL = "https://test.fonepaisa.com/pg/pay";
    private String prod_CBSU = "https://secure.fonepaisa.com/portal/ASDPay/success";
    private String prod_CBFU = "https://secure.fonepaisa.com/portal/ASDPay/success";
    private String prod_PGURL = "https://secure.fonepaisa.com/pg/pay";
    private String Environment = "";
    private String callback_failure_url = "";
    private String callback_success_url = "";
    private String PG_URL = "";
    private JSONObject json_to_be_sent_toPG = new JSONObject();
    private ImageButton close_btn;
    int PG_First_Load = 0;
    private WebView fPWV1;
    private Boolean webViewStarted = false;
    String identity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_fp);
        close_btn = findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new closeBtn());
        Intent intent = getIntent();
        String json_str_recieved = intent.getStringExtra("data");
        try {
            JSONObject json_obj = new JSONObject(json_str_recieved);
            String invoice_amt = json_obj.get("invoice_amt").toString();
             identity = json_obj.get("identity").toString();

            json_check(json_obj);
            Environment = json_obj.getString("Environment");
            Log.d("invoice_amtsds",""+invoice_amt+"  "+Environment+" json_obj "+json_obj.toString());

            if (Environment.equals(FPConstants.Production_Environment)) {
                callback_failure_url = prod_CBFU;
                callback_success_url = prod_CBSU;
                PG_URL = prod_PGURL;
            } else if (Environment.equals(FPConstants.Test_Environment)) {
                callback_failure_url = test_CBFU;
                callback_success_url = test_CBSU;
                PG_URL = test_PGURL;
            } else {
                returnTheactivity("8013", "TEST_OR_PROD key can accept only values TEST or PROD");
            }

            json_obj.put("callback_failure_url", callback_failure_url);
            json_obj.put("callback_url", callback_success_url);
            json_to_be_sent_toPG = json_obj;
            start_webView();
        } catch (JSONException e) {
            e.printStackTrace();
            returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
        }
    }

    private void json_check(JSONObject json_obj) {
        if (!json_obj.has("Environment")) {
            returnTheactivity("8001", "Environment is mandatory");
        }
        if (!json_obj.has("id")) {
            returnTheactivity("8002", "id is mandatory");
        }
        if (!json_obj.has("merchant_id")) {
            returnTheactivity("8003", "merchant_id is mandatory");
        }
        if (!json_obj.has("invoice")) {
            returnTheactivity("8004", "invoice is mandatory");
        }
        if (!json_obj.has("invoice_amt")) {
            returnTheactivity("8005", "invoice_amt is mandatory");
        }
        if (!json_obj.has("sign")) {
            returnTheactivity("8011", "sign is mandatory ");
        }
        try {
            if (json_obj.get("Environment").toString().equals("") || json_obj.get("Environment").toString().trim().isEmpty()) {
                returnTheactivity("8006", "Environment is mandatory");
            }
            if (json_obj.get("id").toString().equals("") || json_obj.get("id").toString().trim().isEmpty()) {
                returnTheactivity("8007", "id is mandatory");
            }
            if (json_obj.get("merchant_id").toString().equals("") || json_obj.get("merchant_id").toString().trim().isEmpty()) {
                returnTheactivity("8008", "merchant_id  is mandatory");
            }
            if (json_obj.get("invoice").toString().equals("") || json_obj.get("invoice").toString().trim().isEmpty()) {
                returnTheactivity("8009", "invoice is mandatory");
            }
            if (json_obj.get("invoice_amt").toString().equals("") || json_obj.get("invoice_amt").toString().trim().isEmpty()) {
                returnTheactivity("8010", "invoice_amt is mandatory");
            }
            if (json_obj.get("sign").toString().equals("") || json_obj.get("sign").toString().trim().isEmpty()) {
                returnTheactivity("8012", "sign is mandatory");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
        }
        return;
    }

    private void start_webView() {
        try {
            fPWV1 = findViewById(R.id.wv_fp);
            String request;
            fPWV1.addJavascriptInterface(new WebAppInterface(this), "Android");
            fPWV1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fPWV1.setWebContentsDebuggingEnabled(true);    //can be enabled to do chrome inspect
            }
            fPWV1.getSettings().setJavaScriptEnabled(true);
            fPWV1.setWebViewClient(new FPWebViewClient());
            request = getjsonString(json_to_be_sent_toPG);
            System.out.println(request);
            fPWV1.postUrl(PG_URL, request.getBytes());
            webViewStarted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class FPWebViewClient extends WebViewClient {
        // ProgressDialog progressDialog;
        Dialog dialog;

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            try {
                if (url.equals(PG_URL) && PG_First_Load == 0) {
                    dialog = MessageUtils.showDialog(fonePaisaPG.this);
                    /*if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(getApplicationContext());
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onPageFinished(WebView view, String url) {
            try {
                if (url.equals(PG_URL) && PG_First_Load == 0) {
                    try {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                            PG_First_Load++;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getjsonString(JSONObject json) {

        String Post_data = "";
        Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = json.get(key);
                if (Post_data.equals("")) {
                    Post_data = key + "=" + value;
                } else {
                    Post_data += "&" + key + "=" + value;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
            }
        }
        return Post_data;
    }


    public void returnTheactivity(String code, String message) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
        resultIntent.putExtra("resp_code", code);
        resultIntent.putExtra("resp_msg", message);
        resultIntent.putExtra("identity", identity);

        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    public class closeBtn implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
            resultIntent.putExtra("resp_code", "8000");
            resultIntent.putExtra("resp_msg", "customer cancelled the payment");
            resultIntent.putExtra("identity", identity);
            unregisterObjs();
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
        }
    }

    public void unregisterObjs() {
        try {

            if (webViewStarted) {
                fPWV1.destroy();
                fPWV1 = null;
                webViewStarted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("fonepaisaPG", "Failed to destroy object");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please click the close button on the top right corner to cancel the payment.", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void SuccessPayment(String json_data) {
            Log.d("vjson_data",""+json_data);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
            resultIntent.putExtra("data_recieved", json_data);
            try {
                JSONObject json = new JSONObject(json_data);
                if (json.get("status").equals("S")) {
                    resultIntent.putExtra("resp_code", "0000");
                    resultIntent.putExtra("resp_msg", "Payment Successful");
                    resultIntent.putExtra("identity", identity);
                    setResult(Activity.RESULT_OK,resultIntent);
                } else {
                    resultIntent.putExtra("resp_code", json.getString("error"));
                    resultIntent.putExtra("resp_msg", json.getString("error_msg"));
                    resultIntent.putExtra("identity", identity);
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                }
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
/*
    @Override
    protected void onDestroy() {
        unregisterObjs();
        super.onDestroy();
    }*/

}
