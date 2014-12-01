package comunication.rest;

import com.google.gson.Gson;

import comunication.rest.model.OauthGoogleError;
import comunication.rest.model.OauthGoogleUserInfo;


/**
 * Created by unai on 09/10/2014.
 */
public class GoogleOauth {

    String urlUserInfo = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    Oauth2RestBuilder mRestBuilder;

    Gson gson;


    private OauthGoogleError error;
    
    public OauthGoogleUserInfo googleUserInfo;
    public OauthGoogleError googleGeneralError;
    public String networkError;


    public GoogleOauth(Oauth2RestBuilder restBuilder){
        mRestBuilder = restBuilder;
        gson = new Gson();
    }

    public int getUserInfo(String bearerToken){
        mRestBuilder.sendGetRequest(urlUserInfo+ bearerToken,"");
                if (mRestBuilder.code > 400){
                	googleGeneralError = gson.fromJson(mRestBuilder.result, OauthGoogleError.class);

                }else if (mRestBuilder.code > 0){
                	googleUserInfo =gson.fromJson(mRestBuilder.result,
                            OauthGoogleUserInfo.class);
                }else{
                	networkError = mRestBuilder.result;
                }
                
                return mRestBuilder.code;

           
    }


    public interface UserInfoHandler{
        void onUserInfoSucces(OauthGoogleUserInfo googleUserInfo);
        void onUserInfoFailed(OauthGoogleError googleGeneralError);
        void onNetWorkError(String error);
    }


}
