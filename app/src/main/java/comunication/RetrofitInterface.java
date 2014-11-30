package comunication;

import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by vssnake on 04/11/2014.
 */
public interface RetrofitInterface {
    public static final String BEARER_TOKEN ="bearer_token";
    public static final String HEADER_EMAIL = "email";
    public static final String HEADER_START = "start";
    public static final String HEADER_INNA = "inappropriate";
    public static final String HEADER_GIFT_CHAIN = "idChain";
    public static final String HEADER_GIFT_ID = "giftID";
    public static final String HEADER_GIFT_TITLE = "giftTitle";
    public static final String HEADER_GCM_CLIENT_KEY = "gcmKey";

    public static final String HEADER_GC_LATITUDE = "gcLatitude";
    public static final String HEADER_GC_LONGITUDE = "gcLongitude";
    public static final String HEADER_GC_PRECISION = "gcPrecision";
    public static final String HEADER_GC_TITLE = "gcTitle";
    public static final String HEADER_GC_DESCRIPTION = "gcDescription";
    public static final String GC_MULTI_IMAGE = "gcMultiImage";
    public static final String GC_MULTI_IMAGE_THUMB = "gcMultiImageThumb";

    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    public static final String LOGOUT = "/logout";
    public static final String GET_USER = "/user";
    public static final String CURRENT_USER_MODIF_INA = "/user/inna"; //Modify inappropriate
    public static final String USER_GIFTS = "/user/gifts";
    public static final String SEARCH_USER = "/user/{email}";
    public static final String GIFT_CREATE = "/gift/create/";
    public static final String GIFT_SHOW = "/gift/";
    public static final String GIFTS_SHOW = "/gift";
    public static final String GIFT_LIKE = "/gift/like";
    public static final String GIFT_OBSCENE = "/gift/obscene";
    public static final String GIFT_DELETE = "/gift/delete";
    public static final String GIFT_SEARCH = "/gift/search";
    public static final String GIFT_CHAIN = "/gift/chain";
    public static final String SPECIAL_INFO = "/special";

    public static final String TITLE_PARAMETER = "title";
    public static final String START_PARAMETER = "start";

    public static final String LINK_IMAGE = "/photo";

    public static final String PATH_IMAGE = "image";
    public static final String PATH_GIFT_ID = "giftID";

    public static final String PART_GIFT = "giftPart";

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
    public void login(@Header(BEARER_TOKEN)String accessToken,
                      @Header(HEADER_EMAIL)String email,
                      @Header(HEADER_GCM_CLIENT_KEY) String gmcKey,
                      Callback<User> userCallback);

    /**
     *  Logout the user
     * @param responseCallback
     * @return
     */
    @POST(LOGOUT)
    public void logout(@Header(BEARER_TOKEN)String accessToken,
                       @Header(HEADER_GCM_CLIENT_KEY) String gmcKey,
                       Callback<Boolean> responseCallback);

    /**
     * Get a one callbackUser Data
     * @param accessToken
     * @param email
     * @param callbackUser
     */
    @GET(GET_USER)
    public void showUser(@Header(BEARER_TOKEN)String accessToken, @Header(HEADER_EMAIL)String email,
                         Callback<User> callbackUser);

    /**
     * Change the inappropriate user status
     * @param accessToken
     * @param inappropriate
     * @param callbackResult
     */
    @PUT(CURRENT_USER_MODIF_INA)
    public void modifyInappropriate(@Header(BEARER_TOKEN)String accessToken,
                                    @Header(HEADER_INNA)Boolean inappropriate,
                                            Callback<Boolean> callbackResult);

    /**
     * Find a user with the email or part of it.
     * @param accessToken
     * @param email
     * @param emailList
     */
    @GET(SEARCH_USER)
    public void searchUser(@Header(BEARER_TOKEN)String accessToken, @Path("email") String email,
                               Callback<User[]> emailList);

    /**
     * Create a gift
     * @param accessToken
     * @param giftCallback
     */
    @Multipart
    @POST(GIFT_CREATE)
    public void createGift(@Header(BEARER_TOKEN)String accessToken,
                           @Header(HEADER_GIFT_CHAIN) Long idChain,
                           @Header(HEADER_GC_TITLE) String title,
                           @Header(HEADER_GC_DESCRIPTION) String description,
                           @Header(HEADER_GC_LATITUDE) Double  latitude,
                           @Header(HEADER_GC_LONGITUDE) Double longitude,
                           @Header(HEADER_GC_PRECISION) Float precision,
                           @Part(GC_MULTI_IMAGE)TypedFile photo,
                           @Part(GC_MULTI_IMAGE_THUMB)TypedFile photoThumb,
                           Callback<Gift> giftCallback);

    /**
     *
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_SHOW)
    public void showGift(@Header(BEARER_TOKEN)String accessToken,@Header(HEADER_GIFT_ID)Long idGift,
                      Callback<Gift> giftCallback);

    /**
     * Get the user gifts
     * @param accessToken
     * @param email
     * @param giftsCallback
     */
    @GET(USER_GIFTS)
    public void showUserGifts(@Header(BEARER_TOKEN)String accessToken,
                              @Header(HEADER_EMAIL)String email,
                              Callback<Gift[]> giftsCallback);
    /**
     * Search gift by  title
     * @param accessToken
     * @param title
     * @param giftsCallback
     * @return
     */
    @GET(GIFT_SEARCH)
    public void searchGifts(@Header(BEARER_TOKEN)String accessToken,
                            @Header(HEADER_GIFT_TITLE)String title,
                              Callback<Gift[]> giftsCallback);

    /**
     * Add or delete like, depends of the previous mark
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_LIKE)
    public void modifyLike(@Header(BEARER_TOKEN)String accessToken,
                           @Header(HEADER_GIFT_ID)Long idGift,
                           Callback<Gift> giftCallback);


    /**
     * Mark or UnMark the gift with obscene
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_OBSCENE)
    public void setObscene(@Header(BEARER_TOKEN)String accessToken,@Header(HEADER_GIFT_ID)Long idGift,
                          Callback<Gift> giftCallback);

    /**
     * Delete gift only if the user is the owner
     * @param accessToken
     * @param idGift
     * @param callbackSuccess
     */
    @GET(GIFT_DELETE)
    public void deleteGift(@Header(BEARER_TOKEN)String accessToken,
                           @Header(HEADER_GIFT_ID)Long idGift
                           ,Callback<Boolean> callbackSuccess);

    /**
     * Show last gifts
     * @param accessToken
     * @param startGift
     * @param giftsCallback
     */
    @GET(GIFTS_SHOW)
    public void showGifts(@Header(BEARER_TOKEN)String accessToken,
                            @Header(HEADER_START)int startGift,Callback<Gift[]> giftsCallback);

    /**
     * Show a gift chain of one gift
     * @param accessToken
     * @param idGift
     * @param giftCallback
     */
    @GET(GIFT_CHAIN)
    public void showGiftChain(@Header(BEARER_TOKEN)String accessToken,
                              @Header(HEADER_GIFT_ID)Long idGift,
                              Callback<Gift[]> giftCallback);

    /**
     * Show special info of the day
     * @param accessToken
     */
    @GET(SPECIAL_INFO)
    public void showSpecialInfo(@Header(BEARER_TOKEN)String accessToken,
                                Callback<SpecialInfo> specialInfo);


}
