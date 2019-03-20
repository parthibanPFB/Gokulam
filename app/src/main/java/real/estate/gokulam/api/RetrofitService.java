package real.estate.gokulam.api;


import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import real.estate.gokulam.model.login.ModelLogin;
import real.estate.gokulam.model.logout.ModelLogout;
import real.estate.gokulam.model.property.property_2.ModelProperty;
import real.estate.gokulam.model.register.ModelRegister;
import real.estate.gokulam.model.register.mediatorNames.ModelMediatorName;
import real.estate.gokulam.model.versionUpdate.ModelVersionUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {


    @POST("forceupdate")
    Call<ModelVersionUpdate> checkVersionForUpdate();

    @POST("mediatorlist")
    Call<ModelMediatorName> onCallMediatorName();

    @POST("register")
    Call<ModelRegister> onCallRegister(@Body HashMap<String, String> hashMap);


    @POST("login")
    Call<ModelLogin> onCallLogin(@Body HashMap<String, String> hashMap);


    @FormUrlEncoded
    @POST("logout")
    Call<ModelLogout> onCallLogout(@Field("userid") String id);



    @FormUrlEncoded
    @POST("plotdetail")
    Call<ModelProperty> onCallPropertyInfo(@Header("Authorization") String auth, @Field("plot_id") String property_id, @Field("userId") String userId);



    @POST("{path}")
    Call<ResponseBody> callWebApi(@Path("path") String path, @Header("Authorization") String auth, @Body HashMap<String, Object> map);

    /**
     * Called WebApi After Payment Success with Body is JsonObject
     *
     * @param path
     * @param auth
     * @param jsonObject
     * @return
     */

    @POST("{path}")
    Call<ResponseBody> callWebApi(@Path("path") String path, @Header("Authorization") String auth, @Body JsonObject jsonObject);


  /*  @FormUrlEncoded
    @POST("aazp_ticket_status.php")
    Call<ModelPaymentDetails> onPaymentDetails(@Field("ticket_number") String paymentId);*/
}
