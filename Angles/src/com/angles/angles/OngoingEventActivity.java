package com.angles.angles;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.angles.model.Angle;
import com.angles.model.User;
import com.google.cloud.backend.android.CloudBackendAsync;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.DBTableConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class OngoingEventActivity extends Activity {
	
	private static final String DEBUG_TAG = "OngoingEventActivity";
	
	public ImageView imageView;
	
	public Activity currentActivity;
	
	public User user;
		
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		AnglesController.getInstance().getDisplayManager().displayOngoingEventActivity(this);

   	 	AnglesController.getInstance().getTouchManager().setOngoingEventListeners(this);
   	 	   	 	   	 	
   	 	findViewById(R.id.btnCapturePhoto).setOnClickListener (new OnClickListener() {
 		
			public void onClick(View v) {
				
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								
				startActivityForResult(cameraIntent, 011);
			}
		});
   	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
		
		if (requestCode == 011) {
			
			/*	Retrieve data from returned Activity
			 */
			byte[] data = returnedIntent.getByteArrayExtra("returnedIntent");
							   	 	
	   	 	/* Display the recently taken picture
	   	 	 */
			
			//Create Bitmap							
			//imageView = (ImageView)findViewById(R.id.imageViewRecentAngle);
																					
			/*Creating a time stamp of when the picture was taken. This will
			 * be appended to the filename for the picture taken.
			 */
			String timeStamp = String.valueOf((Calendar.getInstance()).getTimeInMillis());
			String filename = "angle" + timeStamp;
			File rawImageFile = new File(getFilesDir().toString());

			/*	Sending Image to Cloud Datastore
			 * 
			 */
			user = AnglesController.getInstance().getAnglesUser();
						
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
				public void onError(final IOException ioe) {
	  
					Log.e(DEBUG_TAG, ioe.toString());
				}
			};

			    /*	Execute the insertion with the handler.
			     */
			backend.insert(entity, handler);
		}
		else {
			
			Toast.makeText(this, "Photo not taken.", Toast.LENGTH_SHORT).show();
		}
		
		super.onActivityResult(requestCode, resultCode, returnedIntent);
	}
}