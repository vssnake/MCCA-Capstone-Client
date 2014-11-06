package comunication;

import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by vssnake on 04/11/2014.
 */
public interface ComInterface{
    public static final String BEARER_TOKEN ="bearer_token";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String GET_USER = "/user/{email}";
    public static final String CURRENT_USER = "/user";
    public static final String SEARCH_USER = "/search/user/{email}";
    public static final String GIFT_CREATE = "/gift/create/";
    public static final String GIFT_SHOW = "/gift/{id}";
    public static final String GIFTS_SHOW = "/gifts";
    public static final String GIFT_LIKE = "/gift/{id}/like";
    public static final String GIFT_OBSCENE = "/gift/{id}/obscene";
    public static final String GIFT_DELETE = "/gift/{id}/delete";
    public static final String GIFT_SEARCH = "/gift/search/{title}";
    public static final String GIFT_CHAIN = "/gift/chain/{id}";
    public static final String SPECIAL_INFO = "/special";

    /**
     * Register a user
     * @param user
     * @param userCallback
     */
    @POST(REGISTER)
    public void register(@Body User user,Callback<User>userCallback);

    /**
     * Login and register in one steep
     * @param accessToken
     * @param email
     * @return
     */
    @GET(LOGIN)
    public void login(@Header(BEARER_TOKEN)String accessToken,String email,
                      Callback<Boolean> successCallback);

    /**
     *  Logout the user
     * @param user
     * @param responseCallback
     * @return
     */
    @POST(LOGOUT)
    public void logout(User user,Callback<Boolean> responseCallback);

    /**
     * Get a one callbackUser Data
     * @param accessToken
     * @param email
     * @param callbackUser
     */
    @GET(GET_USER)
    public void showUser(@Header(BEARER_TOKEN)String accessToken, @Path("email")String email,Callback<User> callbackUser);

    /**
     * Update callbackUser
     * @param accessToken
     * @param callbackUser
     * @return
     */
    @GET(CURRENT_USER)
    public void updateUser(@Header(BEARER_TOKEN)String accessToken,Callback<User> callbackUser);

    /**
     * Find a user with the email or part of it.
     * @param accessToken
     * @param email
     * @param emailList
     */
    @GET(SEARCH_USER)
    public void searchUser(@Header(BEARER_TOKEN)String accessToken, @Path("email") String email,
                               Callback<String[]> emailList);

    /**
     * Create a gift
     * @param accessToken
     * @param giftCreator
     * @param giftCallback
     */
    @POST(GIFT_CREATE)
    public void createGift(@Header(BEARER_TOKEN)String accessToken,@Body GiftCreator giftCreator,
                           Callback<Gift> giftCallback);

    /**
     *
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_SHOW)
    public void showGift(@Header(BEARER_TOKEN)String accessToken,@Path("id")Long idGift,
                      Callback<Gift> giftCallback);

    /**
     * Search gift by  title
     * @param accessToken
     * @param title
     * @param giftsCallback
     * @return
     */
    @GET(GIFT_SEARCH)
    public void searchGifts(@Header(BEARER_TOKEN)String accessToken,@Path("title")String title,
                              Callback<Gift[]> giftsCallback);

    /**
     * Add or delete like, depends of the previous mark
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_LIKE)
    public void modifyLike(@Header(BEARER_TOKEN)String accessToken,@Path("id")Long idGift,
                           Callback<Gift> giftCallback);


    /**
     * Mark or UnMark the gift with obscene
     * @param accessToken
     * @param idGift
     * @param obscene
     * @param giftCallback
     */
    @GET(GIFT_OBSCENE)
    public void setObscene(@Header(BEARER_TOKEN)String accessToken,@Path("id")Long idGift,
                           boolean obscene,Callback<Gift> giftCallback);

    /**
     * Delete gift only if the user is the owner
     * @param accessToken
     * @param idGift
     * @param callbackSucces
     */
    @GET(GIFT_DELETE)
    public void deleteGift(@Header(BEARER_TOKEN)String accessToken,@Path("id")Long idGift,
                              Callback<Boolean> callbackSucces);

    /**
     * Show last gifts
     * @param accessToken
     * @param startGift
     * @param giftsCallback
     */
    @GET(GIFTS_SHOW)
    public void showGifts(@Header(BEARER_TOKEN)String accessToken,
                            @Query("start")int startGift,Callback<Gift[]> giftsCallback);

    /**
     * Show a gift chain of one gift
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_CHAIN)
    public void showGiftChain(@Header(BEARER_TOKEN)String accessToken,@Path("id")Long idGift,
                                Callback<Gift[]> giftCallback);

    /**
     * Show special info of the day
     * @param accessToken
     * @param day
     * @param specialInfo
     */
    @GET(SPECIAL_INFO)
    public void showSpecialInfo(@Header(BEARER_TOKEN)String accessToken,
                                       @Query("day")String day, Callback<SpecialInfo> specialInfo);


}
