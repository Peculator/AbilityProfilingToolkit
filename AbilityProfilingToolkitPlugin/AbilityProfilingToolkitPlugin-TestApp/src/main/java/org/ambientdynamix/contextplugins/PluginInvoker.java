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
import de.itm.stage.abilityprofiling.DB.Permission;
import de.itm.stage.abilityprofiling.DB.Profile;
import de.itm.stage.abilityprofiling.DB.ResponseMessages;
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

    }


    public List<PluginInvocation> getPluginInvocations() {
        return pluginInvocations;
    }


    /**
     *
     * @param pluginId    The plugin to call
     * @param contextType  The contextType  to request
     * @param configuration  optional configuration bundle, can be null
     */
    public void addPluginInvocation(String pluginId, String contextType, Bundle configuration) {
        pluginInvocations.add(new PluginInvocation(pluginId,contextType,configuration));
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