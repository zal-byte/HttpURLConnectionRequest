/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author ruUreh
 */
public class HttpREQUEST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        
        
        String link = "http://site.com";
        
        
        //Create new Object of SendingRequest class
        SendingRequest send = new SendingRequest(link);
        
        //Creating new parameter for post method
        HashMap<String, String> param = new HashMap<>();
        param.put("key1","value1");
        param.put("key2","value2");
        param.put("key3","value3");
        
        
        //Send or Post ?
        //Post
        send.startRequestPost(param);
        System.out.println(send.startRequestPost(param));
        //Get
        send.startRequestGet(); // not printing result because startRequstGet() just returning string response, then. 
        System.out.println(send.startRequestGet());
        //Same with post method if the website have post response
    }
    
}

class SendingRequest{
    public HashMap<String, String> going =  new HashMap<>();
    public String uri;
    public SendingRequest( String url) throws MalformedURLException, IOException{
        this.uri = url;
    }
    
    
    public String startRequestGet() throws MalformedURLException, ProtocolException, IOException{
        URL site = new URL(uri);
        StringBuilder sb = new StringBuilder();
        
        HttpURLConnection connection = (HttpURLConnection) site.openConnection();
        connection.setReadTimeout(27000);
        connection.setConnectTimeout(27000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 CK={} (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        while((lines = br.readLine()) != null){
            sb.append(lines+"\n");
        }
        br.close();
        
        return sb.toString();
    }
    
   public String startRequestPost(HashMap<String, String> param) throws MalformedURLException, ProtocolException, IOException{
       this.going = param;
       
       String response = "";
       
       
       URL url = new URL(uri);
       
       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
       connection.setDoInput(true);
       connection.setDoOutput(true);
       connection.setRequestProperty("User-Agent","Mozilla/5.0 CK={} (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
       connection.setConnectTimeout(27000);
       connection.setReadTimeout(27000);
       connection.setRequestMethod("POST");
       connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
       
       
       OutputStream os = connection.getOutputStream();
       BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
       bw.write(getPostDataString(going));
       
       bw.flush();
       bw.close();
       
       
       int responseCode = connection.getResponseCode();
       if(responseCode == connection.HTTP_OK){
        String lines;
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((lines = br.readLine()) != null){
            response += lines;
        }
       }else{
           response = "";
       }
       
       return response;
       
   }
   
   private String getPostDataString(HashMap<String, String> hashMap) throws UnsupportedEncodingException{
       StringBuilder sb = new StringBuilder();
       boolean first = true;
       for(Map.Entry<String, String> map : hashMap.entrySet()){
           if(first)
               first = true;
           else
               sb.append("&");
           sb.append(URLEncoder.encode(map.getKey(),"UTF-8"));
           sb.append("=");
           sb.append(URLEncoder.encode(map.getValue(),"UTF-8"));
       }
       return sb.toString();
   }
    
}
