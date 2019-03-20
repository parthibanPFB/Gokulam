package real.estate.gokulam.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Formatter;

import real.estate.gokulam.api.RetrofitCall;
import real.estate.gokulam.api.RetrofitService;
import real.estate.gokulam.payment.FPConstants;


/**
 * Created by AND I5 on 30-03-2018.
 */

public class Utils {

    public static String PRIVACY_POLICY = "https://app-privacy-policy-generator.firebaseapp.com/#";  //Change your privacy policy URL
    public static final int PERMISSION_REQUEST_CODE = 1;
    //Live
    public static String BASE_PARENT = "http://35.154.245.147:8004/";
    //Demo
    //public static String BASE_PARENT = "http://192.168.2.9:8000/";  //vanitha
//    public static String BASE_PARENT = "http://192.168.2.4:8009/";  //Deepika
    public static String BASE_URL = BASE_PARENT + "api/real/";
    public static String BASE_URL_IMAGE = BASE_PARENT + "uploads/layout/";
    public static String BASE_URL_MEDIA = BASE_PARENT + "uploads/land/";

    public static final String CALL_PAY_MODE = FPConstants.Test_Environment;
    //public static final String CALL_PAY_MODE = FPConstants.Production_Environment;

    public static int FONEPAISAPG_RET_CODE = 1;
    public static int DUE_AMOUNT_PAYMENT = 2;


    public static String USER_CUSTOMER = "Customer";
    public static String USER_MEDIATOR = "Mediator";
    public static String USER_COLLECTION_AGENT = "2";//"Collection Agent";

