package com.angles.angles;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements Callback {

	private static final String DEBUG_TAG = "CameraPreview";
	
	private SurfaceHolder surfaceHolder;
	
	private Camera camera;
	
	public CameraPreview(Context context, Camera instanceOfCamera) {
		
		super(context);
		
		camera = instanceOfCamera;
		
		surfaceHolder = getHolder();
		
		surfaceHolder.addCallback(this);
		 
		/* This is a deprecated setting for Android OS versions earlier
		 * than 3.0, recommended by Google.
		 */
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			
			camera.setPreviewDisplay(holder);
			
			camera.startPreview();
		} catch (IOException e) {
			
			Log.e(DEBUG_TAG, "Could not set Camera Preview " + e.getMessage());
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		
		if (holder.getSurface() == null) {
			
			/* Preview Surface does not exist */
			return;
		}
		else {
			
			/* Stop the Preview before making changes */
			camera.stopPreview();
		}
		
		/*Start Preview with new settings */
		try {
			
			camera.setPreviewDisplay(holder);
			
			camera.startPreview();
		} catch (Exception e) {
			
			Log.e(DEBUG_TAG, "Could not restart Preview " + e.getMessage());
		}
	}
}
