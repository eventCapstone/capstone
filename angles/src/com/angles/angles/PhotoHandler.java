/*	The class PhotoHandler is used to create a new picture and save it
 *  to local storage. Saving to local storage is only a temporary,
 *  testing solution until they can be sent to the datastore instead.
 */
package com.angles.angles;

import javax.jdo.annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

@PersistenceCapable
public class PhotoHandler implements PictureCallback {
	
	private final Context context;
		
	public PhotoHandler(Context context) {
		
		this.context = context;
	}
	
	/* The method getDir() returns the directory where our App stores
	 * its photos
	 * 
	 * @return File
	 */
	private File getDir() {
		
		File localFileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		return new File(localFileDirectory, "My Angles");
	}
	
	/* The method onPictureTaken creates a filename for the raw photo,
	 *  and saves it to disk. 
	 */
	public void onPictureTaken(byte[] data, Camera camera) {
		
		File pictureFileDirectory = getDir();
		
		if (pictureFileDirectory.exists() == false &&
			pictureFileDirectory.mkdirs() == false) {
			
			Log.d(OngoingEventActivity.DEBUG_TAG, "Can't create a new directory to save an image.");
			
			Toast.makeText(context, "Can't create a new directory to save an image.", Toast.LENGTH_LONG).show();
			
			return;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		
		String date = dateFormat.format(new Date());
		
		String photoFilename = "Picture_" + date + ".jpg";
		
		String filename = pictureFileDirectory.getPath() + File.separator + photoFilename;
		
		File photo = new File(filename);
		
		try {
			
			FileOutputStream fos = new FileOutputStream(photo);
			
			fos.write(data);
			
			fos.close();
			
			Toast.makeText(context, "Image Saved: " + photoFilename, Toast.LENGTH_LONG).show();
			
		} catch (Exception error) {
			
			Log.d(OngoingEventActivity.DEBUG_TAG, "File " + filename + " not saved: " + error.getMessage());
			
			Toast.makeText(context, "File " + filename + " not saved.", Toast.LENGTH_LONG).show();
		}
	}
}
