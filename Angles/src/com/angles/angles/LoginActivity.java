package com.angles.angles;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.appengine.api.datastore.Hash;
import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.DBTableConstants;

/**
 * Activity that loads the login screen. Also contains the business logic for creating
 * new users
 * @author Mike
 *
 */
public class LoginActivity extends CloudBackendActivity {
	private final int REQ_SIGNUP = 1;
	private final String DEBUG_TAG = "LoginActivity";
	private AccountManager accountManager;
	private String authTokenType;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		/*Instantiate Layout and Managers */
    	 super.onCreate(savedInstanceState);
    	 AnglesController.getInstance().getDisplayManager().displayLogin(this);
    	 AnglesController.getInstance().getTouchManager().setLoginPageListeners(this);
    	 
    	/*Instantiate Account Authentication */
    	 accountManager = AccountManager.get(getBaseContext());
    	 String accountName = getIntent().getStringExtra("ACCOUNT_NAME");
    	 authTokenType = getIntent().getStringExtra("AUTH_TYPE");
    	 
    	 if (authTokenType == null) {
    		 authTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
    	 }
    }
	
	public void createNewUser(View view){
		EditText UserName = (EditText) findViewById(R.id.signupUserName);
		EditText Email = (EditText) findViewById(R.id.signupEmail);
		EditText PW = (EditText) findViewById(R.id.signupPassword);
		
		CloudEntity createNewUser = new CloudEntity(DBTableConstants.DB_USERS_USERSTABLENAME);
		createNewUser.put(DBTableConstants.DB_USERS_PASSWORD,Hash.md5( PW.getText().toString()));
		createNewUser.put(DBTableConstants.DB_USERS_EMAIL, Email.getText().toString().toLowerCase());
		createNewUser.put(DBTableConstants.DB_USERS_USERNAME,UserName.getText().toString());
		
		 CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
		      @Override
		      public void onComplete(final CloudEntity result) {}

		      @Override
		      public void onError(final IOException exception) {
		        handleEndpointException(exception);
		      }
		};

	    // execute the insertion with the handler
		getCloudBackend().insert(createNewUser, handler);
	}
 
	private void handleEndpointException(IOException e) {
	    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	}
		
		
	
	public void submit() {
		final String accountType = getIntent().getStringExtra("ACCOUNT_TYPE");
		
		new AsyncTask<String, Void, Intent>() {
			protected Intent doInBackground(String... params) {
				Log.d(DEBUG_TAG, "Authentication Attempted");
				Bundle data = new Bundle();
				
				/* Try Catch Block that submits user/pass to datastore
				 * 
				 */
				
				final Intent res = new Intent();
				res.putExtras(data);
				
				return res;
			}
			
			protected void onPostExecute(Intent intent) {
				if (intent.hasExtra("ERR_MSG")) {
					Toast.makeText(getBaseContext(), intent.getStringExtra("ERR_MSG"), Toast.LENGTH_LONG).show();
				}
				else
					finishLogin(intent);
			}
		}.execute();
	}
	
	private void finishLogin(Intent intent) {
		Log.d(DEBUG_TAG, "Authentication finishing.");
		
		/* Getting account name and password */
		String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
		String accountPassword = intent.getStringExtra("USER_PASS");
		
		/* Instantiating an Account */
		final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
		
		if (getIntent().getBooleanExtra("IS_ADDING_NEW_ACCOUNT", false)) {
			/* If the user created a new account, this will register it with the OS */
			Log.d(DEBUG_TAG, "Adding an account explicitly");
			
			String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
			String authtokenType = authTokenType;
			
			accountManager.addAccountExplicitly(account, accountPassword, intent.getBundleExtra(AccountManager.KEY_USERDATA));
			accountManager.setAuthToken(account, authtokenType, authtoken);
		}
		else {
			Log.d(DEBUG_TAG, "Setting the password");
			accountManager.setPassword(account, accountPassword);
		}
	}
}
