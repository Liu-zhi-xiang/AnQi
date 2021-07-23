package com.motorbike.anqi.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 应用的基本初始化，包括缓存目录等
 * 
 * @author LiuSheng
 * 
 */
public class FileUtil {

	public final static String appName = "Anqi";

	public final static String imgcache = "imgcache";
	public final static String data = "data";
	public final static String download = "download";
	public final static String logFile = "Logtest.txt";
	public final static String log = "exceptionLog";

	/**
	 * 正则：手机号
	 */
	public static final String REGEX_MOBILE_EXACT = "^(1[2-9])\\d{9}$";
	/**
	 * 正则：汉字
	 */
	public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";

	/**
	 * 验证手机号
	 *
	 * @param string 待验证文本
	 * @return {@code true}: 匹配<br>{@code false}: 不匹配
	 */
	public static boolean isMobileExact(String string) {
		return !TextUtils.isEmpty(string) && Pattern.matches(REGEX_MOBILE_EXACT, string);
	}
	public static boolean EmailFormat(String eMAIL1) {//邮箱判断正则表达式
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern
				.compile(check);
		Matcher mc = pattern.matcher(eMAIL1);
		return mc.matches();
	}

	/**
	 * 判断文件夹是否存在
	 * ********************/
	public static String checkAndMkdirs(String dir) {
	    File file = new File(dir);
	    if (!file.exists()) {
	      file.mkdirs();
	    }
	    return dir;
	}
	/**
	 * 图片的缓存目录
	 * ********************/
	public static String DEFAULT_CACHE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()+ getImageCachePath();

	public static String LOg_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()+ getLogPath();

	/**
	 * 数据的缓存目录
	 **********************/
	public static String DEFAULT_CACHE_PRIVATE = Environment.getExternalStorageDirectory().getAbsolutePath() + getDataPath();

	/**
	 * APK下载目录
	 **********************/
	public static String APK_DOWNLOAD = Environment.getExternalStorageDirectory().getAbsolutePath()+ getDownloadPath();

	/**
	 * 判断Sdcard是否存在
	 **********************/
	public static boolean isSdCardExists() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回apk下载路径
	 * 
	 * @return
	 **********************/
	public static String getDownloadPath(){
		return File.separator + appName + File.separator + download+ File.separator;
	}
	public static String getLogPath() {
		return File.separator + appName+ File.separator+log+ File.separator;
	}
	/**
	 * 返回图片缓存路径
	 * 
	 * @return
	 **********************/
	public static String getImageCachePath() {
		return File.separator + appName + File.separator + imgcache+ File.separator;
	}

	/**
	 * 返回数据缓存路径
	 * 
	 * @return
	 **********************/
	public static String getDataPath() {
		return File.separator + appName + File.separator + data;
	}

	/**
	 * 从手机或sd卡获取bitmap base64
	 * 
	 * @param filePath
	 *            完整路径
	 * @return
	 */
	public String getBitmapBase64(String filePath) {

		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();

			byte[] base64 = Base64.encode(buffer, Base64.DEFAULT);

			return new String(base64, "utf-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存图片
	 */

	public static String savaBitmap(String fileName, Bitmap bitmap, int quality)
			throws IOException {
		if (bitmap == null) {
			return "";
		}

		if (quality < 0 || quality > 100) {
			quality = 100;
		}

		String path = DEFAULT_CACHE_FOLDER;
		File folderFile = new File(path);
		if (!folderFile.exists()) {
			folderFile.mkdirs();
		}
		String filePath = path + File.separator + fileName;
		File file = new File(filePath);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.PNG, quality, fos);
		fos.flush();
		fos.close();
		return filePath;
	}

	/**
	 * 截断输出日志
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (tag == null || tag.length() == 0
				|| msg == null || msg.length() == 0)
			return;

		int segmentSize = 3 * 1024;
		long length = msg.length();
		if (length <= segmentSize ) {// 长度小于等于限制直接打印
			Log.e(tag, msg);
		}else {
			while (msg.length() > segmentSize ) {// 循环分段打印日志
				String logContent = msg.substring(0, segmentSize );
				msg = msg.replace(logContent, "");
				Log.e(tag, logContent);
			}
			Log.e(tag, msg);// 打印剩余日志
		}
	}

	/**
	 * Compress by quality,  and generate image to the path specified
	 *
	 * @param image
	 * @param outPath
	 * @param maxSize target will be compressed to be smaller than this size.(kb)
	 * @throws IOException
	 */
	public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// scale
		int options = 100;
		// Store the bitmap into output stream(no compress)
		image.compress(CompressFormat.JPEG, options, os);
		// Compress by loop
		while ( os.toByteArray().length / 1024 > maxSize) {
			// Clean up os
			os.reset();
			// interval 10
			image.compress(CompressFormat.JPEG, options, os);
			options -= 10;
		}
		// Generate compressed image file
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(os.toByteArray());
		fos.flush();
		fos.close();
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 60, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;//压缩好比例大小后再进行质量压缩
	}
	public static void deleteFile(String filepath) {
		try {
			File file=new File(filepath);
			if (file.exists()) { // 判断文件是否存在
                if (file.isFile()) { // 判断是否是文件
                    file.delete(); // delete()方法 你应该知道 是删除的意思;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.<br>
	 * <br>
	 * Callers should check whether the path is local before assuming it
	 * represents a local file.
	 *
	 * @param context The context.
	 * @param uri     The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri)) {
				return uri.getLastPathSegment();
			}

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 * @author paulburke
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} catch (IllegalArgumentException ex) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 * @author paulburke
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 * @author paulburke
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 * @author paulburke
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}


