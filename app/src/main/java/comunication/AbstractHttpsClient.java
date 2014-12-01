package comunication;

import java.util.Map;

/**
 * Created by unai on 09/10/2014.
 */
public abstract class AbstractHttpsClient {
	
	
	public AbstractHttpsClient(){}

	public int code;
	public String result;
	
	public abstract  void getDataUrl(final String inputUrl, final Map<String,String> requestProperties);
	
    public abstract  void getDataUrl(final String inputUrl, final Map<String,String> requestProperties,
                            final HttpClientReadHandler readHandler);

    public abstract void sendData(final String inputUrl,byte[] data,
    HttpClientReadHandler readHandler);


    public interface HttpClientReadHandler{
        void onHttpClientDataReceived(int bodyCode, String code);
    }
    
   
    	
}
