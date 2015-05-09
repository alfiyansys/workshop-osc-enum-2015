package com.imagemanipulation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class BitmapImageEffect {
	public int length = 14;
	private Bitmap objInBitmap, objOutBitmap;

	public BitmapImageEffect() {

	}

	public BitmapImageEffect(Bitmap obj) {
		this.objInBitmap = obj;
	}

	public BitmapImageEffect(Bitmap obj, int width, int height) {
		this.objInBitmap = Bitmap.createScaledBitmap(obj, width, height, true);
	}

	public Bitmap getOriginalBitmap() {
		return objInBitmap;
	}

	public Bitmap getEffect(int val) {
		objOutBitmap = Bitmap.createBitmap(objInBitmap.getWidth(),
				objInBitmap.getHeight(), Config.RGB_565);

		switch (val) {
		case 0:
			this.doFlipHorizontal();
			break;
		case 1:
			this.doFlipVertical();
			break;
		case 2:
			this.doRotate90();
			break;
		case 3:
			this.doRotate180();
			break;
		case 4:
			this.doRed();
			break;
		case 5:
			this.doGreen();
			break;
		case 6:
			this.doBlue();
			break;
		case 7:
			this.doSepia();
			break;
		case 8:
			this.doGrayscaleRGB();
			break;
		case 9:
			this.doGrayscaleR();
			break;
		case 10:
			this.doGrayscaleG();
			break;
		case 11:
			this.doGrayscaleB();
			break;
		case 12:
			this.doInvers();
			break;
		case 13:
			this.doAutoLevel();
			break;
		}

		return objOutBitmap;
	}

	private void doFlipHorizontal() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				objOutBitmap.setPixel(objInBitmap.getWidth() - 1 - x, y, w);
			}
		}
	}

	private void doFlipVertical() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				objOutBitmap.setPixel(x, objInBitmap.getHeight() - 1 - y, w);
			}
		}
	}

	private void doRotate90() {
		objOutBitmap = Bitmap.createBitmap(objInBitmap.getHeight(),
				objInBitmap.getWidth(), Config.RGB_565);

		for (int x = 0; x < objInBitmap.getHeight(); x++) {
			for (int y = 0; y < objInBitmap.getWidth(); y++) {
				int w = objInBitmap.getPixel(objInBitmap.getWidth() - 1 - y, x);
				objOutBitmap.setPixel(objInBitmap.getHeight() - 1 - x,
						objInBitmap.getWidth() - 1 - y, w);
			}
		}
	}

	private void doRotate180() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				objOutBitmap.setPixel(objInBitmap.getWidth() - 1 - x,
						objInBitmap.getHeight() - 1 - y, w);
			}
		}
	}

	private void doRed() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wr = Color.red(w);
				int new_w = Color.rgb(wr, 0, 0);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}

	private void doGreen() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wg = Color.green(w);
				int new_w = Color.rgb(0, wg, 0);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}

	private void doBlue() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wb = Color.blue(w);
				int new_w = Color.rgb(0, 0, wb);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}

	private void doSepia() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wr = (int) ((Color.red(w) * 0.393)
						+ (Color.green(w) * 0.769) + (Color.blue(w) * 0.189));
				int wg = (int) ((Color.red(w) * 0.349)
						+ (Color.green(w) * 0.686) + (Color.blue(w) * 0.168));
				int wb = (int) ((Color.red(w) * 0.272)
						+ (Color.green(w) * 0.534) + (Color.blue(w) * 0.130));
				if (wr > 255) {
					wr = 255;
				}
				if (wg > 255) {
					wg = 255;
				}
				if (wb > 255) {
					wb = 255;
				}
				int new_w = Color.rgb(wr, wg, wb);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}

	private void doGrayscaleRGB() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int xg = (int) ((Color.red(w) + Color.green(w) + Color.blue(w)) / 3);
				int new_w = Color.rgb(xg, xg, xg);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}
	}

	private void doGrayscaleR() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wr = Color.red(w);
				int new_w = Color.rgb(wr, wr, wr);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}
	}

	private void doGrayscaleG() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wg = Color.green(w);
				int new_w = Color.rgb(wg, wg, wg);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}
	}

	private void doGrayscaleB() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int wb = Color.blue(w);
				int new_w = Color.rgb(wb, wb, wb);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}
	}

	private void doInvers() {
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int xg = (int) ((Color.red(w) + Color.green(w) + Color.blue(w)) / 3);
				int xo = 128;
				int xi = (xo - xg);
				if (xi < 0) {
					xi = 0;
				}
				int new_w = Color.rgb(xi, xi, xi);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}

	private void doAutoLevel() {
		int xgmax = 0;
		int xgmin = 255;
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int xg = (int) ((Color.red(w) + Color.green(w) + Color.blue(w)) / 3);
				if (xg > xgmax) {
					xgmax = xg;
				}
				if (xg < xgmin) {
					xgmin = xg;
				}
			}
		}
		for (int x = 0; x < objInBitmap.getWidth(); x++) {
			for (int y = 0; y < objInBitmap.getHeight(); y++) {
				int w = objInBitmap.getPixel(x, y);
				int xg = (int) ((Color.red(w) + Color.green(w) + Color.blue(w)) / 3);
				int xb = (int) (255 * (xg - xgmin) / (xgmax - xgmin));
				int new_w = Color.rgb(xb, xb, xb);
				objOutBitmap.setPixel(x, y, new_w);
			}
		}

	}
}
