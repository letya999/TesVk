package com.example.tesvk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private ListView friendList;
    private ImageView img;
    private TextView userText, titleList, titleUser, titleFhoto;
    private final String TokenKey ="VK_ACCESS_TOKEN";
    private VKList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKAccessToken res = VKAccessToken.currentToken();
        setContentView(R.layout.activity_user);
        img = findViewById(R.id.imageView);
        titleFhoto = (TextView) findViewById(R.id.titleFhoto);
        Thread t = new Thread(new Runnable() {
            public void run() {
                initUserPhoto();
                initUserInfo();
                initFriendList();
            }
        });
        t.start();
        userText = (TextView) findViewById(R.id.textView);
        titleList = (TextView) findViewById(R.id.titleUser);
        friendList = (ListView) findViewById(R.id.listView);
        titleList = (TextView) findViewById(R.id.titleList);
    }

    public void initFriendList() {
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name", "last_name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                list = (VKList) response.parsedModel;
                for (int i = list.size() - 1; list.size()>5 && i >= 0; i--) list.remove(i);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_list_item_1, list);
                friendList.setAdapter(arrayAdapter);
                friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String select = getString(R.string.click)+" " + list.get(position);
                        Toast.makeText(getApplicationContext(),select ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"Ошибка получения списка друзей" ,Toast.LENGTH_LONG).show();
                super.onError(error);
            }
        });
    }

    public void initUserPhoto() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.FIELDS, "photo_200");
        VKRequest reqFhoto = new VKRequest("users.get",params);
        reqFhoto.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONArray resp = response.json.getJSONArray("response");
                    JSONObject user = resp.getJSONObject(0);
                    String photo_url = user.getString("photo_200");
                    Log.d("MyTag", photo_url);
                    Picasso.get().load(photo_url).into(img);
                    img.setVisibility(View.VISIBLE);
                } catch (org.json.JSONException ex) {
                    onError( new VKError(VKError.VK_JSON_FAILED));
                }
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"Ошибка получения фото" ,Toast.LENGTH_LONG).show();
                super.onError(error);
            }
        });
    }

    public void initUserInfo() {
        VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject user = response.json.getJSONArray("response").getJSONObject(0);
                    String first_name = user.getString("first_name");
                    String last_name = user.getString("last_name");
                    userText.setText(first_name +" " + last_name);
                } catch (JSONException e) {
                    Log.e(e.getMessage(), e.toString());
                }
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"Ошибка получения информации о пользователе" ,Toast.LENGTH_LONG).show();
                super.onError(error);
            }
        });
    }

}
