package com.angles.angles;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LoginActivity extends AccountAuthenticatorActivity {

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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		/* If the user created a new account successfully */
		
		if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
			
			finishLogin(data);
		}
		else
			super.onActivityResult(requestCode, resultCode, data);
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
