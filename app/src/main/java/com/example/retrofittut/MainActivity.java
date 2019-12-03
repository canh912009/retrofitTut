package com.example.retrofittut;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.android.debug.hv.ViewServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewServer.get(this).addWindow(this);

        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getComments();
//        getPosts();
//        getOnePost();
//        createPost();
//        updatePost();
//        deletePost();
    }

    private void deletePost() {
        Call<Void> callDelete = jsonPlaceHolderApi.deletePost(13);

        callDelete.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code" + response.code());
                }

                textViewResult.setText("Code" + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });
    }

    private void updatePost() {
        Post post = new Post(6, "null", "New text");

//        Call<Post> call = jsonPlaceHolderApi.putPost("abc", 1, post);
//        Call<Post> call = jsonPlaceHolderApi.putPost("abc", 1, post);

        Map<String, String> headers = new HashMap<>();
        headers.put("MapHeader1", "valueMapHeader1");
        headers.put("MapHeader2", "valueMapHeader2");
        Call<Post> call = jsonPlaceHolderApi.patchPost(headers, 1, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code" + response.code());
                }

                Post putPost = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Id: " + putPost.getId() + "\n";
                content += "User Id: " + putPost.getUserId() + "\n";
                content += "Email: " + putPost.getTitle() + "\n";
                content += "Comment: " + putPost.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }


    private void createPost() {
//        Post post = new Post(22, "New Title", "this is test text");
//        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        Call<Post> call = jsonPlaceHolderApi.createPost(22, "New Title", "this is test text");

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code" + response.code());
                    return;
                }

                Post postRespond = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Id: " + postRespond.getId() + "\n";
                content += "User Id: " + postRespond.getUserId() + "\n";
                content += "Email: " + postRespond.getTitle() + "\n";
                content += "Comment: " + postRespond.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments() {
//        Call<List<Comment>> callComments = jsonPlaceHolderApi.getComments(1);
        Call<List<Comment>> callComments = jsonPlaceHolderApi.getComments("comments?postId=1");


        callComments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code" + response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    String content = "";
                    content += "PostId: " + comment.getPostId() + "\n";
                    content += "Id: " + comment.getId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Comment: " + comment.getText() + "\n\n";

                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getPosts() {
        // https://jsonplaceholder.typicode.com/posts?userId=3&userId=4&_sort=id&_order=desc
        //Call<List<Post>> callPosts = jsonPlaceHolderApi.getPosts(3, 4, "id", null);
        //Call<List<Post>> callPosts = jsonPlaceHolderApi.getPosts(new Integer[]{1,2}, "id", null);

        //this use for QueryMap
        Map<String, String> params = new HashMap<>();
        params.put("userId", "3");
        params.put("_sort", "id");
        params.put("_order", "desc");
        Call<List<Post>> callPosts = jsonPlaceHolderApi.getPosts(params);

        callPosts.enqueue(new Callback<List<Post>>() {
            // được gọi khi nhận được một phản hồi HTTP
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            // gọi khi một ngoại lệ kết nối mạng xảy ra trong quá trình giao tiếp đến máy chủ
            // hoặc khi một ngoại lệ bất ngờ xảy ra trong quá trình xử lý yêu cầu hoặc xử lý phản hồi.
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getOnePost() {
        Call<Post> callOnePost = jsonPlaceHolderApi.getOnePost(2);
        callOnePost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                String content = "";
                content += "ID: " + post.getId() + "\n";
                content += "User ID: " + post.getUserId() + "\n";
                content += "Title: " + post.getTitle() + "\n";
                content += "Text: " + post.getText() + "\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }
}
