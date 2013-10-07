/* The class CameraActivity detects the camera for this Android device,
 * and launches a new PhotoHandler to handle the picture file it 
 * captures.
 */
package com.angles.angles;

import com.angles.angles.R;

import javax.jdo.annotations.*;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CameraActivity extends Activity {
	
	public final static String DEBUG_TAG = "CameraActivity";
	
	private Camera camera;
	
	private int cameraId = 0;
	
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
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ongoing_event_activity_main);
		
		/* The boolean cameraDetected first checks if the environment has
		 * any cameras.
		 */
		boolean cameraDetected = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		
		/* Now we check to see which direction they are facing. */
		if (cameraDetected == false) {
			
			Toast.makeText(this,"No Camera detected!",Toast.LENGTH_LONG).show();
			
		} else {
			
			cameraId = findFrontFacingCamera();
			
			if (cameraId < 0) {
				
				Toast.makeText(this, "No Front Facing Camera Found!", Toast.LENGTH_LONG).show();
				
				cameraId = findRearFacingCamera();
			}
			
			if (cameraId < 0) {
				
				Toast.makeText(this, "No Rear Facing Camera Found!", Toast.LENGTH_LONG).show();
			}
			else
				
				/* Launch the Camera */
				camera = Camera.open(cameraId);
		}
	}
	
	public void onClick(View view) {
		
		/* Taking a picture launches a new PhotoHandler instance.
		 * PhotoHandler gets the file data and saves it locally.
		 * 
		 * NOTE: Saving locally is only temporary until the photos
		 * can be sent to the datastore.
		 */
		camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
	}
	
	protected void onPause() {
		
		if (camera != null) {
			
			camera.release();
			
			camera = null;
		}
		
		super.onPause();
	}
}
