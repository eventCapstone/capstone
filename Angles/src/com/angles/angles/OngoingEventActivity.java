package com.angles.angles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.angles.model.Angle;
import com.angles.model.User;
import com.google.cloud.backend.android.CloudBackendAsync;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.DBTableConstants;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OngoingEventActivity extends Activity {
	
	private static final String DEBUG_TAG = "OngoingEventActivity";
	
	public static final int REQUEST_CODE = 011;
	
	public ImageView imageView;
	
	public Activity currentActivity;
	
	public User user;
	
	private Uri recentPhotoUri;
	
	private Bitmap recentPhoto;
	
	private LinearLayout recentPhotoGallery;
	
	private int sizeOfGallery = 0;
		
	private File tempFile;
	
	private Display display;
			
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		AnglesController.getInstance().getDisplayManager().displayOngoingEventActivity(this);

   	 	AnglesController.getInstance().getTouchManager().setOngoingEventListeners(this);
   	 	
   	 	display = getWindowManager().getDefaultDisplay();
   	    	 	
   	 	recentPhotoGallery = (LinearLayout)findViewById(R.id.recentPhotoGallery);
   	 	
   	 	findViewById(R.id.btnCapturePhoto).setOnClickListener (new OnClickListener() {
   	 	   	 		
			public void onClick(View v) {
				
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
												
		        tempFile = new File(Environment.getExternalStorageDirectory(),  
		        		"angle" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg");
		        
		        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		        
		        recentPhotoUri = Uri.fromFile(tempFile);
		        
				startActivityForResult(cameraIntent, REQUEST_CODE);
			}
		});
   	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
		 
		Log.d(DEBUG_TAG, "Request code: " + requestCode);
		
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
						
			/*	Retrieve data from returned Activity
			 */
			
            getContentResolver().notifyChange(recentPhotoUri, null);

            ContentResolver cr = getContentResolver();
            
            try {

                recentPhoto = MediaStore.Images.Media.getBitmap(cr, recentPhotoUri);

            } catch (Exception e) {

                 Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
                        																											
			/* Display the recently taken picture in the gallery.
			 * The gallery size is kept to no more than 4 pictures. */
            if (sizeOfGallery > 3) {
            	
            	recentPhotoGallery.removeViewAt(3);
            	
            	recentPhotoGallery.addView(insertPhoto(tempFile.getAbsolutePath()), 0);

            }
            else {
            	
            	recentPhotoGallery.addView(insertPhoto(tempFile.getAbsolutePath()), 0);
            	
            	sizeOfGallery++;
            }
            
			/*	Sending Image to Cloud */
			user = AnglesController.getInstance().getAnglesUser();
			
			byte[] data = convertImageToByteArray(recentPhotoUri);
			
			Log.d(DEBUG_TAG, "Byte data size: " + String.valueOf(data.length));
			
			CloudBackendAsync backend = new CloudBackendAsync(getApplicationContext());
			Angle newAngle = new Angle(data, user);
			CloudEntity entity = new CloudEntity(DBTableConstants.DB_TABLE_ANGLE);
			entity.put(DBTableConstants.DB_ANGLE_IMAGE, newAngle.getImage());
			entity.put(DBTableConstants.DB_ANGLE_CREATED_BY, newAngle.getCreatedBy().getUserName());
			
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
            
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            
            data = baos.toByteArray();
			
		} catch (FileNotFoundException fne) {
			
			Log.e(DEBUG_TAG, "Error converting image to byte array: File not found.");
		}
		
		return data;
	}
	
	@SuppressLint("NewApi")
	public View insertPhoto(String path) {
    	
    	/* The bitmap is currently decoded based on calculations
    	 * from a relative screen size. It assumes the picture should
    	 * have a height no more than half the screen height, and a
    	 * width no more than half the screen width.
    	 */
		int targetWidth = 0;
		
		int targetHeight = 0;
		
		/*	The previous means for calculating screen height and width
		 *  have been deprecated as of API level 13. This check uses
		 *  the current recommended getSize() method for API levels 13
		 *  and up, and the supported way for previous API levels otherwise.
		 */
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			Point size = new Point();
    	
			display.getSize(size);
			
			targetWidth = (size.x)/2;
			
			targetHeight = (size.y)/2;
		}
		else {
		
			targetWidth = (getWindowManager().getDefaultDisplay().getWidth()) / 2;
			
			targetHeight = (getWindowManager().getDefaultDisplay().getHeight()) / 2;
		}
		
        Bitmap bm = decodeSampledBitmapFromUri(path, 400, 600);
        
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(targetWidth, targetHeight));
        layout.setGravity(Gravity.CENTER);
        
        Toast.makeText(this, "Size.x: " + targetWidth
        		+ ", Size.y: " + targetHeight, Toast.LENGTH_LONG).show();
        
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(targetWidth, targetHeight));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
        
        layout.addView(imageView);
        
        return layout;
    }
    
    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
    	
        Bitmap bm = null;
        
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options); 
        
        return bm;
    }
    
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    	
    	final int height = options.outHeight;
    	
    	final int width = options.outWidth;
    	
    	int inSampleSize = 1;
    	        
    	if (height > reqHeight || width > reqWidth) {
    		
    		if (width > height) {
    			
    			inSampleSize = Math.round((float)height / (float)reqHeight);
    		}
    		else {
    			
    			inSampleSize = Math.round((float)width / (float)reqWidth);
    		}
    	}
    	
    	return inSampleSize;
    }
}