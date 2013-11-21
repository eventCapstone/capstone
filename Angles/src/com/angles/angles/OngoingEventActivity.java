package com.angles.angles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.angles.model.Angle;
import com.angles.model.User;
import com.google.appengine.api.datastore.Blob;
import com.google.apphosting.api.search.AclPb.Scope;
import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudBackendAsync;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery;
import com.google.cloud.backend.android.DBTableConstants;
import com.google.cloud.backend.android.F;

import android.app.Activity;
import android.content.ContentResolver;
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
	private Uri recentPhotoUri;
	private Bitmap recentPhoto;
	private List<CloudEntity> result;
			
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AnglesController.getInstance().getDisplayManager().displayOngoingEventActivity(this);
   	 	AnglesController.getInstance().getTouchManager().setOngoingEventListeners(this);

   	 	findViewById(R.id.btnCapturePhoto).setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		        File tempFile = new File(Environment.getExternalStorageDirectory(),  
		        		"angle" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg");
		        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		        recentPhotoUri = Uri.fromFile(tempFile);
				startActivityForResult(cameraIntent, 011);
			}
		});
   	 	
//		final CloudBackend cb = new CloudBackend();
//		final CloudQuery cq = new CloudQuery(DBTableConstants.DB_TABLE_ANGLE);
//		//cq.setFilter(F.eq("ANGLE_CREATED_BY","Walter White"));
//		cq.setLimit(1);
//
//		Thread theThread = new Thread() {
//			@Override
//			public void run() {
//				try {
//						result = cb.list(cq);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//		theThread.start();
//		
//		try {
//			theThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		if(result.size() == 1) {
//			CloudEntity angle = result.get(0);
//			Object angleImage = (ArrayList<Byte>)angle.get("ANGLE_IMAGE");
//			
//			if (angleImage == null) {
//				Map<String, Object> propertyMap = angle.getProperties();
//				String debug = "";
//				for (String str: propertyMap.keySet()) {
//					debug += (str + "");
//				}
//				Toast.makeText(this, "Properties" + debug, Toast.LENGTH_LONG).show();
//				
//			} else {
//				Bitmap bitmap = BitmapFactory.decodeByteArray(angleArray, 0, angleArray.length);
//				if (bitmap != null) {
//					ImageView view = (ImageView)findViewById(R.id.imageViewRecentAngle);
//					view.setImageBitmap(bitmap);
//				}
//			}
//		}
   	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
		if (requestCode == 011) {
			/*	Retrieve data from returned Activity
			 */
            getContentResolver().notifyChange(recentPhotoUri, null);
            ContentResolver cr = getContentResolver();
            
            try {
                recentPhoto = MediaStore.Images.Media.getBitmap(cr, recentPhotoUri);
            } catch (Exception e) {
                 Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
			
			/* Display the recently taken picture
	   	 	 */
            
            ((ImageView)findViewById(R.id.imageViewRecentAngle)).setImageBitmap(recentPhoto);
																											
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
			
			byte[] data = convertImageToByteArray(recentPhotoUri);
			
			Log.d(DEBUG_TAG, "Byte data size: " + String.valueOf(data.length));
			
			CloudBackendAsync backend = new CloudBackendAsync(getApplicationContext());
			
			Angle newAngle = new Angle(data, user);
			Toast.makeText(this, newAngle.getImage().toString(), Toast.LENGTH_LONG).show();
			
			CloudEntity entity = new CloudEntity(DBTableConstants.DB_TABLE_ANGLE);
			entity.put(DBTableConstants.DB_ANGLE_CREATED_BY, newAngle.getCreatedBy().userName);
			entity.put(DBTableConstants.DB_ANGLE_IMAGE, newAngle.getImage());
			entity.put("TEST_VALUE", new User("Test", "User"));
			
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
	
	private byte[] convertImageToByteArray(Uri uri) {
		byte[] data = null;

		try {
			ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int imageSize = 100;
            do {
            	imageSize /= 2;
            	bitmap.compress(Bitmap.CompressFormat.JPEG, imageSize, baos);
            	data = baos.toByteArray();
            } while (data.length > 1000000);
		} catch (FileNotFoundException fne) {
			Log.e(DEBUG_TAG, "Error converting image to byte array: File not found.");
		}
		
		return data;
	}
}