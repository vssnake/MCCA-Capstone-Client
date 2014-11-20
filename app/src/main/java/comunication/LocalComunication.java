package comunication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;
import com.vssnake.potlach.testing.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by vssnake on 04/11/2014.
 */
@Singleton
public class LocalComunication implements  RetrofitInterface{

    HashMap<String,User> userMap = new HashMap<String, User>();
    HashMap<Long,Gift> giftMap = new HashMap<Long, Gift>();
    SpecialInfo special = new SpecialInfo();
    User mainUser;
    Context mContext;

    public LocalComunication(Context context){
        init(context);
    }

    private void init(Context context){

        mContext = context;
        userMap.put("virtual.solid.snake@gmail.com",
                new User("virtual.solid.snake@gmail.com","Realkone",true,null,null,
                        "http://img2.meristation.com/files/imagenes/general/mgs4_estara_disponible_manana_martes_17_en_espana_.28276.jpg"
                        ));
        userMap.put("pepitoGrillo@gmail.com",
                new User("pepitoGrillo@gmail.com","Pepito grillo",true,null,null,
                        "http://eidease.com/wp-content/uploads/2014/02/4466c312099b30875585341d15e7a0c0.png"));
        userMap.put("juanPalomo@gmail.com",
                new User("juanPalomo@gmail.com","Juan Palomo",true,null,null,
                        "http://img.desmotivaciones.es/201012/Juanpalomo_1.jpg"));
        userMap.put("aranoka@gmail.com",new User("aranoka@gmail.com","Lorena",true,null,null,
                "http://img.desmotivaciones.es/201103/lamonavestidadeseda.jpg"));

        giftMap.put(1l,new Gift(1l,"virtual.solid.snake@gmail.com",
                "River","The nightfall is beautifull",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test1,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test1_t));
        userMap.get("virtual.solid.snake@gmail.com").addGift(1l);

        giftMap.put(2l,new Gift(2l,"aranoka@gmail.com",
                "CAT","CAT CAT CAT CAT CAT CAT CAT CAT",
                "android.resource://com.vssnake" +
                        ".potlach/"+ R.drawable.test2,
                "android.resource://com.vssnake" +
                ".potlach/"+ R.drawable.test2_t));
        userMap.get("aranoka@gmail.com").addGift(2l);

        giftMap.put(3l,new Gift(3l,"virtual.solid.snake@gmail.com",
                "Bear","I wanna Hug",
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

    }






    @Override
    public void register(@Body User user, Callback<User> userCallback) {
        if (!userMap.containsKey(user.getEmail())){
            userMap.put(user.getEmail(),user);
        }
        userCallback.success(userMap.get(user.getEmail()), null);
    }

    @Override
    public void login(String accessToken,String email,Callback<User> successCallback) {
        if  (userMap.containsKey(email)){
            mainUser = userMap.get(email);
            successCallback.success(userMap.get(email),null);
        }else{
            successCallback.success(null,null);
        }

    }

    @Override
    public void logout(User user, Callback<Boolean> responseCallback) {
        responseCallback.success(userMap.containsKey(user.getEmail()), null);
    }

    @Override
    public void showUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, Callback<User> callbackUser) {
       callbackUser.success(userMap.get(email), null);
    }

    @Override
    public void modifyInappropriate(@Header(BEARER_TOKEN) String accessToken, Boolean inappropriate, Callback<Boolean> callbackResult) {
       mainUser.setInappropriate(inappropriate);
        callbackResult.success(inappropriate,null);
    }


    @Override
    public void searchUser(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email,
                           Callback<User[]> emailList) {
        List<User> users = new ArrayList<User>();
        for (Map.Entry<String, User> e : userMap.entrySet()) {
            if (e.getKey().startsWith(email)) {
                users.add(e.getValue());
            }
        }
        emailList.success(users.toArray(new User[users.size()]), null);
    }

    private  String  saveNewPhoto(Context context,File photo,String sufix){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + sufix + ".jpg";


        File potlachBase = new File(Environment.getExternalStorageDirectory() +
                "/Potlach/LocalPictures/");
        potlachBase.mkdirs();

        File potlachImage = new File(potlachBase,imageFileName);


        OutputStream out = null;
        try {
            potlachImage.createNewFile();
            InputStream in = new FileInputStream(photo);
            out = new FileOutputStream(potlachImage);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(potlachImage).toString();




    }

    @Override
    public void createGift(@Header(BEARER_TOKEN)String accessToken,
                           @Part("gift") GiftCreator giftCreator,
                           Long idChain,
                           Callback<Gift> giftCallback) {
        if(userMap.containsKey(giftCreator.getUserEmail())){

            User user = userMap.get(giftCreator.getUserEmail());

            String photoUri = saveNewPhoto(mContext,giftCreator.getImage().file(),"");
            String photoThumbUri = saveNewPhoto(mContext,giftCreator.getImageThumb().file(),
                    "thumb");

            Gift gift = new Gift(giftMap.size() + 1l,
                    giftCreator,
                    photoUri,
                    photoThumbUri);

            giftMap.put(gift.getId(),gift);
            giftMap.get(idChain).addNewChain(gift.getId());
            user.addGift(gift.getId());
            giftCallback.success(gift,null);

        }
    }

    @Override
    public void showGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {
        giftCallback.success(giftMap.get(idGift),null);
    }

    @Override
    public void showUserGifts(@Header(BEARER_TOKEN) String accessToken, @Path("email") String email, final Callback<Gift[]> giftsCallback) {
        final List<Gift> gifts = new ArrayList<Gift>();
        showUser("",email,new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                List<Long> idGiftsUser = user.getGiftPosted();
                for (int i = 0; i<idGiftsUser.size();i++){
                    if(giftMap.containsKey(idGiftsUser.get(i))){
                        gifts.add(giftMap.get(idGiftsUser.get(i)));
                    }
                }
                giftsCallback.success(gifts.toArray(new Gift[gifts.size()]),null);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
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
    public void modifyLike(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift,
                           User loggedUser, Callback<Gift> giftCallback) {
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
    public void setObscene(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift, Callback<Gift> giftCallback) {
        if (giftMap.containsKey(idGift)) {
            Gift gift = giftMap.get(idGift);
            gift.setObscene(!gift.getObscene());
            giftCallback.success(gift,null);
        }
    }

    @Override
    public void deleteGift(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift,
                           User loggedUser, Callback<Boolean> callbackSuccess) {
        if (loggedUser.giftExist(idGift)){
            if(giftMap.containsKey(idGift)){
                giftMap.remove(idGift);
                loggedUser.removeGift(idGift);
                callbackSuccess.success(true,null);
            }
        }
    }

    @Override
    public void showGifts(@Header(BEARER_TOKEN) String accessToken, @Query("start") int startGift, Callback<Gift[]> giftsCallback) {
        Gift[] values = giftMap.values().toArray(new Gift[giftMap.values().size()]);
        List<Gift> gifts = new ArrayList<Gift>();
        for (int i = startGift; i<values.length;i++){
            gifts.add(values[i]);
        }
           giftsCallback.success(gifts.toArray(new Gift[gifts.size()]),null);
    }

    @Override
    public void showGiftChain(@Header(BEARER_TOKEN) String accessToken, @Path("id") Long idGift,
                               Callback<Gift[]> giftCallback) {
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
