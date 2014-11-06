package comunication;

import android.content.Context;

import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by vssnake on 04/11/2014.
 */
@Singleton
public class LocalComunication extends ConnectionManager {

    HashMap<String,User> userMap = new HashMap<String, User>();
    HashMap<Long,Gift> giftMap = new HashMap<Long, Gift>();
    SpecialInfo special = new SpecialInfo();

    User loggedUser = null;

    public LocalComunication(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        userMap.put("virtual.solid.snake@gmail.com",
                new User("realkone@gmail.com","Realkone",null,null));
        userMap.put("pepitoGrillo@gmail.com",
                new User("pepitoGrillo@gmail.com","Pepito grillo",null,null));
        userMap.put("juanPalomo@gmail.com",
                new User("juanPalomo@gmail.com","Juan Palomo",null,null));

        giftMap.put(1l,new Gift(1l,"virtual.solid.snake@gmail.com",
                "River","The nightfall is beautifull",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test1,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test1_t));
        userMap.get("virtual.solid.snake@gmail.com").addGift(1l);

        giftMap.put(2l,new Gift(2l,"virtual.solid.snake@gmail.com",
                "CAT","CAT CAT CAT CAT CAT CAT CAT CAT",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test2,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test2_t));
        userMap.get("virtual.solid.snake@gmail.com").addGift(2l);

        giftMap.put(3l,new Gift(3l,"virtual.solid.snake@gmail.com",
                "Bear","I´m wanna Hug",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test3,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test3_t));
        userMap.get("virtual.solid.snake@gmail.com").addGift(3l);

        giftMap.put(4l,new Gift(4l,"pepitoGrillo@gmail.com",
                "Bird","Bird singing",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test4,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test4_t));
        userMap.get("pepitoGrillo@gmail.com").addGift(4l);

        giftMap.put(5l,new Gift(5l,"pepitoGrillo@gmail.com",
                "Night Sky","",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test5,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test5_t));
        userMap.get("pepitoGrillo@gmail.com").addGift(5l);

        loggedUser = userMap.get("virtual.solid.snake@gmail.com");
    }



    @Override
    public void register(@Body User user, Callback<User> userCallback) {
        if (!userMap.containsKey(user.getEmail())){
            userMap.put(user.getEmail(),user);
        }
        userCallback.success(userMap.get(user.getEmail()), null);
    }

    @Override
    public void login(String accessToken,String email,Callback<Boolean> successCallback) {
        if  (userMap.containsKey(email)){
            loggedUser = userMap.get(email);
            successCallback.success(true,null);
        }
        successCallback.success(false,null);
    }

    @Override
    public void logout(User user, Callback<Boolean> responseCallback) {
        loggedUser = null;
        responseCallback.success(userMap.containsKey(user.getEmail()), null);
    }

    @Override
    public void showUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<User> callbackUser) {
       callbackUser.success(userMap.get(email), null);
    }

    @Override
    public void updateUser(@Header(BEARER_TOKEN) String accessToken, Callback<User> callbackUser) {
        callbackUser.success(loggedUser,null);
    }

    @Override
    public void searchUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<String[]> emailList) {
        List<String> usersEmail = new ArrayList<String>();
        for (Map.Entry<String, User> e : userMap.entrySet()) {
            if (e.getKey().startsWith(email)) {
                usersEmail.add(e.getKey());
            }
        }
        emailList.success(usersEmail.toArray(new String[usersEmail.size()]), null);
    }

    @Override
    public void createGift(@Header(BEARER_TOKEN) String accessToken, @Body GiftCreator giftCreator, Callback<Gift> giftCallback) {
        //TODO need a file creator for local
    }

    @Override
    public void showGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {
        giftCallback.success(giftMap.get(idGift),null);
    }

    @Override
    public void searchGifts(@Header(BEARER_TOKEN) String accessToken, @Path("title") String title,
                         Callback<Gift[]> giftsCallback) {
        List<Gift> gifts = new ArrayList<Gift>();
        for (Map.Entry<Long, Gift> e : giftMap.entrySet()) {
            if (e.getValue().getTitle().startsWith(title)) {
                gifts.add(giftMap.get(e.getValue()));
            }
        }
        giftsCallback.success(gifts.toArray(new Gift[gifts.size()]), null);
    }
    @Override
    public void modifyLike(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {
        boolean increment;
        if (giftMap.containsKey(idGift)){
            Gift gift =  giftMap.get(idGift);
            if(loggedUser.giftLikeExist(idGift)) {
                increment = false;
                loggedUser.removeLike(gift.getId());
            }else {
                increment = true;
                loggedUser.addLike(gift.getId());
            }
            gift.incrementDecrementLike(increment);
            giftCallback.success(gift,null);

        }
    }

    @Override
    public void setObscene(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, boolean obscene, Callback<Gift> giftCallback) {
        if (giftMap.containsKey(idGift)) {
            Gift gift = giftMap.get(idGift);
            gift.setObscene(obscene);
            giftCallback.success(gift,null);
        }
    }

    @Override
    public void deleteGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Boolean> callbackSucces) {
        if (loggedUser.giftExist(idGift)){
            if(giftMap.containsKey(idGift)){
                giftMap.remove(idGift);
                loggedUser.removeGift(idGift);
                callbackSucces.success(true,null);
            }
        }
    }

    @Override
    public void showGifts(@Header(BEARER_TOKEN) String accessToken, @Query("start") int startGift, Callback<Gift[]> giftsCallback) {
           giftsCallback.success(giftMap.values().toArray(new Gift[giftMap.values().size()]),null);
    }

    @Override
    public void showGiftChain(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift[]> giftCallback) {
        List<Gift> gifts = new ArrayList<Gift>();
        if (giftMap.containsKey(idGift)) {
            Gift gift = giftMap.get(idGift);
            List<Long> giftChainIds = gift.getChainsID();
            for (int i =0; giftChainIds.size() > i; i++){

                gifts.add(giftMap.get(giftChainIds.get(i)));
            }
        }
        giftCallback.success(gifts.toArray(new Gift[gifts.size()]),null);
    }

    @Override
    public void showSpecialInfo(@Header(BEARER_TOKEN) String accessToken, @Query("day") String day, Callback<SpecialInfo> specialInfo) {
        specialInfo.success(special,null);

    }

























}
