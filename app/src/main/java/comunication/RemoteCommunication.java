package comunication;

import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by vssnake on 08/11/2014.
 */
public class RemoteCommunication  implements  RetrofitInterface{
    @Override
    public void register(@Body User user, Callback<User> userCallback) {

    }

    @Override
    public void login(@Header(BEARER_TOKEN) String accessToken, String email, Callback<User> userCallback) {

    }

    @Override
    public void logout(User user, Callback<Boolean> responseCallback) {

    }

    @Override
    public void showUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<User> callbackUser) {

    }

    @Override
    public void searchUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<User[]> emailList) {

    }

    @Override
    public void createGift(@Header(BEARER_TOKEN) String accessToken, @Part("gift") GiftCreator giftCreator, Long idChain, Callback<Gift> giftCallback) {

    }


    @Override
    public void showGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {

    }

    @Override
    public void showUserGifts(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<Gift[]> giftsCallback) {

    }

    @Override
    public void searchGifts(@Header(BEARER_TOKEN) String accessToken, @Path("title") String title, Callback<Gift[]> giftsCallback) {

    }

    @Override
    public void modifyLike(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, User user, Callback<Gift> giftCallback) {

    }

    @Override
    public void setObscene(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {

    }

    @Override
    public void deleteGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, User loggedUser, Callback<Boolean> callbackSuccess) {

    }

    @Override
    public void showGifts(@Header(BEARER_TOKEN) String accessToken, @Query("start") int startGift, Callback<Gift[]> giftsCallback) {

    }

    @Override
    public void showGiftChain(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift[]> giftCallback) {

    }

    @Override
    public void showSpecialInfo(@Header(BEARER_TOKEN) String accessToken, @Query("day") String day, Callback<SpecialInfo> specialInfo) {

    }
}
