package com.wein_business.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.wein_business.BaseApp
import java.io.*
import java.util.*

object FileUtils {

    private fun getTempDir(): File {
//        val tempDir = File(BaseApp.appContext.externalCacheDir, "temp")
//        if (!tempDir.exists()) tempDir.mkdir()
//        return tempDir
        return BaseApp.appContext.cacheDir
    }

    fun getNewPictureFile(): File {
        return File(getTempDir(), Date().time.toString() + ".jpg")
    }

    fun clearTempDir() {
        try {
            for (file in getTempDir().listFiles()!!) file.delete()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getFileUri(file: File?): Uri {
        return if (Build.VERSION.SDK_INT >= 24) FileProvider.getUriForFile(
            BaseApp.appContext,
            BaseApp.appContext.packageName + ".provider",
            file!!
        ) else Uri.fromFile(file)
    }

    //Was created to make a copy of the file in temp directory with TYPE name or Random Name
    //TODO STOP CREATING TEMP AND PASS THE ORIGINAL FILE DIRECT TO API
    fun fileFromContentUri(context: Context, contentUri: Uri, name: String): File {
        // Preparing Temp file name
        val fileExtension = getFileExtensionFromMimeType(context.contentResolver.getType(contentUri))
        val fileName = name + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(getTempDir(), fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    fun getFileExtensionFromMimeType(mimeType: String?): String {
        var extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        return extension ?: ""
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }


    //**********************************************************************************
    //**********************************************************************************
    //    public boolean writeFileToStorage(ResponseBody body , String fileName) {
    //        try {
    //            File file = new File(getTempDir() + File.separator + fileName);
    //            file.deleteOnExit();
    //
    //            InputStream inputStream = null;
    //            OutputStream outputStream = null;
    //            try {
    //                byte[] fileReader = new byte[4096];
    //                long fileSize = body.contentLength();
    //                long fileSizeDownloaded = 0;
    //                inputStream = body.byteStream();
    //                outputStream = new FileOutputStream(file);
    //                while (true) {
    //                    int read = inputStream.read(fileReader);
    //                    if (read == -1) {
    //                        break;
    //                    }
    //                    outputStream.write(fileReader, 0, read);
    //                    fileSizeDownloaded += read;
    //                }
    //                outputStream.flush();
    //                previewFile(file);
    //                return true;
    //            } catch (IOException e) {
    //                return false;
    //            } finally {
    //                if (inputStream != null) {
    //                    inputStream.close();
    //                }
    //                if (outputStream != null) {
    //                    outputStream.close();
    //                }
    //            }
    //        } catch (IOException e) {
    //            return false;
    //        }
    //    }
    //    public void previewFile(File file) {
    //        try {
    //            Uri fileUri = getFileUri(file);
    //            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
    //            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    //
    //            Intent intent = new Intent(Intent.ACTION_VIEW);
    //            intent.setDataAndType(fileUri, mimeType);
    //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //            genericActivity.startActivity(intent);
    //        } catch (Exception ex) {
    //            ex.printStackTrace();
    //        }
    //    }
    //    public String[] getAcceptedFilesTypes() {
    //        String[] mimeTypes = {
    //                "image/jpeg",
    //                "image/jpg",
    //                "image/png",
    //                "application/pdf",
    //                "application/msword",
    //                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    //        };
    //
    //        return mimeTypes;
    //    }
    //
    //    public String getMimeType(File file) {
    //        String type = null;
    //        Uri fileUri = getFileUri(file);
    //        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
    //        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    //    }
    //    public String getFileExtension(File file) {
    //        Uri fileUri = getFileUri(file);
    //        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
    //        return fileExtension;
    //    }
    //    public Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
    //
    //        InputStream input = context.getContentResolver().openInputStream(selectedImage);
    //        ExifInterface ei;
    //        if (Build.VERSION.SDK_INT > 23)
    //            ei = new ExifInterface(input);
    //        else
    //            ei = new ExifInterface(selectedImage.getPath());
    //
    //        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    //
    //        switch (orientation) {
    //            case ExifInterface.ORIENTATION_ROTATE_90:
    //                return rotateImage(img, 90);
    //            case ExifInterface.ORIENTATION_ROTATE_180:
    //                return rotateImage(img, 180);
    //            case ExifInterface.ORIENTATION_ROTATE_270:
    //                return rotateImage(img, 270);
    //            default:
    //                return img;
    //        }
    //    }
    //    public String getPathURI(final Uri uri) {
    //        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    //        // DocumentProvider
    //        if (isKitKat && DocumentsContract.isDocumentUri(genericActivity, uri)) {
    //            // ExternalStorageProvider
    //            if (isExternalStorageDocument(uri)) {
    //                final String docId = DocumentsContract.getDocumentId(uri);
    //                final String[] split = docId.split(":");
    //                final String type = split[0];
    //
    //                if ("primary".equalsIgnoreCase(type)) {
    //                    return Environment.getExternalStorageDirectory() + "/" + split[1];
    //                }
    //
    //            }
    //            // DownloadsProvider
    //            else if (isDownloadsDocument(uri)) {
    //
    //                final String id = DocumentsContract.getDocumentId(uri);
    //                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
    //                        Long.valueOf(id));
    //
    //                return getDataColumn(genericActivity, contentUri, null, null);
    //            }
    //            // MediaProvider
    //            else if (isMediaDocument(uri)) {
    //                final String docId = DocumentsContract.getDocumentId(uri);
    //                final String[] split = docId.split(":");
    //                final String type = split[0];
    //
    //                Uri contentUri = null;
    //                if ("image".equals(type)) {
    //                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    //                } else if ("video".equals(type)) {
    //                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //                } else if ("audio".equals(type)) {
    //                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //                }
    //
    //                final String selection = "_id=?";
    //                final String[] selectionArgs = new String[]{split[1]};
    //
    //                return getDataColumn(genericActivity, contentUri, selection, selectionArgs);
    //            }
    //        }
    //        // MediaStore (and general)
    //        else if ("content".equalsIgnoreCase(uri.getScheme())) {
    //
    //            // Return the remote address
    //            if (isGooglePhotosUri(uri))
    //                return uri.getLastPathSegment();
    //
    //            return getDataColumn(genericActivity, uri, null, null);
    //        }
    //        // File
    //        else if ("file".equalsIgnoreCase(uri.getScheme())) {
    //            return uri.getPath();
    //        }
    //
    //        return null;
    //    }
    //
    //    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
    //
    //        Cursor cursor = null;
    //        final String column = "_data";
    //        final String[] projection = {column};
    //
    //        try {
    //            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
    //            if (cursor != null && cursor.moveToFirst()) {
    //                final int index = cursor.getColumnIndexOrThrow(column);
    //                return cursor.getString(index);
    //            }
    //        } finally {
    //            if (cursor != null)
    //                cursor.close();
    //        }
    //        return null;
    //    }
    //    
    //    public static boolean isExternalStorageDocument(Uri uri) {
    //        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    //    }
    //
    //    public static boolean isDownloadsDocument(Uri uri) {
    //        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    //    }
    //    
    //    public static boolean isMediaDocument(Uri uri) {
    //        return "com.android.providers.media.documents".equals(uri.getAuthority());
    //    }
    //    
    //    public static boolean isGooglePhotosUri(Uri uri) {
    //        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    //    }
}