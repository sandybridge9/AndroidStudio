package com.example.destroyer.lab2mobileapps;

import android.view.Display;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestOperator extends Thread {

    public interface RequestOperatorListener{
        //void success(ModelPost publication);
        void success(int count);
        void failed(int responseCode);
    }

    private RequestOperatorListener listener;
    private int responseCode;

    public void setListener(RequestOperatorListener listener){ this.listener = listener; }
/*
    @Override
    public void run(){
        super.run();
        try{
            ModelPost publication = request();

            if(publication != null){
                success(publication);
            }
            else
                failed(responseCode);
        }catch (IOException e){
            failed(-1);
        }catch (JSONException e){
            failed(-2);
        }
    }

    private ModelPost request() throws IOException, JSONException {

        //URL address
        URL obj = new URL("http://jsonplaceholder.typicode.com/posts");

        //Executor
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Determines what method will be used (GET, POST, PUT OR DELETE)
        con.setRequestMethod("GET");

        //Determine the content type
        con.setRequestProperty("Content-Type", "application/json");

        responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        InputStreamReader streamReader;

        if(responseCode == 200){
            streamReader = new InputStreamReader(con.getInputStream());
        }else{
            streamReader = new InputStreamReader(con.getErrorStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        if(responseCode == 200) {
            return parsingJsonObject(response.toString());
        }
        else {
            return null;
        }
    }
*/
    @Override
    public void run(){
        super.run();
        try{
            int count = request();

        if(count != -1){
            success(count);
        }
        else
            failed(responseCode);
        }catch (IOException e){
            failed(-1);
        }catch (JSONException e){
            failed(-2);
        }
    }
    private int request() throws IOException, JSONException {

        //URL address
        URL obj = new URL("http://jsonplaceholder.typicode.com/posts");

        //Executor
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Determines what method will be used (GET, POST, PUT OR DELETE)
        con.setRequestMethod("GET");

        //Determine the content type
        con.setRequestProperty("Content-Type", "application/json");

        responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        InputStreamReader streamReader;

        if(responseCode == 200){
            streamReader = new InputStreamReader(con.getInputStream());
        }else{
            streamReader = new InputStreamReader(con.getErrorStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        //JSONObject obje = new JSONObject(response.toString());
        JSONArray array = new JSONArray(response.toString());
        System.out.println(array.length());

        if(responseCode == 200) {
            return array.length();
        }
        else {
            return -1;
        }
    }

    public ModelPost parsingJsonObject(String response) throws JSONException{

        JSONObject object = new JSONObject(response);

        ModelPost post = new ModelPost();

        post.setId(object.optInt("id", 0));
        post.setUserId(object.optInt("userId", 0));

        post.setTitle(object.getString("title"));
        post.setBodyText(object.getString("body"));

        return post;
    }

    private void failed(int code){
        if(listener != null){
            listener.failed(code);
        }
    }

    private void success(int count){
        if(listener != null)
            listener.success(count);
    }
}
