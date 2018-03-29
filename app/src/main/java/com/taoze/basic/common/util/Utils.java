package com.taoze.basic.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String CONFIG_PATH = "/config";//配置文件根路径
	
	// 获取SD卡根路径
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return "";
		}
		return sdDir.getPath()+"/";
	}
	
	/**
	 * 获取应用根目录
	 * @param context
	 * @return
	 */
	public static String getAppDir(Context context, String appName) {
		if (getSDPath().equals("")) {
			return "";
		}
		String rootPath = getSDPath() + appName;
		File file = new File(rootPath);
		if (file.exists()) {
			return rootPath;
		} else {
			file.mkdirs();
			return rootPath;
		}
	}
	
	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
										   int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 *
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
										   int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 复制单个文件
	 *
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 */
	public static void copyFile(String oldPath, String newPath) {
		FileOutputStream fs = null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.flush();
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除指定目录及其中的所有内容。
	 * @param dir 要删除的目录
	 */
	public static boolean deleteDirectory(File dir) {
		File[] entries = dir.listFiles();
		int sz = entries.length;

		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			} else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}
		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if(file.exists()) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
			return false;
		}
		//判断目标文件所在的目录是否存在
		if(!file.getParentFile().exists()) {
			//如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if(!file.getParentFile().mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");
				return false;
			}
		}
		//创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + destFileName + "成功！");
				return true;
			} else {
				System.out.println("创建单个文件" + destFileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
			return false;
		}
	}

	/**
	 * 拷贝配置文件数据
	 *
	 * @param assetDir
	 *            文件夹名称
	 * @param dir
	 *            目录
	 * @param context
	 */
	public static void copyAssets(String assetDir, String dir, Context context) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		if (!mWorkingPath.exists()) {
			mWorkingPath.mkdir();

		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						copyAssets(fileName, dir + fileName + File.separator,
								context);
					} else {
						copyAssets(assetDir + File.separator + fileName, dir
								+ File.separator + fileName, context);
					}
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length()) {
					in = context.getAssets().open(
							assetDir + File.separator + fileName);
				} else {
					in = context.getAssets().open(fileName);
				}
				OutputStream out = new FileOutputStream(outFile);

				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (Exception e) {
				Log.e("eeeeeeeee", e.getMessage());
			}
		}
	}

	/**
	 * 数据保留小数位数格式转换
	 * @param num
	 * @param maximumFractionDigits 保留的小数位数
	 * @return
	 */
	public static String numberFormat(double num, int maximumFractionDigits){
		StringBuffer formatStr = new StringBuffer("0.");
		for (int i = 0; i < maximumFractionDigits; i++) {
			formatStr.append("0");
		}
		String pattern = formatStr.toString();
		if(maximumFractionDigits == 0){
			pattern = "0";
		}
		DecimalFormat g=new DecimalFormat(pattern);
		g.setGroupingUsed(false);
		return g.format(num);
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 获取屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 日期转换成字符串
	 * @param date
	 * @param  formatStr 所需日期对应的格式, 默认"yyyy-MM-dd HH:mm:ss"
	 * @return str
	 */
	public static String DateToStr(Date date, String formatStr) {
		if(TextUtils.isEmpty(formatStr)){
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * @param str 需要转换的日期字符串
	 * * @param  formatStr 传入日期字符串对应的格式, eg:"yyyy-MM-dd HH:mm:ss"
	 * @return date
	 */
	public static Date StrToDate(String str, String formatStr) {

		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 比较两个日期的先后
	 * @param date1
	 * @param date2
     * @return
     */
	public static boolean isDateBefore(Date date1, Date date2){
		if(date1.getYear() - date2.getYear() < 0){
			return true;
		}else{
			if(date1.getMonth() - date2.getMonth()< 0){
				return  true;
			}else{
				if(date1.getDay() - date2.getDay() < 0){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	/**
	 * 获取当前的日期字符串
	 * * @param  formatStr 所需日期字符串对应的格式, eg:"yyyy-MM-dd HH:mm:ss"
	 * @return date
	 */
	public static String getDateStr(String formatStr) {
		String date = "";
		if(TextUtils.isEmpty(formatStr)){
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		date = format.format(new Date());
		return date;
	}
}
