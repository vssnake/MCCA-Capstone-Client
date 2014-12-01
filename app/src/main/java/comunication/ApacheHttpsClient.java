package comunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApacheHttpsClient extends AbstractHttpsClient{
	
	static ApacheHttpsClient singleton;
	

	HttpClient httpClient;

	@Override
	public void getDataUrl(String inputUrl, Map<String,String> requestProperties,
			final HttpClientReadHandler readHandler) {
		
		HttpGet request = new HttpGet(inputUrl);
		
		

		
		for (Iterator<String> property = requestProperties.keySet().iterator(); property.hasNext();){
            String key = property.next();
  
           request.addHeader(key,requestProperties.get(key));
        }
		try {
			HttpResponse response = httpClient.execute(request);
			
			// Get the response
			BufferedReader rd = new BufferedReader
				  (new InputStreamReader(response.getEntity().getContent()));
			String result = "";	    
			String line = "";
				
			while ((line = rd.readLine()) != null) {
				result = result + line + "\n";
			}
		} catch (IOException e) {
			readHandler.onHttpClientDataReceived(0, result);
		} 
		
	
			
		

	}
	
	
	public void getDataUrl(String inputUrl,Map<String,String> requestProperties) {
		
		HttpGet request = new HttpGet(inputUrl);

		for (Iterator<String> property = requestProperties.keySet().iterator(); property.hasNext();){
            String key = property.next();
  
           request.addHeader(key,requestProperties.get(key));
        }
		try {
			HttpResponse response = httpClient.execute(request);
			
			// Get the response
			BufferedReader rd = new BufferedReader
				  (new InputStreamReader(response.getEntity().getContent()));
			String result ="";	    
			String line = "";
				
			while ((line = rd.readLine()) != null) {
				result = result + line + "\n";
			}
			code = response.getStatusLine().getStatusCode();
			this.result = result;
		} catch (IOException e) {
			code = 0;
			result = e.getMessage();
		} 

	}
	
	public ApacheHttpsClient() {
		// Instantiate and configure the SslContextFactory
		httpClient = new DefaultHttpClient();
		
	}

	@Override
	public void sendData(String inputUrl, byte[] data,
			HttpClientReadHandler readHandler) {
		// TODO Auto-generated method stub
		
	}
	
	public static ApacheHttpsClient getHttpClient(){
    	if (singleton == null){
    		singleton = new ApacheHttpsClient();
    	}
    	return singleton;
    }

}
