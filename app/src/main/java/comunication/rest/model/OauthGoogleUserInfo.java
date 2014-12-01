package comunication.rest.model;

/**
 * Created by unai on 09/10/2014.
 */
public class OauthGoogleUserInfo {

    private String family_name;
    private String name;
    private String picture;
    private String locale;
    private String gender;
    private String email;
    private String link;
    private String given_name;
    private String id;
    private String verified_email;

    public OauthGoogleUserInfo(){}

    public String getFamily_name() {
        return family_name;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getLocale() {
        return locale;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    public String getGiven_name() {
        return given_name;
    }

    public String getId() {
        return id;
    }

    public String getVerified_email() {
        return verified_email;
    }
}
