package id.ac.amikom.amikomsocial;

import id.ac.amikom.amikomsocial.libs.DbHelper;
import id.ac.amikom.amikomsocial.libs.Login;
import id.ac.amikom.amikomsocial.libs.ServiceHelper;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btnLogin;
	private EditText txtId, txtPwd;
	private String id, pwd;
	private TextView viewId, viewName, viewSts, viewLog;
	private ImageView viewImg;

	public class LoginTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);		

		protected void onPreExecute() {
			dialog.setMessage("Login as " + id + "..");
			dialog.show();
		}

		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (result == true) {
				Toast.makeText(LoginActivity.this, "Login Success",
						Toast.LENGTH_LONG).show();								
				finish();

			} else {
				Toast.makeText(LoginActivity.this, "Login Failed",
						Toast.LENGTH_LONG).show();
			}

		}

		protected Boolean doInBackground(String... params) {
			ServiceHelper srv = new ServiceHelper();

			if (srv.login(LoginActivity.this, id, pwd)) {
				return true;
			} else {
				return false;
			}

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DbHelper db = new DbHelper(this);
		if (db.isLogin()) {
			setContentView(R.layout.activity_profile);
			viewId = (TextView) findViewById(R.id.viewId);
			viewName = (TextView) findViewById(R.id.viewName);
			viewSts = (TextView) findViewById(R.id.viewSts);
			viewLog = (TextView) findViewById(R.id.viewLog);
			viewImg = (ImageView) findViewById(R.id.viewImg);
			
			Login login = db.getLogin();
			
			String identity = "";
			if (login.get_is_mhs() == 1)
				identity = "Amikom Student";
			else if (login.get_is_mhs() == 2)
				identity = "Amikom Lecturer";
			else
				identity = "Amikom Alumni";
			
			viewId.setText("Id User. "+login.get_usr());
			viewName.setText("Name User. "+login.get_name());
			viewSts.setText("Status User. "+identity);
			viewLog.setText("Login Date. "+login.get_logdate());
			
			String imgPath = "/mnt/sdcard/amikom/amikomuser";
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bmp = BitmapFactory.decodeFile(imgPath, options);
			viewImg.setImageBitmap(bmp);
						

		} else {
			setContentView(R.layout.activity_login);
			btnLogin = (Button) findViewById(R.id.btnLog);
			txtId = (EditText) findViewById(R.id.inputId);
			txtPwd = (EditText) findViewById(R.id.inputPwd);

			btnLogin.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					id = txtId.getText().toString();
					pwd = txtPwd.getText().toString();

					new LoginTask().execute();

				}

			});
		}

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_post);
		actionBar.setTitle(R.string.app_login_title);

		actionBar.setHomeAction(new IntentAction(this, MainActivity
				.createIntent(this), R.drawable.ic_action_back));

	}

}
