package allmaker_partners.adamstore.co.kr.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JinWoo on 2016-03-02.
 * HttpUrlConnection
 */
public class CHttpUrlConnection {

    private final String TAG = "ChttpUrlConnection";

    final String boundary = "boundary";

    public String postCall(String url){
        String response = null;
        try{
            URL mUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) mUrl.openConnection();

            //서버로부터 메세지를 받을수 있도록 설정
            con.setDoInput(true);

            //헤더값을 설정
            con.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");

            //전달방식을 설정한다(기본값은GET)
            con.setRequestMethod("GET");

            //서버로 데이터를 전송할 수 있도록 한다. GET방식이면 사용될 일이 없으나, true로 설정하면 자동으로 POST로 설정된다. 기본값은 false이다.
            con.setDoOutput(false);

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.d(HoUtils.TAG,"겟 방식 성공 : "+responseCode);
                InputStream is = con.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while ((nLength = is.read(byteBuffer,0, byteBuffer.length))!= -1){
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
                Log.d(HoUtils.TAG,"겟 방식 성공2 : "+response);
            }else{
                Log.d(HoUtils.TAG,"겟 방식 실패 오류 : "+responseCode);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(HoUtils.TAG,"겟 방식 성공리턴3 : "+response);
        return response;
    }

    public String sendPost(String url, HashMap<String, String> map)
    {
        String response = "";
        try{
            URL mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if(map != null)
            {
                OutputStream os = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(getPostString(map));
                bw.flush();
                bw.close();
                os.close();
            }

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.d(HoUtils.TAG,"연결성공");
                String line="";
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while((line = br.readLine()) != null){
                    response += line;
                }
            }else{
                Log.d(HoUtils.TAG,"연결실패 : "+conn.getResponseCode());
            }
            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(HoUtils.TAG,"HttpConnection HTTP URL ===> : "+url+"/?"+getPostString(map)+"\nHttpConnection HTTP Response ===> : "+response);
        return response;
    }



    private String getPostString(HashMap<String, String> map) {
        StringBuilder result = new StringBuilder();
        boolean first = true; // 첫 번째 매개변수 여부

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (first)
                first = false;
            else // 첫 번째 매개변수가 아닌 경우엔 앞에 &를 붙임
                result.append("&");
            try { // UTF-8로 주소에 키와 값을 붙임
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                if(entry.getValue().contains("%")){
                    result.append(entry.getValue());
                }else {
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(HoUtils.TAG,"리절트 : "+result.toString());
//        Log.i(TAG, "Result > "+result.toString());
        return result.toString();
    }

    /*public JSONArray getArrayKing(HashMap<Integer, FragUsePointData> map){
        JSONArray ja = new JSONArray();
        for(Map.Entry<Integer, FragUsePointData> entry : map.entrySet()){
            try{
                JSONObject jo = new JSONObject();
                jo.put("item_id", entry.getValue().getItemId());
                jo.put("item_count", entry.getValue().getCount());
                ja.put(jo);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return ja;
    }

    public JSONArray getArrayStore(HashMap<Integer, FragUsePointDataStore> map){
        JSONArray ja = new JSONArray();
        for(Map.Entry<Integer, FragUsePointDataStore> entry : map.entrySet()){
            try{
                JSONObject jo = new JSONObject();
                jo.put("item_id", entry.getValue().getItemId());
                jo.put("item_count", entry.getValue().getCount());
                ja.put(jo);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return ja;
    }*/

}
