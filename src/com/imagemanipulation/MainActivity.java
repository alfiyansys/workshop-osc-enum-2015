package com.imagemanipulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagemanipulation.TouchImageView.OnTouchImageViewListener;

public class MainActivity extends Activity {
	int widthImgGallery = 150, heightImgGallery = 120;
	private BitmapImageEffect bitmapObj;
	private static final int CAMERA_REQUEST = 0;
	private static final int GALLERY_REQUEST = 1;
	private static final int IMG_MAX_PIXEL = 640;
	private static String APP_LOCAL_DATA_CACHE;
	private static String EXTERNAL_LOCAL_DIR;
	private static String FILE_NAME = "captured-image.jpg";
	private ImageView imgChoose;
	private TouchImageView imageView01, imageView02;
	private Intent intent;
	private Gallery ga;
	private Bitmap currentBitmap;
	private Bitmap[] iconPics = new Bitmap[new BitmapImageEffect().length];
	private Bitmap[] Pics = new Bitmap[new BitmapImageEffect().length];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		EXTERNAL_LOCAL_DIR = String.valueOf(getExternalFilesDir("")) + "/";
		APP_LOCAL_DATA_CACHE = String.valueOf(getExternalCacheDir()) + "/";

		File extDir = new File(APP_LOCAL_DATA_CACHE);
		if (!extDir.exists()) {
			extDir.mkdir();
		}

		ga = (Gallery) findViewById(R.id.Gallery01);
		imgChoose = (ImageView) findViewById(R.id.ImageChoose);
		imageView01 = (TouchImageView) findViewById(R.id.ImageView01);
		imageView02 = (TouchImageView) findViewById(R.id.ImageView02);
		imageView01.setMaxZoom(2f);
		imageView02.setMaxZoom(2f);
		ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				currentBitmap = existFileImage(arg2);
				imageView02.setImageBitmap(currentBitmap);
				imageView01.setZoom(1f);
				imageView02.setZoom(1f);
			}

		});

		//
		// Each image has an OnTouchImageViewListener which uses its own
		// TouchImageView
		// to set the other TIV with the same zoom variables.
		//
		imageView01.setOnTouchImageViewListener(new OnTouchImageViewListener() {
			@Override
			public void onMove() {
				imageView02.setZoom(imageView01);
			}
		});

		imageView02.setOnTouchImageViewListener(new OnTouchImageViewListener() {
			@Override
			public void onMove() {
				imageView01.setZoom(imageView02);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.items, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.camera:
			intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File file = new File(APP_LOCAL_DATA_CACHE + FILE_NAME);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, CAMERA_REQUEST);
			break;
		case R.id.gallery:
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, GALLERY_REQUEST);
			break;
		case R.id.save:
			if (currentBitmap == null) {
				alertSaveImage("Save error",
						"Please load Image first then choose selection to compare!");
				return true;
			}
			LayoutInflater factory = LayoutInflater.from(this);

			// text_entry is an Layout XML file containing two text field to
			// display in alert dialog
			final View textEntryView = factory.inflate(R.layout.save_dialog,
					null);
			final EditText input1 = (EditText) textEntryView
					.findViewById(R.id.editText1);
			final EditText input2 = (EditText) textEntryView
					.findViewById(R.id.editText2);

			input1.setText(EXTERNAL_LOCAL_DIR, TextView.BufferType.EDITABLE);
			input2.setText(
					"image-"
							+ new SimpleDateFormat("yyyyMMdd_HHmmss")
									.format(Calendar.getInstance().getTime())
							+ ".png", TextView.BufferType.EDITABLE);

			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setIcon(R.drawable.icon)
					.setTitle("Save new image")
					.setView(textEntryView)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String filePath = input1.getText()
											.toString()
											+ "/"
											+ input2.getText().toString();
									filePath = filePath.replaceAll("//", "/");
									storeImage(filePath, currentBitmap);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// ignore
								}
							});
			alert.show();
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Pics = new Bitmap[new BitmapImageEffect().length];
			String targetPath = APP_LOCAL_DATA_CACHE + FILE_NAME;
			Bitmap objBitmap = BitmapFactory.decodeFile(targetPath);

			if (requestCode == GALLERY_REQUEST) {
				Uri targetUri = data.getData();
				try {
					InputStream image_stream = getContentResolver()
							.openInputStream(targetUri);
					objBitmap = BitmapFactory.decodeStream(image_stream);
				} catch (FileNotFoundException e) {
					this.log("Not found");
				}
			}

			if (IMG_MAX_PIXEL < objBitmap.getWidth()
					|| IMG_MAX_PIXEL < objBitmap.getHeight()) {
				float xSize = (float) IMG_MAX_PIXEL / objBitmap.getWidth();
				int newHeight = (int) (xSize * objBitmap.getHeight());
				Bitmap resized = Bitmap.createScaledBitmap(objBitmap,
						IMG_MAX_PIXEL, newHeight, true);
				objBitmap = resized;
			}
			
			imgChoose.setVisibility(View.GONE);
			currentBitmap = null;
			imageView02.setImageBitmap(null);
			imageView01.setImageBitmap(objBitmap);
			bitmapObj = new BitmapImageEffect(objBitmap);
			fillGalleryArray();

			ga.setAdapter(new ImageAdapter(this));
		}
	}

	private Bitmap existFileImage(int val) {
		if (Pics[val] == null) {
			Pics[val] = bitmapObj.getEffect(val);
		}

		return Pics[val];
	}

	private void fillGalleryArray() {
		BitmapImageEffect obj = new BitmapImageEffect(
				bitmapObj.getOriginalBitmap(), widthImgGallery,
				heightImgGallery);
		for (int i = 0; i < iconPics.length; i++) {
			iconPics[i] = obj.getEffect(i);
		}
	}

	private void storeImage(String fileLocation, Bitmap image) {
		File pictureFile = new File(fileLocation);
		if (pictureFile == null) {
			alertSaveImage("Save error",
					"Error while storing image file into external media.");
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
			alertSaveImage("Save success", "Image saved to " + fileLocation);
		} catch (FileNotFoundException e) {
			alertSaveImage("Save error",
					"Error while storing image file into external media.");
		} catch (IOException e) {
			alertSaveImage("Save error",
					"Error while storing image file into external media.");
		}
	}

	private void alertSaveImage(String title, String message) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title).setMessage(message).setPositiveButton("OK", null)
				.show();
	}

	private static void log(Object txt) {
		Log.e("Pencit", String.valueOf(txt));
	}

	private class ImageAdapter extends BaseAdapter {
		private Context ctx;
		private int imageBackground;

		public ImageAdapter(Context c) {
			ctx = c;
			TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
			imageBackground = ta.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 1);
			ta.recycle();
		}

		@Override
		public int getCount() {
			return bitmapObj.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ImageView iv = new ImageView(ctx);
			iv.setImageBitmap(iconPics[arg0]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setLayoutParams(new Gallery.LayoutParams(widthImgGallery,
					heightImgGallery));
			iv.setBackgroundResource(imageBackground);
			return iv;
		}

	}
}