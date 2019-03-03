package com.sauce.inunion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NoticeModify extends AppCompatActivity {
    SharedPreferences pref;
    Retrofit retrofit;
    NoticeInterface service;
    List<NoticeWriteImageItem> items = new ArrayList<>();
    NoticeWriteImageAdapter imageAdapter;
    private Uri imgUri, photoURI, albumURI;

    private String mCurrentPhotoPath;
    String Id;

    EditText editTitle, editContent;
    String department;
    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.notice_modify);

        ImageView imageBack = (ImageView) findViewById(R.id.toolbar_back);
        ImageView imageImage = (ImageView) findViewById(R.id.toolbar_image);
        TextView textSave = (TextView) findViewById(R.id.toolbar_save);

        editTitle = (EditText) findViewById(R.id.notice_write_title);
        editContent = (EditText) findViewById(R.id.notice_write_content);

        Intent intent = getIntent();
        Id=intent.getStringExtra("id");

        pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        department = pref.getString("App_department",null);

        //if ( editText.getText.toString().length() == 0 ) {
        //공백일 때 처리할 내용
        //} else {
        //공백이 아닐 때 처리할 내용
        //}

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notice_write_image);
        imageAdapter = new NoticeWriteImageAdapter(this,items);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));

// setting custom timeouts
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        client.connectTimeout(10, TimeUnit.SECONDS);
//        client.readTimeout(10, TimeUnit.SECONDS);
//        client.writeTimeout(10, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client.build())
                .build();
        service = retrofit.create(NoticeInterface.class);

        service.boardSelect(Id,department).enqueue(new Callback<RetrofitNotice>() {
            @Override
            public void onResponse(Call<RetrofitNotice> call, Response<RetrofitNotice> response) {
                Log.d("notice", "연결 성공"+response.code());

                RetrofitNotice res = response.body();
                editTitle.setText(res.title);
                editContent.setText(res.content);

            }

            @Override
            public void onFailure(Call<RetrofitNotice> call, Throwable t) {
                Log.d("contact", ""+t);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Notice notice = new Notice();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, notice)
//                        .commit();
//                toolbarActivity.setVisibility(View.VISIBLE);
//                navigation.setVisibility(View.VISIBLE);
//                shadow.setVisibility(View.VISIBLE);
                onBackPressed();
            }
        });


        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.d("notice", "Permission Granted");
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Log.d("notice", "Permission Denied\n" + deniedPermissions.toString());
            }
        };

        TedPermission.with(NoticeModify.this)
                .setPermissionListener(permissionlistener).setRationaleMessage("사진 첨부를 하기 위해서는 권한이 필요합니다")
                .setDeniedMessage("거부하시면 정상적으로 사용이 불가합니다\n\n [설정] > [권한] 에서 권한을 허용해 주세요")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        imageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        RelativeLayout relativeLayout = findViewById(R.id.rl_picture_icon);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUpload();
            }
        });
    }

    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
    }

    public File createImageFile() throws IOException {

        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile= null;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");

        if(!storageDir.exists()){
            Log.v("알림","storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림","storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case 0 : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        albumURI = Uri.fromFile(albumFile);
                        galleryAddPic();
                        //이미지뷰에 이미지 셋팅
                        items.add(new NoticeWriteImageItem(photoURI));
                        imageAdapter.notifyDataSetChanged();
//                        imageUpload();
                        Log.v("알림","이미지뷰에 이미지 저장");
                        //cropImage();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }
        }
    }
    public String getImageNameToUri(Uri data) {

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst(); String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        return imgName;

    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = new File(getPath(fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void imageUpload() {

//        File file = new File (getPath(photoURI));
        final RequestBody titleRequest = RequestBody.create(MediaType.parse("text/plain"), editTitle.getText().toString());
        final RequestBody contentRequest = RequestBody.create(MediaType.parse("text/plain"), editContent.getText().toString());
        final RequestBody contentidRequest = RequestBody.create(MediaType.parse("text/plain"), Id);
        final RequestBody departmentRequest = RequestBody.create(MediaType.parse("text/plain"), department);

        // MultipartBody.Part is used to send also the actual file name
        final List<MultipartBody.Part> body = new ArrayList<>();

        // add dynamic amount
        for(int j=0; j<imageAdapter.items.size();j++){
            body.add(prepareFilePart("userfile", imageAdapter.items.get(j).getImage()));
        }

        pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        department = pref.getString("App_department",null);

        service.boardModify(titleRequest,contentRequest,contentidRequest,departmentRequest,body).enqueue(new Callback<RetrofitNotice>() {
            @Override
            public void onResponse(Call<RetrofitNotice> call, Response<RetrofitNotice> response) {
                Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
                Intent saveIntent =  new Intent();
//                saveIntent.putExtra("title", editTitle.getText().toString());
//                saveIntent.putExtra("content", editContent.getText().toString());
//                saveIntent.putExtra("id", Id);
//                saveIntent.putExtra("file", Id);
                setResult(700, saveIntent);
                finish();
            }

            @Override
            public void onFailure(Call<RetrofitNotice> call, Throwable t) {
                Toast.makeText(getApplicationContext(), String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
