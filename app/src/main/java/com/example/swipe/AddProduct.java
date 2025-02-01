package com.example.swipe;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.swipe.ViewModel.ProductViewModel;
import com.example.swipe.api.ProductAPIService;
import com.example.swipe.api.RetrofitInstance;
import com.example.swipe.databinding.FragmentAddProductListDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends BottomSheetDialogFragment {

    private FragmentAddProductListDialogBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri imageUri = null;
    private ProductViewModel viewModel;
    ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddProductListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.spinnerProductType.setAdapter(ArrayAdapter.createFromResource(
                getContext(),
                R.array.product_types,
                android.R.layout.simple_spinner_item
        ));
        viewModel=new ViewModelProvider(this).get(ProductViewModel.class);
        // Observe the upload result
        // Observe the progress dialog state
        viewModel.getShowProgressDialog().observe(getViewLifecycleOwner(), show -> {
            if (show) {
                // Show progress dialog

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Uploading product...");
                    progressDialog.setCancelable(false);
                }
                progressDialog.show();
            } else {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        viewModel.getUploadResult().observe(getViewLifecycleOwner(), result -> {
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show();
            if (result.toLowerCase().contains("Product added Successfully!")) {
                // Only dismiss if upload was successful
                progressDialog.dismiss();
                dismiss();
            }
            else{
               // progressDialog.dismiss();
            }
        });




        binding.submitButton.setOnClickListener(v -> {

            if (isValidForm()) {
                if(imageUri!=null) {
                    try {
                        uploadProduct(imageUri);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{Toast.makeText(getContext(),"add image",Toast.LENGTH_SHORT).show();}
                // You can now send this data to your database or use it further as needed
                dismiss();



            }
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();

                        if (imageUri != null) {
                            // Load the image into an ImageView


                            //uploadProduct(selectedImageUri);

                            // Show a Toast message
                            Toast.makeText(requireContext(), "Image selected!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        // Set click listener for the button
        binding.btnSelectImage.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }

//    private void cropImage(Uri sourceUri) {
//        Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped_image.jpg"));
//
//        UCrop.of(sourceUri, destinationUri)
//                .withAspectRatio(1, 1) // 1:1 aspect ratio
//                .start(requireContext(), this);
//    }

    private void uploadProduct(Uri imageUri) throws InterruptedException {
        if (!isValidForm()) {
            return;
        }

        String productName = binding.editProductName.getText().toString().trim();
        String productType = binding.spinnerProductType.getSelectedItem().toString();
        String price = binding.editSellingPrice.getText().toString().trim();
        String tax = binding.editTaxRate.getText().toString().trim();

        // Create RequestBody for text fields


        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), productName);
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), productType);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody taxBody = RequestBody.create(MediaType.parse("text/plain"), tax);

        // Create MultipartBody.Part for the image
        File file = getFileFromUri(imageUri);
        if (file == null || !file.exists()) {
            Toast.makeText(requireContext(), "Failed to process image file", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log file details for debugging
        Log.d("AddProduct", "File size: " + file.length() + " bytes");
        Log.d("AddProduct", "File path: " + file.getAbsolutePath());


        String mimeType = requireContext().getContentResolver().getType(imageUri);
        if (mimeType == null) {
            mimeType = "image/jpeg"; // fallback
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);



        viewModel.uploadProduct(nameBody, typeBody, priceBody, taxBody, imagePart);

//        ProductAPIService apiService = RetrofitInstance.getService();
//        // Make the API call
//        Call<ResponseBody> call = apiService.uploadProduct(nameBody, typeBody, priceBody, taxBody, imagePart);
//        Log.d("progreesss", "uploadProduct: "+call.request().url());
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (!isAdded() || isDetached()) {
//                    progressDialog.dismiss();
//                    return; // Fragment is no longer attached, do nothing
//                }
//
//                if (response.isSuccessful()) {
//
//                    Log.d("progreesss", "onResponse: "+response.body());
//                    Toast.makeText(requireContext(), "Product uploaded successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    progressDialog.dismiss();
//                    Log.d("progreesss", "onResponse: "+response.body());
//                    Toast.makeText(requireContext(), "Failed to upload product", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                if (!isAdded() || isDetached()) {
//                    progressDialog.dismiss();
//                    return; // Fragment is no longer attached, do nothing
//                }
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    try {
//
//                    } catch (IllegalStateException e) {
//                        e.printStackTrace(); // Handle the exception if the fragment is detached
//                    }
//                }
//                Toast.makeText(requireContext(), "kuch nhi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("ERROR_SUB", "onFailure: "+t.getMessage());
//            }
//        });
////        if (progressDialog != null && progressDialog.isShowing()) {
////            progressDialog.dismiss();
////        }

    }
    private File getFileFromUri(Uri uri) {
        try {
            Log.d("AddProduct", "Image URI: " + uri.toString());

            // Create a temporary file in the cache directory
            File file = File.createTempFile("temp_image", ".jpg", requireContext().getCacheDir());
            Log.d("AddProduct", "Temporary file path: " + file.getAbsolutePath());

            // Open an InputStream from the Uri
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

            // Copy the content of the InputStream to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[5120];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams
            outputStream.close();
            inputStream.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidForm() {
        boolean valid = true;

        // Product Name Validation
        if (binding.editProductName.getText().toString().trim().isEmpty()) {
            binding.editProductName.setError("Product Name is required!");
            valid = false;
        }

        // Selling Price Validation (Decimal value)
        String sellingPrice = binding.editSellingPrice.getText().toString().trim();
        if (sellingPrice.isEmpty() || !isValidDecimal(sellingPrice)) {
            binding.editSellingPrice.setError("Valid Selling Price is required!");
            valid = false;
        }

        // Tax Rate Validation (Decimal value)
        String taxRate = binding.editTaxRate.getText().toString().trim();
        if (taxRate.isEmpty() || !isValidDecimal(taxRate)) {
            binding.editTaxRate.setError("Valid Tax Rate is required!");
            valid = false;
        }

        return valid;
    }

    private boolean isValidDecimal(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
