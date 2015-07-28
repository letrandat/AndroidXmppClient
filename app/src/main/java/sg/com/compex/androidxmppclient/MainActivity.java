package sg.com.compex.androidxmppclient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    protected static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new XMPPLogin().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class XMPPLogin extends AsyncTask<Void, Void, Void> {

        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Your domain or IP address
            final String DOMAIN="example.com";
            final int PORT=5222;
            // Your host name
            final String HOST="localhost";

            // Create the configuration for this new connection
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setUsernameAndPassword("username", "password");
            configBuilder.setResource("smack-client");
            configBuilder.setServiceName(HOST);
            configBuilder.setHost(DOMAIN);
            configBuilder.setPort(PORT);
            configBuilder.setDebuggerEnabled(true);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
            // Connect to the server
            try {
                Log.d(TAG, "connect");
                connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            }
            // Log into the server
            try {
                Log.d(TAG, "login");
                connection.login();
            } catch (XMPPException | SmackException | IOException e) {
                e.printStackTrace();
            }

            // Disconnect from the server
            Log.d(TAG, "disconnect");
            connection.disconnect();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            pdLoading.dismiss();
        }
    }
}
