package com.angles.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

public class ConfirmPictureDialog extends DialogFragment {
	
	private static final String DEBUG_TAG = "ConfirmPictureDialog";
	
	public ConfirmPictureDialog() {
		
		/* Empty constructor required for DialogFragment */
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
			
		return new AlertDialog.Builder(getActivity())
		            .setCancelable(false)
					.setMessage("Send this Angle?")
					.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							/* TODO: Return to OngoingEventActivity 
							 * to send the photo
							 */
						
							Log.d(DEBUG_TAG, "Send Picture");
							
							dialog.dismiss();
						}
					})
					.setNeutralButton("No, Take Another Picture", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							/* TODO: re-launch the camera */
							
							Log.d(DEBUG_TAG, "Cancel Picture, Take Another");
							
							dialog.dismiss();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 
							/* Return to OngoingEventActivity, but not
							 * to send the photo
							 */
							
							Log.d(DEBUG_TAG, "Cancel Picture");
							
							dialog.dismiss();
						}
					})
					.create();
	}
	
	
}
