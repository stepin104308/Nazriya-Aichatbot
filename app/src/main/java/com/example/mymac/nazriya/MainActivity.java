package com.example.mymac.nazriya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ConversationService myConversationService = null;
   // private TextView chatDisplayTV;
    private EditText userStatementET;
    private ListView List_chat;
    ArrayList chat;
    SimpleDateFormat time = new SimpleDateFormat("dd/MM/yyyy HH:mm;ss");
    String currentDateandTime = time.format(new Date());

    private final String IBM_USERNAME = "3ce32e83-b7d4-4872-864f-5568472f4e17";
    private final String IBM_PASSWORD = "Qhg084YCe1Gd";
    private final String IBM_WORKSPACE_ID = "c3a95310-285b-4886-b1f5-b2704d0923b9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //chatDisplayTV = findViewById(R.id.tv_chat_display);
        List_chat = findViewById(R.id.list_chat);
        chat = new ArrayList();
        final uiadapter adapter = new uiadapter(this,chat);
        List_chat.setAdapter(adapter);
        userStatementET = findViewById(R.id.et_user_statement);
        List_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //instantiating IBM Watson Conversation Service
        myConversationService =
                new ConversationService(
                        "2017-12-06",
                        IBM_USERNAME,
                        IBM_PASSWORD
                );

        userStatementET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    //show the user statement
                    final String userStatement = userStatementET.getText().toString();
//                    chatDisplayTV.append(
//                            Html.fromHtml("<p><b>YOU :<br/></b> " + userStatement + "</p>")
//                    );
                    Data d = new Data();
                    d.message = "You : " + userStatement;
                    d.time = ""+currentDateandTime;
                    d.who = 0;
                    chat.add(d);
                    adapter.notifyDataSetChanged();
                    userStatementET.setText("");

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(userStatement)
                            .build();
                    // initiate chat conversation
                    myConversationService
                            .message(IBM_WORKSPACE_ID, request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String botStatement = response.getText().get(0);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           /* chatDisplayTV.append(
                                                    Html.fromHtml("<p><b>Nazriya :<br/></b> " +
                                                            botStatement + "</p>")
                                            ); */
                                            Data d = new Data();
                                            d.message = "Nazriya : " + botStatement;
                                            d.time = ""+currentDateandTime;
                                            d.who =1;
                                           chat.add(d);
                                           adapter.notifyDataSetChanged();
                                        }
                                    });


                                    if (response.getIntents().get(0).getIntent().endsWith("Joke")) {
                                        final Map<String, String> params = new HashMap<String, String>() {{
                                            put("Accept", "text/plain");
                                        }};
                                        Fuel.get("https://icanhazdadjoke.com/").header(params)
                                                .responseString(new Handler<String>() {
                                                    @Override
                                                    public void success(Request request, Response response, String body) {
                                                        Log.d(TAG, "" + response + " ; " + body);
//                                                        chatDisplayTV.append(
//                                                                Html.fromHtml("<p><b>Nazriya:</b> " +
//                                                                        body + "</p>")
//                                                        );
                                                        Data d = new Data();
                                                        d.message = "Nazriya : " + body;
                                                        d.time = ""+currentDateandTime;
                                                        d.who =1;
                                                        chat.add(d);
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void failure(Request request, Response response, FuelError fuelError) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });
                }
                return false;
            }
        });
    }
}
