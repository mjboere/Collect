package nl.mjboere.collect.Connectors;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.mjboere.collect.Models.UserAccount;


public class Connector {

    private RequestQueue requestQueue;  // = Volley.newRequestQueue(getActivity());
    private static UserAccount ua = new UserAccount();  // Use this model to group data
    private boolean usernameAndPasswordFound = false;


    public Connector(Context ct ) {
        requestQueue = Volley.newRequestQueue(ct);
        Log.d("Constructor"," Finished ");
    }

    public UserAccount getLastAccount() {

        String getUrl = "http://192.168.1.10/showUsers.php";
        //String getUrl = "http://localhost/showUsers.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getUrl,(String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray users = response.getJSONArray("users");// tv.setText("");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                ua.setVoornaam(user.getString("voornaam"));
                                ua.setAchternaam(user.getString("achternaam"));
                                ua.setEmail(user.getString("email"));
                                ua.setCreateDate(user.getString("create_date")); // tekstView.append(create_date + "\n" + voornaam + "\n" + achternaam + "\n" + email + "\n" + "\n");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  tv.setText("Error: \n\n" + error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
        return ua; // Use this model to group data
    }


    public boolean checkLoginAndPassword(String username, String password) {
        Log.d("checkLoginAndPassword"," START ");
        String getUrl = "http://192.168.1.10/user.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl,(String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkLoginAndPassword"," GOT RESPONSE ");
                        try {
                            JSONArray found = response.getJSONArray("found");// tv.setText("");
                            for (int i = 0; i < found.length(); i++) {
                                JSONObject foundUIDPWD = found.getJSONObject(i);
                                String uid = foundUIDPWD.getString("username");
                                String pwd = foundUIDPWD.getString("password");
                                //usernameAndPasswordFound = foundUIDPWD.getBoolean("found");
                                Log.d("checkLoginAndPassword"," RESPONSE CONTAINS"+uid+pwd);
                                usernameAndPasswordFound = true;
                                return;
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkLoginAndPassword"," GOT ERROR RESPONSE ");
                usernameAndPasswordFound = false;
                //  tv.setText("Error: \n\n" + error.toString());
            }
        }
        );
        Log.d("checkLoginAndPassword"," SETUP REQUEST"+usernameAndPasswordFound+"");
        //requestQueue.add(jsonObjectRequest);
        try {Thread.sleep(8000);} catch (Exception e) {e.printStackTrace();}
        //Log.d("checkLoginAndPassword"," END"+usernameAndPasswordFound+"");
        return usernameAndPasswordFound;
    }



}