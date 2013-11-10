package com.angles.angles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.angles.angles.R;
import com.angles.model.Angle;
import com.angles.model.User;
import com.google.cloud.backend.android.CloudBackendAsync;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.DBTableConstants;
import com.angles.view.ConfirmPictureDialog;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CameraActivity extends Activity {
	
private static final String DEBUG_TAG = "CameraActivity";
	
	private Camera camera;
	private CameraPreview cameraPreview;
	private PictureCallback pictureCallback;
	
	private PictureCallback picture;
	
	private static final int pictureOkDialog = 11;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		AnglesController.getInstance().getDisplayManager().displayCameraActivity(this);
		camera = getCameraInstance();
		cameraPreview = new CameraPreview(getApplicationContext(), camera);
		pictureCallback = new AnglesPictureCallback(this);
		
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
		preview.addView(cameraPreview);
				
		findViewById(R.id.camera_preview).setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				camera.takePicture(null, null, pictureCallback);
				Toast.makeText(getApplicationContext(), "Picture snap", Toast.LENGTH_LONG).show();
			}
		});
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
	
	private class AnglesPictureCallback implements PictureCallback  {
		Activity currentActivity;
		User user;
		
		public AnglesPictureCallback(Activity activity) {
			this.currentActivity = activity;
			user = AnglesController.getInstance().getAnglesUser();
		}
		
		public void onPictureTaken(byte[] data, Camera camera) {
			/*Creating a time stamp of when the picture was taken. This will
			 * be appended to the filename for the picture taken.
			 */
			String timeStamp = String.valueOf((Calendar.getInstance()).getTimeInMillis());
			String filename = "angle" + timeStamp;
			File rawImageFile = new File(getFilesDir().toString());

			CloudBackendAsync backend = new CloudBackendAsync(currentActivity);
			Angle newAngle = new Angle(data, user);
			CloudEntity entity = new CloudEntity(DBTableConstants.DB_TABLE_ANGLE);
			entity.put(DBTableConstants.DB_ANGLE_IMAGE, newAngle.getImage());
			entity.put(DBTableConstants.DB_ANGLE_CREATED_BY, newAngle.getCreatedBy().userName);
			
			CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			     @Override
			     public void onComplete(final CloudEntity result) {
			    	 Toast.makeText(getApplicationContext(),"Angle Sent!", Toast.LENGTH_LONG).show();
			     }

			     @Override
			     public void onError(final IOException exception) {
			    	 handleEndpointException(exception);
			     }
			};

			    // execute the insertion with the handler
			backend.insert(entity, handler);
			
//			try {
//				FileOutputStream fos = new FileOutputStream(rawImageFile);
//				fos.write(data);
//				fos.close();
//				
//				/* TODO: Create Dialog Fragment to give the user the option
//				 *  to approve or re-take the photo.
//				 */
//				
//				//NOTE: This requires min SDK 11, which Mike's phone apparently doesn't have
//				//new ConfirmPictureDialog().show(getFragmentManager(), "Send Photo?");
//				
//				/* Angles photos are not stored locally. The temporary 
//				 * file must be deleted.
//				 */
//				//rawImageFile.delete();
//				
//			} catch (FileNotFoundException e) {
//				Log.e(DEBUG_TAG, "File Not Found " + e.getStackTrace());
//			} catch (IOException ioe) {
//				Log.e(DEBUG_TAG, "I/O Exception " + ioe.getStackTrace());
//			}			
			camera.release();
		}
	}
	
	private void handleEndpointException(IOException e) {
	    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	}
}
