package org.ambientdynamix.contextplugins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ambientdynamix.api.application.Callback;
import org.ambientdynamix.api.application.ContextHandler;
import org.ambientdynamix.api.application.ContextHandlerCallback;
import org.ambientdynamix.api.application.ContextListener;
import org.ambientdynamix.api.application.ContextPluginInformation;
import org.ambientdynamix.api.application.ContextPluginInformationResult;
import org.ambientdynamix.api.application.ContextResult;
import org.ambientdynamix.api.application.DynamixConnector;
import org.ambientdynamix.api.application.DynamixFacade;
import org.ambientdynamix.api.application.IContextInfo;
import org.ambientdynamix.api.application.IContextRequestCallback;
import org.ambientdynamix.api.application.ISessionListener;
import org.ambientdynamix.api.application.SessionCallback;
import org.ambientdynamix.api.application.SessionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.itm.stage.abilityprofiling.DB.APParameters;
import de.itm.stage.abilityprofiling.DB.Ability;
import de.itm.stage.abilityprofiling.DB.DataBaseRequests;
import de.itm.stage.abilityprofiling.DB.Permission;
import de.itm.stage.abilityprofiling.DB.Profile;
import de.itm.stage.abilityprofiling.DB.ResponseMessages;
import de.itm.stage.abilityprofiling.DB.Validator;

/**
 * Created by Peculator
 */