    /**
     * Check Build Version
     *
     * @return
     */
    public static boolean checkVersion() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            //view.setBackgroundResource(R.drawable.ripple_center);
            return true;
        } else {
            return false;
        }
    }


    public static RetrofitService getInstance(FragmentActivity activity) {
        return RetrofitCall.getClient().create(RetrofitService.class);
    }


    public static ViewGroup.LayoutParams setParamsForListView(int position, ArrayList<Integer> items) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            //First One
            lp.setMargins(8, 8, 8, 3);
        } else if (position == (items.size() - 1)) {
            //Last One Item
            lp.setMargins(8, 3, 8, 8);
        } else {
            //Others
            lp.setMargins(8, 3, 8, 3);
        }

        return lp;
    }

    public static ViewGroup.LayoutParams setParamsForListViewInAll(int position, int itemSize, String view) {

        LinearLayout.LayoutParams lp = null;
        if (view.equals("l")) {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else if (view.equals("c")) {
            lp = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        if (position == 0) {
            //First One
            lp.setMargins(24, 24, 24, 12);
        } else if (position == (itemSize - 1)) {
            //Last One Item
            lp.setMargins(24, 12, 24, 24);
        } else {
            //Others
            lp.setMargins(24, 12, 24, 12);
        }

        return lp;
    }

    public static ViewGroup.LayoutParams setParamsForGridView(int position, int items) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            //First One
            lp.setMargins(24, 24, 12, 12);
        } else if (position == 1) {
            //Second One
            lp.setMargins(12, 24, 24, 12);
        } /*else if (position == (items - 1)) {
            //Last One Item
            lp.setMargins(12, 12, 24, 24);
        } else if (position == (items - 2)) {
            //Last Two Item
            lp.setMargins(24, 12, 12, 24);
        } */ else if ((position % 2) == 0) {
            if (position == (items - 2)) {
                lp.setMargins(24, 12, 12, 24);
            } else {
                //3,5,7..etc
                lp.setMargins(24, 12, 12, 12);
            }
        } else {
            //4,6,8..etc
            if (position == (items - 1)) {
                //Last One Item
                lp.setMargins(12, 12, 24, 24);
            } else {
                lp.setMargins(12, 12, 24, 12);
            }
        }

        return lp;
    }


    public static ViewGroup.LayoutParams setParamsForGridViewCons(int position, int items) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            //First One
            lp.setMargins(24, 24, 12, 12);
        } else if (position == 1) {
            //Second One
            lp.setMargins(12, 24, 24, 12);
        } /*else if (position == (items - 1)) {
            //Last One Item
            lp.setMargins(12, 12, 24, 24);
        } else if (position == (items - 2)) {
            //Last Two Item
            lp.setMargins(24, 12, 12, 24);
        } */ else if ((position % 2) == 0) {
            if (position == (items - 2)) {
                lp.setMargins(24, 12, 12, 24);
            } else {
                //3,5,7..etc
                lp.setMargins(24, 12, 12, 12);
            }
        } else {
            //4,6,8..etc
            if (position == (items - 1)) {
                //Last One Item
                lp.setMargins(12, 12, 24, 24);
            } else {
                lp.setMargins(12, 12, 24, 12);
            }
        }

        return lp;
    }

    public static ViewGroup.LayoutParams setParamsForGridViewThreeItem(int position, int itemsSize) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            //First One
            lp.setMargins(24, 24, 12, 12);
        } else if (position == 1) {
            //Second One
            lp.setMargins(12, 24, 12, 12);
        } else if (position == 2) {
            //Third One
            lp.setMargins(12, 24, 24, 12);
        }/* else if (position == (items.size() - 1)) {
            //Last One Item
            lp.setMargins(12, 12, 24, 24);
        } else if (position == (items.size() - 2)) {
            //Last Two Item
            lp.setMargins(12, 12, 12, 24);
        } else if (position == (items.size() - 3)) {
            //Last Third Item
            lp.setMargins(24, 12, 12, 24);
        } else if ((position % 3) == 3) {
            Log.d("sdsddd", "111   " + position);
            //3,6..etc
            lp.setMargins(12, 12, 24, 12);

        } */ else if (position % 3 == 1) {
            //4,7.etc

            lp.setMargins(12, 12, 12, 12);
        } else if (position % 3 == 0) {
            //5..etc

            lp.setMargins(24, 12, 12, 12);
        } else {
            lp.setMargins(12, 12, 24, 12);
        }

        return lp;
    }


    public static String getSignedMsg(String input) {
        try {
            /**************************************************************************************************************
             *                                    TODO                                                                     *
             *IMPORTANT : Signing of the message should be done in the backend to ensure that the keys are not compromised *
             *               and  can be modified on a regular  basis.                                                     *
             ***************************************************************************************************************/


            String privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDNzBfgbTSNp62zSYmScf9pwyr77QCEjGRvyuvHaRQ9AsPqYvxIwstar4WESLIKN9tUvvzE4xJGRuy7J0sGnehRom93fSWtdck6V+MCATfxUOfrRJCNqPEEcNi1809dcz1qsmUfZegG34p/ARURJ3bHAaVvS3YdX8tAwcnHFGX2QLl9/eX02qAZWbxpBFojsAlzpkF1E6NkaIaIiuymvPN1EmQQmeibeSUJlx6juUgtF2K9eS6hbke0khA2Upy+0nA6PLGDnpVuQnFpXG0UxiojLMdlh/Rv0oCadd2exYdGB2CT3q/NGHtDYSKnLU1xBImvS92nPMHVmO7gauoxKSQfAgMBAAECggEBAKM1BoJ/WLw2jHSxDx9KtOolU4NzU4PK6yQVY6NDXD9+X+0UD0uM4ETNCi/8juW3ooO06zUhd66wNLG/2aontMR487lpUGYeETXp2SgP21PPe/2C5LjTkECbVeIGUZyk9cIWNEgQQ1CgG2/ZZeGy0GnGjnKS/9sPy1tR1DnDnZEKG2jANGeE6H7ouYChFVwP85UEOgiHv2fmidRdfk5KrNw4prVelpznTOthKx/Zi+7q9oGkEie/PmE4hdCO1ucXUtOVxU3QMOBL0QWIhyBlWhTeY6nt7ayX4MnIweRsu4CQlP0LsrihmSfsEcgpIRteEQzhG9TuC1CVHvzI6XH2iQECgYEA6teV0RSCSUO9AB64A9p5wgB5V9+SkaBF3lCPfmJbOboQs5Jl0U5F3Ph7IfW1OzZDtqPThRfy+yV6+XWu3fczG3GP2OFCOrPlesmGXPTvWAWbfowEkiQCkddrRFmypI6QR2ZBItrHXhzPfR9Br8RybBzrLuUv7yho0BGCJ/ZnZ18CgYEA4FadXsVJnJxK65akI8ihGJpu1KgVmrKo6zYfplw3bdzoFPbLRAmuc09J9aQcuAQaCBEfguHeVNJb2CTAv90dx7E3gXHCDbW4SUcyybbTBBG5ZUIz4Ltv4LnLITqFKxiTpiOc+vi8+XFfOvDtqyXfBVVegIbV+xUiaOYPS4ULO0ECgYA0e1VZ0lGDegXk3viUs+B+AIkdoDMrJDw5AJvwzJ5Celh9KPxkGC/4v/cUkcqcnvXm/RmqJr4AblHbKfeYV0Qun+RbvYuFfuqL1DmY0IwkiaxETZo/5phEa3XnYnxP1iRcMHfiCC6B08Jy3edaFnbTvmq4ojNiKQ+zYBZMQ/671QKBgEX8/6+3YRXI9N626pJ3XzrrwzP5FHRk1Ko9AnbGQky2JHmV3Shm1NQIooxOHN+T+AMYRHpyuQhBcIHoRXIWK9pHAYgS03WvgcTqv3+K2B5m4S4kD0dHcsnrbOH6/dzKGBY2+hyaSWqQ4iLjU2KXuBJT5d23Mz7YAxoy3Aa1hSGBAoGAHJgJe7f6PT5BgeDZbC7yw1RcLyH7uFwMaTTZCHcnrsvz1FHbWLI4N5lCd08RBzyygEtIofQXGsxpE6wwyDGlnPHBZXnQsUWIm+eYQ4bl0O5mAunPVU9VWl6P8ynCoBqehbjAyy34b59XJ92gxcdK73ybVbHyemocPU4Fj8sp48o=";
            // Get private key from String
            PrivateKey pk = loadPrivateKey(privKey);

            Signature sign = Signature.getInstance("SHA512withRSA");
            sign.initSign(pk);
            //System.out.println("TEST3");
            sign.update(input.getBytes());
            byte[] hashBytes = sign.sign();
            return bytesToHexString(hashBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatter.close();
        return sb.toString();
    }


    static PrivateKey loadPrivateKey(String key64) {
        byte[] privkeybytes = Base64.decode(key64, Base64.NO_WRAP);
        KeyFactory fact;
        try {
            fact = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privkeybytes);
            PrivateKey privateKey1 = fact.generatePrivate(privateSpec);
            return privateKey1;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static void setMaxLength(int maxLength, EditText editText) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(filterArray);
    }


    public static boolean haveNetworkConnection(FragmentActivity fragmentActivity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
