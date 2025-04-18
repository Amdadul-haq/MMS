package com.example.mosque_management_system.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.UserAPI;
import com.example.mosque_management_system.models.UserProfileRequest;
import com.example.mosque_management_system.models.UserProfileResponse;
import com.example.mosque_management_system.network.RetrofitClient;
import com.example.mosque_management_system.utils.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editFullName;
    private ImageView editProfileImage;
    private Button btnChooseImage, btnUpdateProfile;

    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    private String token;

    public UpdateProfileFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        editFullName = view.findViewById(R.id.edit_full_name);
        editProfileImage = view.findViewById(R.id.edit_profile_image);
        btnChooseImage = view.findViewById(R.id.btn_choose_image);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("jwt_token", null);

        btnChooseImage.setOnClickListener(v -> openFileChooser());

        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        return view;
    }

//    private void openFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
private void openFileChooser() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        // Android 13+
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 101);
        } else {
            launchImagePicker();
        }
    } else {
        // Android 12 and below
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            launchImagePicker();
        }
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchImagePicker();
        } else {
            Toast.makeText(getContext(), "Permission denied to access images", Toast.LENGTH_SHORT).show();
        }
    }
    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            editProfileImage.setImageURI(imageUri);
        }
    }

//    private void updateProfile() {
//        String fullName = editFullName.getText().toString().trim();
//
//        if (fullName.isEmpty()) {
//            Toast.makeText(getContext(), "Full name is required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        File imageFile = null;
//        MultipartBody.Part imagePart = null;
//
//        if (imageUri != null) {
//            String filePath = RealPathUtil.getRealPathFromURI(requireContext(), imageUri);
//            imageFile = new File(filePath);
//
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
//            imagePart = MultipartBody.Part.createFormData("profileImage", imageFile.getName(), requestFile);
//        }
//
//        // For image only upload
//        UserAPI userAPI = RetrofitClient.getRetrofitInstance(token).create(UserAPI.class);
//        Call<UserProfileResponse> call = userAPI.uploadProfileImage(imagePart);
//
//        call.enqueue(new Callback<UserProfileResponse>() {
//            @Override
//            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        // If you also want to update full name: call another API for fullName update if available
//    }
private void updateProfile() {
    String fullName = editFullName.getText().toString().trim();

    if (fullName.isEmpty()) {
        Toast.makeText(getContext(), "Full name is required", Toast.LENGTH_SHORT).show();
        return;
    }

    MultipartBody.Part imagePart = null;

    if (imageUri != null) {
        try {
            // Use ContentResolver to open InputStream
            Context context = requireContext();
            String mimeType = context.getContentResolver().getType(imageUri);

            // Use content resolver to open input stream
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            // Create RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), imageBytes);

            // Extract file name
            String fileName = getFileNameFromUri(context, imageUri);

            imagePart = MultipartBody.Part.createFormData("profileImage", fileName, requestFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to read image file", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    UserAPI userAPI = RetrofitClient.getRetrofitInstance(token).create(UserAPI.class);
    Call<UserProfileResponse> call = userAPI.uploadProfileImage(imagePart);

    call.enqueue(new Callback<UserProfileResponse>() {
        @Override
        public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<UserProfileResponse> call, Throwable t) {
            Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
}

    private String getFileNameFromUri(Context context, Uri uri) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                if (index >= 0) {
                    result = cursor.getString(index);
                }
            }
            cursor.close();
        }

        if (result == null) {
            result = "image_" + System.currentTimeMillis() + ".jpg";
        }

        return result;
    }

}
