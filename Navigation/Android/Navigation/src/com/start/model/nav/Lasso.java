package com.start.model.nav;

import java.util.List;

import android.graphics.PointF;
import android.util.Log;

/**
 * a polygon represents a lasso.
 * @author start
 *
 */
public class Lasso {
	// polygon coordinates
	private double[] mPolyX, mPolyY;

	// number of size in polygon
	private int mPolySize;

	/**
	 * default constructor
	 * 
	 * @param px
	 *            polygon coordinates X
	 * @param py
	 *            polygon coordinates Y
	 * @param ps
	 *            polygon sides count
	 */
	public Lasso(double[] px, double[] py, int ps) {
		this.mPolyX = px;
		this.mPolyY = py;
		this.mPolySize = ps;
	}

	/**
	 * constructor
	 * 
	 * @param pointFs
	 *            points list of the lasso
	 */
	public Lasso(List<PointF> pointFs) {
		this.mPolySize = pointFs.size();
		this.mPolyX = new double[this.mPolySize];
		this.mPolyY = new double[this.mPolySize];

		for (int i = 0; i < this.mPolySize; i++) {
			this.mPolyX[i] = pointFs.get(i).x;
			this.mPolyY[i] = pointFs.get(i).y;
		}
		Log.d("lasso", "lasso size:" + mPolySize);
	}

	/**
	 * check if this polygon contains the point.
	 * 
	 * @param x
	 *            point coordinate X
	 * @param y
	 *            point coordinate Y
	 * @return point is in polygon flag
	 */
	public boolean contains(double x, double y) {
		boolean result = false;
		for (int i = 0, j = mPolySize - 1; i < mPolySize; j = i++) {
			if ((mPolyY[i] < y && mPolyY[j] >= y)
					|| (mPolyY[j] < y && mPolyY[i] >= y)) {
				if (mPolyX[i] + (y - mPolyY[i]) / (mPolyY[j] - mPolyY[i])
						* (mPolyX[j] - mPolyX[i]) < x) {
					result = !result;
				}
			}
		}
		return result;
	}
}