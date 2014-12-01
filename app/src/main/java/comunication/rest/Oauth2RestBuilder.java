package comunication.rest;



import java.util.HashMap;
import java.util.Map;

import comunication.AbstractHttpsClient;

/**
 * Created by unai on 09/10/2014.
 */
public class Oauth2RestBuilder {

    AbstractHttpsClient httpClient;

    Map<String,String> parameters;

    public Oauth2RestBuilder(AbstractHttpsClient client){
        httpClient = client;
        parameters = new HashMap<String, String>();
    }
    
    public int code;
	public String result;

    public void sendGetRequest(String url, final String bearerToken){
        parameters.clear();
        
        httpClient.getDataUrl(url,parameters);
        
        code = httpClient.code;
        result = httpClient.result;
    }

    public interface RequestReceivedHandler{
        void onRequestReceived(int code, String data);
    }
}