	private static final String TAG = "FileUtils";

	public static File getFile(final String path) {
		try {
			File file = new File(path);
			File dir = file.getParentFile();
			if (dir == null) {
				Log.e(TAG, "file's parent dir is null, path=" + file.getCanonicalPath());
				return null;
			}

			if (!dir.exists()) {
				if (dir.getParentFile().exists()) {
					dir.mkdir(); // dir父目录存在用mkDir
				} else {
					dir.mkdirs(); // dir父目录不存在用mkDirs
				}
			}

			if (!file.exists() && !file.createNewFile()) {
				Log.e(TAG, "can not create dest file, path=" + path);
				return null;
			}
			return file;
		} catch (Throwable e) {
			Log.e(TAG, "create dest file error, path=" + path, e);
		}

		return null;
	}

	public static boolean appendFile(final String message, final String path) {
		if (TextUtils.isEmpty(message)) {
			return false;
		}

		if (TextUtils.isEmpty(path)) {
			return false;
		}

		boolean written = false;
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(path, true));
			fw.write(message);
			fw.flush();
			fw.close();

			written = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return written;
	}

	public static boolean appendFile(final byte[] message, final String path) {
		if (message == null || message.length <= 0) {
			return false;
		}

		if (TextUtils.isEmpty(path)) {
			return false;
		}

		boolean written = false;
		try {
			FileOutputStream fw = new FileOutputStream(path, true);
			fw.write(message);
			fw.close();

			written = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return written;
	}

	public static synchronized void shrink(final String logPath, final int maxLength, final int baseLength) {
		File file = new File(logPath);
		if (file.length() < maxLength) {
			return;
		} else if (file.length() > Integer.MAX_VALUE) {
			file.delete();
			return;
		}

		File out = new File(logPath + "_tmp");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(out);
			FileChannel input = fis.getChannel();

			input.position(file.length() - baseLength);
			FileChannel output = fos.getChannel();
			output.transferFrom(fis.getChannel(), 0, baseLength);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(fis);
			close(fos);
		}

		if (out.exists()) {
			if (file.delete()) {
				out.renameTo(file);
			}
		}
	}

	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFilePath(final String dirPath, final String fileName) {
		File dir = new File(dirPath);

		if (!dir.exists()) {
			if (dir.getParentFile().exists()) {
				dir.mkdir(); // dir父目录存在用mkDir
			} else {
				dir.mkdirs(); // dir父目录不存在用mkDirs
			}
		}

		return dirPath + File.separator + fileName;
	}

	// 是否包含扩展名
	public static boolean hasExtension(String filename) {
		int dot = filename.lastIndexOf('.');
		return ((dot > -1) && (dot < (filename.length() - 1)));
	}

	// 获取文件扩展名
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return "";
	}

	// 获取文件名
	public static String getFileNameFromPath(String filepath) {
		if ((filepath != null) && (filepath.length() > 0)) {
			int sep = filepath.lastIndexOf('/');
			if ((sep > -1) && (sep < filepath.length() - 1)) {
				return filepath.substring(sep + 1);
			}
		}
		return filepath;
	}

	// 获取不带扩展名的文件名
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static String getExternalPackageDirectory(Context context) {
		String externalPath = Environment.getExternalStorageDirectory().getPath();
		return externalPath + File.separator + context.getPackageName();
	}
}
