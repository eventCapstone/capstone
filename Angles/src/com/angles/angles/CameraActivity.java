package com.angles.angles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.angles.angles.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CameraActivity extends Activity {
	
	private static final String DEBUG_TAG = "CameraActivity";
	
	private Camera camera;
	
	private CameraPreview cameraPreview;
	
	private PictureCallback picture;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		camera = getCameraInstance();
		
		cameraPreview = new CameraPreview(this, camera);
		
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		
		preview.addView(cameraPreview);
	}
	
	public void onPictureTaken(byte[] data, Camera camera) {
		
		File pictureFile = getOutputMediaFile();
		
		if (pictureFile == null) {
			
			Log.e(DEBUG_TAG, "Error creating media file. Check storage permissions.");
			
			return;
		}
		
		try {
			
			FileOutputStream fos = new FileOutputStream(pictureFile);
			
			fos.write(data);
			
			fos.close();
		} catch (FileNotFoundException fne) {
			
			Log.e(DEBUG_TAG, "File not found: " + fne.getMessage());
		} catch (IOException ioe) {
			
			Log.e(DEBUG_TAG, "Error Accessing File. " + ioe.getMessage());
		}
	}
	
	private Camera getCameraInstance() {
		
		Camera camera = null;
		
		boolean cameraDetected = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		
		/* Now we check to see which direction they are facing. */
		if (cameraDetected == false) {
			
			Toast.makeText(this,"No Camera detected!",Toast.LENGTH_LONG).show();
			
		} else {
			
			int cameraId = findFrontFacingCamera();
			
			if (cameraId < 0) {
				
				Toast.makeText(getApplicationContext(), "No Front Facing Camera Found!", Toast.LENGTH_LONG).show();
				
				cameraId = findRearFacingCamera();
			}
			
			if (cameraId < 0) {
				
				Toast.makeText(getApplicationContext(), "No Rear Facing Camera Found!", Toast.LENGTH_LONG).show();
			}
			else {
				
				try {
					
					camera = Camera.open(cameraId);
				}
				catch (Exception e) {
					
					Log.e(DEBUG_TAG, "Camera may be in-use and not released.");
				}
			}
		}		
		
		return camera;
	}
	
	/* This method searches the local Android environment for a 
	 * rear-facing camera. It returns the id number of the rear-
	 * facing camera, if any. It returns -1 if none are found.
	 * 
	 * @return cameraId
	 */
	private int findRearFacingCamera() {
		
		int cameraId = -1;
		
		int numberOfCameras = Camera.getNumberOfCameras();
		
		/* This for loop checks the CameraInfo data to see if 
		 * any camera has the attribute CAMERA_FACING_BACK. It returns 
		 * the id of any camera that does.
		 */
		
		for (int i = 0; i < numberOfCameras; i++) {
			
			CameraInfo info = new CameraInfo();
			
			Camera.getCameraInfo(i, info);
			
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				
				Log.d(DEBUG_TAG, "Rear Camera Found");
				
				cameraId = i;
				
				break;
			}
		}
		return cameraId;
	}
	
	/* This method searches the local Android environment for a 
	 * front-facing camera. It returns the id number of the front-
	 * facing camera, if any. It returns -1 if none are found.
	 * 
	 * @return cameraId
	 */
	private int findFrontFacingCamera() {
		
		int cameraId = -1;
		
		int numberOfCameras = Camera.getNumberOfCameras();
		
		/* This for loop checks the CameraInfo data to see if 
		 * any camera has the attribute CAMERA_FACING_FRONT. It returns 
		 * the id of any camera that does.
		 */
		for (int i = 0; i < numberOfCameras; i++) {
			
			CameraInfo info = new CameraInfo();
			
			Camera.getCameraInfo(i, info);
			
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				
				Log.d(DEBUG_TAG, "Front Camera Found");
				
				cameraId = i;
				
				break;
			}
		}
		return cameraId;
	}
	
	private static File getOutputMediaFile() {
		
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyAngles");
	    
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	    	
	        if (! mediaStorageDir.mkdirs()){
	        	
	            Log.d("MyCameraApp", "failed to create directory");
	            
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}
}