public class BindDynamixActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private DynamixFacade dynamix;
    public PluginInvoker pluginInvoker;
    private ContextHandler contextHandler;
    public final boolean DEBUG = true;

    public static TextView messagesAdd;
    public static TextView messagesRemove;
    public static TextView messagesChange;

    public static TextView dynamixStatus;

    public static LinkedList<Profile> messageListAdd = new LinkedList<Profile>();
    public static LinkedList<Permission> messageListChange = new LinkedList<Permission>();
    public static LinkedList<Profile> messageListRemove = new LinkedList<Profile>();


    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the
     *                           data it most recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pluginInvoker = new PluginInvoker();

        setContentView(R.layout.main);
        messagesAdd = (TextView) findViewById(R.id.messagesAdd);
        messagesRemove = (TextView) findViewById(R.id.messagesRemove);
        messagesChange = (TextView) findViewById(R.id.messagesChange);

        dynamixStatus = (TextView) findViewById(R.id.dynamix_status);


        Button btnShowICF = (Button) findViewById(R.id.showICFbtn);
        btnShowICF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startICF();
            }
        });

        Button btnShowProfiles = (Button) findViewById(R.id.showProfilebtn);
        btnShowProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfiles();
            }
        });

        Button btnShowPermissions = (Button) findViewById(R.id.showPermissionsbtn);
        btnShowPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPermissions();
            }
        });


        disableButtons();

        connect();

    }

    public void enableButtons(){
        findViewById(R.id.showICFbtn).setEnabled(true);
        findViewById(R.id.showProfilebtn).setEnabled(true);
        findViewById(R.id.showPermissionsbtn).setEnabled(true);
    }

    public void disableButtons(){
        findViewById(R.id.showICFbtn).setEnabled(false);
        findViewById(R.id.showProfilebtn).setEnabled(false);
        findViewById(R.id.showPermissionsbtn).setEnabled(false);
    }

    private void fillDataBase(){
        APParameters params = new APParameters();
        params.setAppname(getString(R.string.app_name));
        params.setFunctionID(DataBaseRequests.GET_ALL_TABLES);

        if(Validator.validate(params)){
            pluginInvoker.addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
        }
        else{
            Log.i(TAG, "VALIDATION FAILDED");
        }
        invokePlugins();
    }

    private void updateMessageBoard() {
        LinkedList <Permission> newPerList = new LinkedList<Permission>();
        for (int i = 0; i < messageListChange.size(); i++) {
                if(!newPerList.contains(messageListChange.get(i))){
                    newPerList.add(messageListChange.get(i));
                }
        }
        messageListChange = newPerList;

        messagesAdd.setText("Added: "+messageListAdd.toString());
        messagesRemove.setText("Removed: "+messageListRemove.toString());
        messagesChange.setText("Changed: "+messageListChange.toString());
    }

    private void clearLists(){
        messageListAdd.clear();
        messageListRemove.clear();
        messageListChange.clear();
        updateMessageBoard();
    }

    private void synchronize() {
        //pluginInvoker.getPluginInvocations().clear();
        if(messageListAdd.size()>0) {
            for (int i = 0; i < messageListAdd.size(); i++) {
                APParameters params = new APParameters();
                params.setAppname(getString(R.string.app_name));
                params.setFunctionID(DataBaseRequests.CREATE_PROFILE);
                params.setProfile(messageListAdd.get(i));
                if(Validator.validate(params))
                    pluginInvoker.addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
                else{
                    Log.i("my","not validated");
                }
            }

            invokePlugins();
        }

        if(messageListRemove.size()>0){
            for (int i = 0; i < messageListRemove.size(); i++) {
                APParameters params = new APParameters();
                params.setAppname(getString(R.string.app_name));
                params.setFunctionID(DataBaseRequests.DELETE_PROFILE);
                params.setProfile(messageListRemove.get(i));
                if(Validator.validate(params))
                    pluginInvoker.addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
                else{
                    Log.i("my","not validated");
                }
            }

            invokePlugins();
        }

        if(messageListChange.size()>0){
            for (int i = 0; i < messageListChange.size(); i++) {
                APParameters params = new APParameters();
                params.setAppname(getString(R.string.app_name));
                params.setFunctionID(DataBaseRequests.CREATE_PERMISSION);
                params.setPermission(messageListChange.get(i));
                if(Validator.validate(params))
                    pluginInvoker.addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
                else{
                    Log.i("my","not validated");
                }
            }

            invokePlugins();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateMessageBoard();
        synchronize();
    }

    private void invokePlugins() {

        if (contextHandler != null) {
            enableButtons();
            try {
                Log.i(TAG, "A1 - Requesting Programmatic Context Acquisitions:" + pluginInvoker.getPluginInvocations().size());
                for (PluginInvoker.PluginInvocation pluginInvocation : pluginInvoker.getPluginInvocations()) {
                    if (pluginInvocation.getConfiguration() != null) {
                        Log.i(TAG, "mit config");
                        contextHandler.contextRequest(pluginInvocation.getPluginId(),
                                pluginInvocation.getContextRequestType(), pluginInvocation.getConfiguration(), new IContextRequestCallback.Stub() {
                                    @Override
                                    public void onSuccess(ContextResult contextEvent) throws RemoteException {
                                        Log.i(TAG, "A1app - Request id was: " + contextEvent.getResponseId());
                                        contextListener.onContextResult(contextEvent);
                                    }

                                    @Override
                                    public void onFailure(String s, int i) throws RemoteException {
                                        Log.w(TAG, "Call was unsuccessful! Message: " + s + " | Error code: "
                                                + i);
                                        contextListener.onContextResult(null);
                                    }

                                });

                    } else {
                        contextHandler.contextRequest(pluginInvocation.getPluginId(),
                                pluginInvocation.getContextRequestType(), new IContextRequestCallback.Stub() {
                                    @Override
                                    public void onSuccess(ContextResult contextEvent) throws RemoteException {
                                        Log.i(TAG, "A1p - Request id was: " + contextEvent.getResponseId());
                                        contextListener.onContextResult(contextEvent);
                                    }

                                    @Override
                                    public void onFailure(String s, int i) throws RemoteException {
                                        Log.w(TAG, "Call was unsuccessful! Message: " + s + " | Error code: "
                                                + i);
                                        contextListener.onContextResult(null);
                                    }
                                });
                    }

                }
                pluginInvoker.getPluginInvocations().clear();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Dynamix not connected.");
            dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Dynamix not connected");
            disableButtons();
        }
    }

    private void disconnect() {
        initializing = false;
        if (contextHandler != null) {
            try {
                /*
                 * In this example, this Activity controls the session, so we call closeSession here. This will
                 * close the session for ALL of the application's IDynamixListeners.
                 */
                dynamix.closeSession(new Callback() {
                    @Override
                    public void onFailure(String message, int errorCode) throws RemoteException {
                        Log.w(TAG, "Call was unsuccessful! Message: " + message + " | Error code: "
                                + errorCode);
                        dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Error closing session");
                    }

                    @Override
                    public void onSuccess() throws RemoteException {
                        Log.w(TAG, "Session closed");
                        dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Session closed");
                    }
                });
                contextHandler.removeAllContextSupport();
                dynamix = null;
                contextHandler = null;


            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void connect() {

        if (contextHandler == null) {
            try {
                DynamixConnector.openConnection(BindDynamixActivity.this, true, sessionListener, new SessionCallback.Stub() {
                    @Override
                    public void onSuccess(DynamixFacade iDynamixFacade) throws RemoteException {
                        dynamix = iDynamixFacade;
                        Log.i(TAG, "openConnection.onSuccess with " + iDynamixFacade);
                        dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Connecting...");

                        dynamix.createContextHandler(new ContextHandlerCallback.Stub() {

                            @Override
                            public void onSuccess(ContextHandler handler) throws RemoteException {
                                contextHandler = handler;
                                pluginInvoker = new PluginInvoker();


                                init(pluginInvoker, new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Connecting: Success");
                                enableButtons();
                            }

                            @Override
                            public void onFailure(String message, int errorCode) throws RemoteException {
                                Log.e(TAG, message);
                                dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Connecting: Error\n"+message);
                                disableButtons();
                            }
                        });

                    }

                    @Override
                    public void onFailure(String s, int i) throws RemoteException {
                        dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Connecting: Failed");
                        disableButtons();
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Error: Remote Exception");
                disableButtons();
            }
            dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Connected");
            enableButtons();
        } else
            Log.i(TAG, "Contexthandler is null");
        dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Contexthandler is null" );
        disableButtons();

    }


    /*
     * Example listener that receives important ongoing information about our session with Dynamix.
	 */
    private ISessionListener sessionListener = new SessionListener() {
        @Override
        public void onSessionOpened(String sessionId) throws RemoteException {
            Log.i(TAG, "onSessionOpened");
        }

        @Override
        public void onSessionClosed() throws RemoteException {
            Log.i(TAG, "onSessionClosed");
            dynamix = null;
            contextHandler = null;
        }

        @Override
        public void onContextPluginDisabled(org.ambientdynamix.api.application.ContextPluginInformation plug)
                throws RemoteException {
            Log.i(TAG, "onContextPluginDisabled: " + plug);
        }

        @Override
        public void onContextPluginEnabled(org.ambientdynamix.api.application.ContextPluginInformation plug)
                throws RemoteException {
            Log.i(TAG, "onContextPluginEnabled: " + plug);
        }
        // Other events omitted for brevity. See JavaDocs for details.
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            APParameters params = new APParameters();
            params.setAppname(getString(R.string.app_name));
            params.setFunctionID(DataBaseRequests.RESET_DATABASE);
            if(Validator.validate(params)) {
                pluginInvoker.addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
                BindDynamixActivity.profiles = null;
                BindDynamixActivity.permissions = null;
            }
            else{
                Log.i("my", "not validated");
            }
            invokePlugins();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startPermissions() {
        Intent i = new Intent(this, PermissionsActivity.class);
        startActivity(i);
        clearLists();
    }

    private void startProfiles() {
        Intent i = new Intent(this, ProfilesActivity.class);
        startActivity(i);
        clearLists();
    }

    private void startICF() {
        Intent i = new Intent(this, ICFActivity.class);
        startActivity(i);
        clearLists();
    }


    public static Ability[] abilities;
    public static Profile[] profiles;
    public static Permission[] permissions;

    private ContextListener contextListener = new ContextListener() {

        @Override
        public void onContextResult(ContextResult event) throws RemoteException {

            if(event==null){
                dynamixStatus.setText(getResources().getString(R.string.dynamix_status) + " Error!!!");
                return;
            }

            // Check for native IContextInfo
            if (event.hasIContextInfo()) {
                IContextInfo nativeInfo = event.getIContextInfo();


                if (nativeInfo instanceof IAPMessageInfo) {
                    IAPMessageInfo info = (IAPMessageInfo) nativeInfo;

                    if (info.getMessageType() == ResponseMessages.TYPE_ALL && info.getMessage().equals(ResponseMessages.SUCCESS)) {
                        String[] result = info.getResultArray();
                        abilities = new Ability[Integer.parseInt(result[0])];
                        profiles = new Profile[Integer.parseInt(result[1])];
                        permissions = new Permission[Integer.parseInt(result[2])];

                        for (int i = 0; i < abilities.length; i++) {
                            abilities[i] = Ability.toAbility(result[i+3]);
                        }
                        for (int i = 0; i < profiles.length; i++) {
                            profiles[i] = Profile.toProfile(result[i + 3 + abilities.length]);
                        }
                        for (int i = 0; i < permissions.length; i++) {
                            permissions[i] = Permission.toPermission(result[i + 3 + abilities.length + profiles.length]);
                        }

                        Log.i(TAG, "ALL added: " + abilities.length + "-"+ profiles.length + "-"+ permissions.length);

                    }else if (info.getMessageType() == ResponseMessages.TYPE_ABILITY && info.getMessage().equals(ResponseMessages.SUCCESS)) {
                        abilities = new Ability[info.getResultArray().length];
                        for (int i = 0; i < info.getResultArray().length; i++) {
                            abilities[i] = Ability.toAbility(info.getResultArray()[i]);
                        }

                        Log.i(TAG, "Abilities added");

                    } else if (info.getMessageType() == ResponseMessages.TYPE_PROFILE && info.getMessage().equals(ResponseMessages.SUCCESS)) {
                        profiles = new Profile[info.getResultArray().length];
                        for (int i = 0; i < info.getResultArray().length; i++) {
                            profiles[i] = Profile.toProfile(info.getResultArray()[i]);
                        }

                        Log.i(TAG, "Profiles added");

                    } else if (info.getMessageType() == ResponseMessages.TYPE_PERMISSION && info.getMessage().equals(ResponseMessages.SUCCESS)) {
                        permissions = new Permission[info.getResultArray().length];
                        for (int i = 0; i < info.getResultArray().length; i++) {
                            permissions[i] = Permission.toPermission(info.getResultArray()[i]);
                        }
                        Log.i(TAG, "Permissions added");

                    } else if (info.getMessageType() == ResponseMessages.TYPE_EXCEPTION) {
                        Log.i(TAG, "Exception: " + info.getMessage());

                    } else if (info.getMessageType() == ResponseMessages.TYPE_NONE) {
                        Log.i(TAG, "No type: " + info.getMessage());

                    } else if (info.getMessage().equals(ResponseMessages.NO_RESULT)) {
                        Log.i(TAG, info.getMessage() + " - " + info.getMessageType());
                    }else{
                        Log.i(TAG, info.getMessage() + " ??? " + info.getMessageType());
                    }
                }
                // Check for other interesting types, if needed...
            } else
                Log.i(TAG,
                        "Plug: Event does NOT contain native IContextInfo... we need to rely on the string representation!");
        }
    };


    private List<ContextPluginInformation> uninstallingPlugins = new ArrayList<ContextPluginInformation>();

    private boolean initializing;

    /**
     * Threaded initialization that automatically uninstalls the plug-ins targeted by the pluginInvoker, waiting for
     * Dynamix events as needed.
     *
     * @param pluginInvoker The PluginInvoker of target plug-ins
     * @param callback      An (optional) callback to run after initialization
     * @throws RemoteException
     */
    private synchronized void init(final PluginInvoker pluginInvoker, final Runnable callback) throws RemoteException {
        if (DEBUG) {
            // Check for init state
            if (!initializing) {
                Log.i(TAG, "Init running");
                initializing = true;
                // Create a list of targetPluginIds
                List<String> targetPluginIds = new ArrayList<String>();
                for (PluginInvoker.PluginInvocation invocation : pluginInvoker.getPluginInvocations())
                    targetPluginIds.add(invocation.getPluginId());
                // Clear the uninstall list
                uninstallingPlugins.clear();
                // Access the list of installed Dynamix plug-ins
                ContextPluginInformationResult result = dynamix.getAllContextPluginInformation();
                if (result.wasSuccessful()) {
                    // Add the ContextPluginInformation for each target plug-in to the uninstallingPlugins list
                    for (ContextPluginInformation plug : result.getContextPluginInformation()) {

                        for (String id : targetPluginIds) {
                            if (plug.getPluginId().equalsIgnoreCase(id))
                                uninstallingPlugins.add(plug);
                        }
                    }
                    if (!uninstallingPlugins.isEmpty()) {
                        final int[] count = new int[1];
                        final int total = uninstallingPlugins.size();
                        for (final ContextPluginInformation info : uninstallingPlugins) {
                            Log.i(TAG, "Calling uninstall for plug " + info);
                            try {
                                dynamix.requestContextPluginUninstall(info, new Callback() {
                                    @Override
                                    public void onSuccess() throws RemoteException {
                                        Log.i(TAG, "Uninstalled: " + info.getPluginId());
                                        count[0]++;
                                        if (count[0] == total) {
                                            Log.i(TAG, "Waiting for uninstall to complete... FINISHED!");
                                            // Set init to false, since we're finished
                                            initializing = false;
                                            // Fire the callback, if necessary (i.e, if it's not null)
                                            if (callback != null)
                                                callback.run();
                                        }
                                    }

                                    @Override
                                    public void onFailure(String message, int errorCode) throws RemoteException {
                                        Log.w(TAG, "Uninstalling: " + info.getPluginId() + " failed, reason: " + message + " moving on as if uninstall succeeded");
                                        count[0]++;
                                        if (count[0] == total) {
                                            Log.i(TAG, "Waiting for uninstall to complete... FINISHED!");
                                            // Set init to false, since we're finished
                                            initializing = false;
                                            // Fire the callback, if necessary (i.e, if it's not null)
                                            if (callback != null)
                                                callback.run();
                                        }
                                    }
                                });
                            } catch (RemoteException e) {
                                Log.w(TAG, e);
                            }
                        }
                        uninstallingPlugins.clear();

                    } else {
                        Log.i(TAG, "No plug-ins to uninstall!");
                        // Set init to false, since we're finished
                        initializing = false;
                        // Fire the callback, if necessary (i.e, if it's not null)
                        if (callback != null)
                            callback.run();
                    }
                } else
                    Log.w(TAG, "Could not access plug-in information from Dynamix");
            } else
                Log.w(TAG, "Already initializing, please wait...");
        }
        fillDataBase();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "ON DESTROY for: Dynamix Simple Logger (A1)");
        /*
         * Always remove our listener and unbind so we don't leak our service connection
		 */
        disconnect();
        if (contextHandler != null) {
            try {
                dynamix.closeSession();
                contextHandler = null;
            } catch (RemoteException e) {
            }
        }
        super.onDestroy();
    }

}
