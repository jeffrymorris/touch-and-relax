package com.example.com.sorc.couchsync_example;

import java.io.IOException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;
import org.ektorp.http.HttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	static {
		TDURLStreamHandlerFactory.registerSelfIgnoreError();
	}
	
	TDServer server = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.e("EXAMPLE", "creating file");
        String filesDir = getFilesDir().getAbsolutePath();
        try{
        	server = new TDServer(filesDir);
        }catch(IOException e){
        	Log.e("TouchDb", "Error starting TDServer", e);
        }
        
        Log.e("EXAMPLE", "making request");
    	HttpClient client = new TouchDBHttpClient(server);
    	CouchDbInstance instance = new StdCouchDbInstance(client);
    	
    	CouchDbConnector connector = instance.createConnector("default", true);
    	
    	Log.e("EXAMPLE", "CREATING REPLICATE COMMAND");
    	ReplicationCommand command = new ReplicationCommand
    			.Builder()
    			//.source("http://couchbase.iriscouch.com/grocery-sync")
    			.source("http://ec2-50-19-168-144.compute-1.amazonaws.com:8091/pools/default/")
		    	.target("default")
		    	.continuous(true)
		    	.build();
    	
    	Log.e("EXAMPLE", "REPLICATING");
    	ReplicationStatus status = instance.replicate(command);
    	Log.e("EXAMPLE", "REPLICATION COMPLETED");
    	Log.e("TuchDb", status.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void createDatabase(){
    	HttpClient client = new TouchDBHttpClient(server);
    	CouchDbInstance instance = new StdCouchDbInstance(client);

    }
}
