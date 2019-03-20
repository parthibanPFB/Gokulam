package real.estate.gokulam.views;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import real.estate.gokulam.R;
import real.estate.gokulam.chat.chatUtils.BaseApplication;
import real.estate.gokulam.items.customer.ItemLandProperty;
import real.estate.gokulam.model.Landdetail;
import real.estate.gokulam.model.property.property_1.ModelInfoForProperty;
import real.estate.gokulam.utils.FragmentCallUtils;
import real.estate.gokulam.utils.MessageUtils;
import real.estate.gokulam.utils.SessionManager;
import real.estate.gokulam.utils.Utils;
import real.estate.gokulam.views.propertyInfo.PropertyInformation;
import real.estate.gokulam.views.propertyInfo.media.SitePhotos;
import real.estate.gokulam.views.propertyInfo.media.SiteVideos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AND I5 on 09-03-2018.
 */

public class Home extends Fragment {
    private RecyclerView mListOfProprty;
    private WebView mapingWebView;
    private Dialog dialog, dialogWebView;
    private Snackbar snackbar;
    String flatId, areaName;
    private NestedScrollView mSanckView;
    private Landdetail landDetails;
    CardView map_lay;
    ImageView signImage, mPhotos, mVideos;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        assert getArguments() != null;
        flatId = getArguments().getString("flatId");
        areaName = getArguments().getString("areaName");

        ((MainActivity) Objects.requireNonNull(getActivity())).setNavigationBarDisable(areaName);
        if (MainActivity.action_settings != null) {
            MainActivity.action_settings.setVisible(false);
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapingWebView = view.findViewById(R.id.maping);
        mListOfProprty = view.findViewById(R.id.list_of_views);
        mSanckView = view.findViewById(R.id.snack_viewhome);
        map_lay = view.findViewById(R.id.map_lay);

        signImage = view.findViewById(R.id.sign);
        mPhotos = view.findViewById(R.id.images);
        mVideos = view.findViewById(R.id.video);
        Button button = view.findViewById(R.id.button);

        mListOfProprty.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mListOfProprty.setHasFixedSize(false);
        WebSettings webSettings = mapingWebView.getSettings();
        mapingWebView.addJavascriptInterface(new WebAppInterface(), "Android");
        mapingWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mapingWebView.setWebContentsDebuggingEnabled(true);    //can be enabled to do chrome inspect
        }

        webSettings.setJavaScriptEnabled(true);
        mapingWebView.loadUrl(BaseApplication.APP_ID + "layout_image1");
        mapingWebView.setWebViewClient(new WebViewCliend());
        mListOfProprty.setVisibility(View.GONE);
        mapingWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });


        mPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentCallUtils.passFragment(getActivity(), new SitePhotos());
                Intent intent = new Intent(getActivity(), SitePhotos.class);
                intent.putExtra("landId", flatId);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end);
            }
        });


        mVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentCallUtils.passFragment(getActivity(), new SiteVideos());
                Intent intent = new Intent(getActivity(), SiteVideos.class);
                intent.putExtra("landId", flatId);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end);


            }
        });


        final SignaturePad mSignaturePad = view.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signImage.setImageBitmap(mSignaturePad.getSignatureBitmap());

                Log.d("signImagesdsd", mSignaturePad.getSignatureBitmap() + "\n" + mSignaturePad.getSignatureSvg() + " transd " + mSignaturePad.getTransparentSignatureBitmap());
            }
        });

/*

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        long currentTime = System.currentTimeMillis();
        long oneMinute = 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                currentTime + oneMinute,
                oneMinute,
                pendingIntent);
*/


        view.findViewById(R.id.map_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (landDetails != null) {
                        String latLang = landDetails.getLattitude() + "," + landDetails.getLongitude();
                        Log.d("latLangsdsd", "" + latLang);
                        String mapa = "http://maps.google.com/maps?q=loc:" + latLang;
                        //Uri gmmIntentUri = Uri.parse("geo:11.247268683, 77.004409&q=11.247268683, 77.004409");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapa));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        getActivity().overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end);
                    }
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.haveNetworkConnection(getActivity())) {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("property_id", flatId);
            callPropertyInfo(stringObjectHashMap);
        } else {
            try {
                snackbar = MessageUtils.showSnackBarAction(getActivity(), mSanckView, getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void callPropertyInfo(HashMap<String, Object> hashMap) {
        Utils.getInstance(getActivity()).callWebApi("propertydetail", SessionManager.getToken(getActivity()), hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                MessageUtils.dissmiss(dialog);
                try {
                    if (response.isSuccessful()) {
                        ModelInfoForProperty modelInfoForProperty = new Gson().fromJson(response.body().string(), ModelInfoForProperty.class);

                        if (modelInfoForProperty.getSuccess()) {
                            modelInfoForProperty.getData().component2();
                            int count = modelInfoForProperty.getData().getPlotdetails().size();
                            int countLand = modelInfoForProperty.getData().getLanddetails().size();
                            if (countLand != 0) {
                                landDetails = modelInfoForProperty.getData().getLanddetails().get(0);
                            }
                            if (count != 0) {
                                ItemLandProperty itemLandProperty = new ItemLandProperty(Objects.requireNonNull(getActivity()), modelInfoForProperty.getData().component2(), modelInfoForProperty.getData().component1());
                                mListOfProprty.setAdapter(itemLandProperty);
                            } else {
                                snackbar = MessageUtils.showSnackBar(Objects.requireNonNull(getActivity()), mSanckView, modelInfoForProperty.getMessage());
                            }
                        } else {
                            //String msg = MessageUtils.setErrorMessage(response.code());
                            snackbar = MessageUtils.showSnackBar(Objects.requireNonNull(getActivity()), mSanckView, modelInfoForProperty.getMessage());
                        }
                    } else {
                        String msg = MessageUtils.setErrorMessage(response.code());
                        snackbar = MessageUtils.showSnackBar(getActivity(), mSanckView, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MessageUtils.dissmiss(dialog);
                try {
                    String msg = MessageUtils.setFailurerMessage(getActivity(), t.getMessage());
                    snackbar = MessageUtils.showSnackBar(getActivity(), mSanckView, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class WebAppInterface {
        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Log.d("toastsdsd", "" + toast);
            //Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();


            if (!toast.isEmpty()) {
/*                Intent intent = new Intent(getActivity(), PropertyInformation.class);
                intent.putExtra("id", toast);
                startActivity(intent);
               getActivity().overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end);*/


                Fragment fragment = new PropertyInformation();
                Bundle bundle = new Bundle();
                bundle.putString("id", toast);
                fragment.setArguments(bundle);
                FragmentCallUtils.passFragment(getActivity(), fragment);
            } else {
                snackbar = MessageUtils.showSnackBar(getActivity(), mSanckView, "Property Already Booked");
            }
        }
    }

    public class WebViewCliend extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            MessageUtils.dissmiss(dialogWebView);
            mListOfProprty.setVisibility(View.VISIBLE);
            map_lay.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dialogWebView = MessageUtils.showDialog(getActivity());
            map_lay.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
