package com.wein_business.ui.fragment.picker;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wein_business.R;
import com.wein_business.ui.fragment.interfaces.AttachmentPickerListener;
import com.wein_business.ui.activity.generic.GenericActivity;

import java.io.File;

public class AttachmentPickerSheetFragment222222 extends BottomSheetDialogFragment  {

    private GenericActivity genericActivity;
    private AttachmentPickerListener imagePickerChooseListener;
    private FileUtils fileUtils;

    private File cameraPictureFile;
    private Uri currentCropUri;
    private boolean isOnlyImages;

    private final int CAMERA_REQUEST = 0;
    private final int GALLERY_REQUEST = 1;
    private final int FILE_REQUEST = 2;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = View.inflate(getContext(), R.layout.fragment_sheet_attachment_picker, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog); // Style here

        return dialog;
    }

//    private void openCamera() {
//        TedPermission.with(genericActivity)
//                .setPermissionListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        cameraPictureFile = fileUtils.getNewPictureFile();
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUtils.getFileUri(cameraPictureFile));
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//
//                    }
//                })
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setDeniedMessage(genericActivity.getResources().getString(R.string.storage_deny_permisssion))
//                .check();
//    }



//    private void openFiles() {
//        TedPermission.with(genericActivity)
//                .setPermissionListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//
//                        Intent intent;
//                        if (isOnlyImages) {
//                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            galleryIntent.setType("image/*");
//                            intent = Intent.createChooser(galleryIntent, "Images");
//                        } else {
//                            Intent documntsIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                            documntsIntent.putExtra(Intent.EXTRA_MIME_TYPES, FileUtils.getSharedInstance().getAcceptedFilesTypes());
//                            documntsIntent.putExtra("android.content.extra.SHOW_ADVANCED", true);
//                            documntsIntent.setType("*/*");
//                            intent = Intent.createChooser(documntsIntent, "Files");
////                            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{galleryIntent});
//                        }
//
//                        startActivityForResult(intent, FILE_REQUEST);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//
//                    }
//                })
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setDeniedMessage(genericActivity.getResources().getString(R.string.storage_deny_permisssion))
//                .check();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            if (resultCode == RESULT_OK) {
//                if (requestCode == CAMERA_REQUEST) {
//
//                    long fileLength = cameraPictureFile.length();
//                    long fileSize = fileLength / 1048576;
//                    if (fileSize > 20) {
//                        Dialogs.showToastMessage(getResources().getString(R.string.fileSizeError));
//                        dismiss();
//                        return;
//                    }
//                    imagePickerChooseListener.cameraResult(cameraPictureFile);
//                } else if (requestCode == FILE_REQUEST && data != null) {
//
//                    Uri uri = data.getData();
//                    String path = fileUtils.getPathURI(uri);
//
//                    File selectedFile = new File(path);
//                    if (path == null || !selectedFile.exists()) {
//                        Dialogs.showToastMessage(genericActivity.getResources().getString(R.string.error_attachment));
//                        dismiss();
//                        return;
//                    }
//
//                    if (!isOnlyImages && !Arrays.asList(FileUtils.getSharedInstance().getAcceptedFilesTypes())
//                            .contains(FileUtils.getSharedInstance().getMimeType(selectedFile))) {
//                        String errorMessage = Utility.getString(R.string.error_attachment) +
//                                "\n" +
//                                Utility.getString(R.string.attachments_accepted);
//                        Dialogs.showErrorMessage(errorMessage);
//                        dismiss();
//                        return;
//                    }
//
//                    long fileLength = selectedFile.length();
//                    long fileSize = fileLength / 1048576;
//                    if (fileSize > 20) {
//                        Dialogs.showToastMessage(getResources().getString(R.string.fileSizeError));
//                        dismiss();
//                        return;
//                    }
//                    imagePickerChooseListener.fileResult(selectedFile);
//                }
//            }
//            dismiss();
//        } catch (Exception e) {
//            dismiss();
//        }
//    }


}