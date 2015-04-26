package org.ambientdynamix.contextplugins;

import android.os.Bundle;
import android.util.Log;

import org.ambientdynamix.api.application.ContextResult;
import org.ambientdynamix.api.application.IContextInfo;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import de.itm.stage.abilityprofiling.DB.APParameters;
import de.itm.stage.abilityprofiling.DB.DataBaseRequests;
import de.itm.stage.abilityprofiling.DB.Profile;
import de.itm.stage.abilityprofiling.DB.Validator;
import de.itm.stage.abilityprofiling.icf.ICFClassifications;
import de.itm.stage.abilityprofiling.icf.ICFRating;


/**
 * Created by workshop on 11/6/13.
 */
public class PluginInvoker {


    private List<PluginInvocation> pluginInvocations;

    /**
     * if this is set to true the BindDynamixActivity will not require any user interaction to run
     */
    public static final boolean AUTOMATIC_EXECUTION = false;

    private String TAG = this.getClass().getSimpleName();

    /**
     * constructor of the PluginInvoker. Add plugin Invocations here
     */
    public PluginInvoker() {
        pluginInvocations = new Vector<PluginInvocation>();

//        Add plugin invocations by calling addPluginInvocation(pluginId,context type, configuration)
        //addPluginInvocation("org.ambientdynamix.contextplugins","org.ambientdynamix.contextplugins.apmessage",null);
        // Requesting ability information
        APParameters params = new APParameters();params.setAppname(TAG);
        params.setFunctionID(DataBaseRequests.CREATE_PROFILE);
        Profile p = new Profile();
        p.setAppName(TAG);
        p.setICFCode("b 230");
        p.setTimeStamp(new Date().getTime());
        p.setRating(ICFRating.SEVERE_IMPAIRMENT[1]);
        params.setProfile(p);
        if(Validator.validate(params)){
            addPluginInvocation("org.ambientdynamix.contextplugins", "org.ambientdynamix.contextplugins.apmessage", params.toBundle());
        }
        else{
            Log.i(TAG, "VALIDATION A FAILDED");
        }

    }

    public List<PluginInvocation> getPluginInvocations() {
        return pluginInvocations;
    }

    /**
     * This gets called when the DynamixBindActivity receives a context event of a type specified in the cotextRequestType array
     *
     * @param event
     */
    public void invokeOnResponse(ContextResult event) {

        // Check for native IContextInfo
        if (event.hasIContextInfo()) {
            Log.i(TAG, "A1 - Event contains native IContextInfo: " + event.getIContextInfo());
            IContextInfo nativeInfo = event.getIContextInfo();
                /*
                 * Note: At this point you can cast the IContextInfo into it's native type and then call its methods. In
				 * order for this to work, you'll need to have the proper Java classes for the IContextInfo data types
				 * on your app's classpath. If you don't, event.hasIContextInfo() will return false and
				 * event.getIContextInfo() would return null, meaning that you'll need to rely on the string
				 * representation of the context info. To use native context data-types, simply download the data-types
				 * JAR for the Context Plug-in you're interested in, include the JAR(s) on your build path, and you'll
				 * have access to native context type objects instead of strings.
				 */
            Log.i(TAG, "A1 - IContextInfo implementation class: " + nativeInfo.getImplementingClassname());
            // Example of using IPedometerStepInfo

            if (nativeInfo instanceof IMyBatteryLevelInfo) {
                IMyBatteryLevelInfo info = (IMyBatteryLevelInfo) nativeInfo;
                Log.i(TAG, "Battery level: " + info.getBatteryLevel());
            }
            if (nativeInfo instanceof IAPMessageInfo) {
                IAPMessageInfo info = (IAPMessageInfo) nativeInfo;
                String[] resultArray = info.getResultArray();
                String message = info.getMessage();
                int messageType = info.getMessageType(); // ResponseMessages.TYPE_NONE = 5;
                int result = (resultArray == null) ? messageType : resultArray.length;
                Log.i(TAG, result + " - " + message);
            }

            // Check for other interesting types, if needed...
        } else
            Log.i(TAG,
                    "Event does NOT contain native IContextInfo... we need to rely on the string representation!");
    }

    /**
     * @param pluginId      The plugin to call
     * @param contextType   The contextType  to request
     * @param configuration optional configuration bundle, can be null
     */
    private void addPluginInvocation(String pluginId, String contextType, Bundle configuration) {
        pluginInvocations.add(new PluginInvocation(pluginId, contextType, configuration));
    }

    public class PluginInvocation {
        private String pluginId;
        private String contextRequestType;
        private Bundle configuration;
        private boolean successfullyCalled;

        public PluginInvocation(String pluginId, String contextRequestType, Bundle configuration) {
            this.pluginId = pluginId;
            this.contextRequestType = contextRequestType;
            this.configuration = configuration;
        }

        public String getPluginId() {
            return pluginId;
        }

        public void setPluginId(String pluginId) {
            this.pluginId = pluginId;
        }

        public String getContextRequestType() {
            return contextRequestType;
        }

        public void setContextRequestType(String contextRequestType) {
            this.contextRequestType = contextRequestType;
        }

        public Bundle getConfiguration() {
            return configuration;
        }

        public void setConfiguration(Bundle configuration) {
            this.configuration = configuration;
        }

        public boolean isSuccessfullyCalled() {
            return successfullyCalled;
        }

        public void setSuccessfullyCalled(boolean successfullyCalled) {
            this.successfullyCalled = successfullyCalled;
        }
    }

}