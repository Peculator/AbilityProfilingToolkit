/*
 * Copyright (C) The Ambient Dynamix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ambientdynamix.contextplugins;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import org.ambientdynamix.api.contextplugin.ContextListenerInformation;
import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.ContextPluginSettings;
import org.ambientdynamix.api.contextplugin.PowerScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.itm.stage.abilityprofiling.DB.APParameters;
import de.itm.stage.abilityprofiling.DB.APResponse;
import de.itm.stage.abilityprofiling.DB.Ability;
import de.itm.stage.abilityprofiling.DB.DataBaseRequests;
import de.itm.stage.abilityprofiling.DB.DataSource;
import de.itm.stage.abilityprofiling.DB.Permission;
import de.itm.stage.abilityprofiling.DB.Profile;
import de.itm.stage.abilityprofiling.DB.ResponseMessages;
import de.itm.stage.abilityprofiling.DB.ResponseType;
import de.itm.stage.abilityprofiling.DB.Validator;

/**
 * The AbilityProfilingPlug-in is a plug-in to manage detected ICF-Codes
 *
 * @author Sven Kamieniorz
 */
public class AbilityProfilingToolkitPluginRuntime extends ContextPluginRuntime {
    // Static logging TAG
    private final String TAG = this.getClass().getSimpleName();
    // Our secure context
    private Context context;
    private DataSource datasource;

    /**
     * Called once when the ContextPluginRuntime is first initialized. The implementing subclass should acquire the
     * resources necessary to run. If initialization is unsuccessful, the plug-ins should throw an exception and release
     * any acquired resources.
     */
    @Override
    public void init(PowerScheme powerScheme, ContextPluginSettings settings) throws Exception {
        // Set the power scheme
        this.setPowerScheme(powerScheme);
        // Store our secure context
        this.context = this.getSecuredContext();

        datasource = new DataSource(this.context);
        datasource.open();
    }

    /**
     * Called by the Dynamix Context Manager to start (or prepare to start) context sensing or acting operations.
     */
    @Override
    public void start() {
        Log.d(TAG, "Started!");
        if (datasource.isReady()) {
            sendBroadcastContextEvent(new APMessageInfo(ResponseMessages.TYPE_NONE, ResponseMessages.DATABASE_READY, null));
        }

    }

    /**
     * Called by the Dynamix Context Manager to stop context sensing or acting operations; however, any acquired
     * resources should be maintained, since start may be called again.
     */
    @Override
    public void stop() {
        Log.d(TAG, "Stopped!");
    }

    /**
     * Stops the runtime (if necessary) and then releases all acquired resources in preparation for garbage collection.
     * Once this method has been called, it may not be re-started and will be reclaimed by garbage collection sometime
     * in the indefinite future.
     */
    @Override
    public void destroy() {
        this.stop();
        context = null;
        datasource.close();
        Log.d(TAG, "Destroyed!");
    }

    @Override
    public void handleContextRequest(UUID requestId, String contextType) {
        sendExceptionMessage(requestId, ResponseMessages.EXCEPTION_WRONG_FORMAT);
    }

    @Override
    public void handleConfiguredContextRequest(UUID requestId, String contextType, Bundle config) {
        // Warn that we don't handle configured requests
        Log.w(TAG, "handleConfiguredContextRequest called, but we don't support configuration!");
        // Drop the config and default to handleContextRequest
        //handleContextRequest(requestId, contextType);

        if (contextType.equalsIgnoreCase(APMessageInfo.CONTEXT_TYPE)) {

            if (datasource.isReady()) {


                APResponse response = handleRequest(requestId, APParameters.fromBundle(config));
                if (response != null) {
                    sendContextEvent(requestId, new APMessageInfo(response.getType(), response.getMessage(), response.getResponseArray()));
                } else {
                    sendExceptionMessage(requestId, ResponseMessages.EXCEPTION_WRONG_FORMAT);
                }

            } else {
                sendExceptionMessage(requestId, ResponseMessages.EXCEPTION_DB_NOT_READY);
            }

        }
    }

    public void sendExceptionMessage(UUID requestID, String message) {
        sendContextEvent(requestID, new APMessageInfo(ResponseMessages.TYPE_EXCEPTION, message, null));
    }

    public APResponse handleRequest(UUID requestID, APParameters request) {

        if (request != null) {
            if (Validator.validate(request)) {
                if (hasPermission(request)) {

                    APResponse response = new APResponse();
                    Log.i(TAG, "REQUEST_ID:" + request.getFunctionID());

                    //----------------------GET_ALL_ABILITIES--------------
                    if (request.getFunctionID() == DataBaseRequests.GET_ALL_ABILITIES) {
                        String[] result = toStringArray(datasource.getAllAbilities().toArray(new Ability[0]));
                        response.setType(ResponseMessages.TYPE_ABILITY);


                        if (result.length > 0) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;

                    }
                    //----------------------Delete Profile--------------
                    else if (request.getFunctionID() == DataBaseRequests.DELETE_PROFILE) {
                        datasource.deleteProfile(request.getProfile());
                        response.setType(ResponseMessages.TYPE_NONE);

                        response.setMessage(ResponseMessages.SUCCESS);

                        response.setResponseArray(null);

                        return response;

                    }
                    //----------------------GET_ALL_TABLES--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_ALL_TABLES) {

                        ResponseType[] result = datasource.getAllTables().toArray(new ResponseType[0]);

                        ArrayList<String> ab = new ArrayList<String>();
                        ArrayList<String> pr = new ArrayList<String>();
                        ArrayList<String> pe = new ArrayList<String>();

                        for (int i = 0; i < result.length; i++) {
                            if (result[i] instanceof Ability) {
                                ab.add(((Ability) result[i]).toString());
                            } else if (result[i] instanceof Profile) {
                                pr.add(((Profile) result[i]).toString());
                            } else if (result[i] instanceof Permission) {
                                pe.add(((Permission) result[i]).toString());
                            }
                        }
                        String[] resultA = ab.toArray(new String[ab.size()]);
                        String[] resultB = pr.toArray(new String[pr.size()]);
                        String[] resultC = pe.toArray(new String[pe.size()]);


                        String[] resultSend = new String[3 + resultA.length + resultB.length + resultC.length];
                        resultSend[0] = String.valueOf(resultA.length);
                        resultSend[1] = String.valueOf(resultB.length);
                        resultSend[2] = String.valueOf(resultC.length);

                        for (int i = 0; i < resultA.length; i++) {
                            resultSend[3 + i] = resultA[i];
                        }
                        for (int i = 0; i < resultB.length; i++) {
                            resultSend[3 + i + resultA.length] = resultB[i];
                        }
                        for (int i = 0; i < resultC.length; i++) {
                            resultSend[3 + i + resultA.length + resultB.length] = resultC[i];
                        }

                        response.setType(ResponseMessages.TYPE_ALL);


                        if (resultSend.length > 3) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(resultSend);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;

                    }
                    //----------------------RESET DATABASE--------------
                    else if (request.getFunctionID() == DataBaseRequests.RESET_DATABASE) {
                        datasource.resetDataBase();

                        response.setType(ResponseMessages.TYPE_NONE);
                        response.setMessage(ResponseMessages.SUCCESS);
                        response.setResponseArray(null);

                        return response;

                    }
                    //----------------------GET_ALL_PROFILES--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_ALL_PROFILES) {
                        String[] result = toStringArray(datasource.getAllProfiles().toArray(new Profile[0]));
                        response.setType(ResponseMessages.TYPE_PROFILE);

                        if (result.length > 0) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }
                    //----------------------GET_ALL_PERMISSIONS--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_ALL_PERMISSIONS) {
                        String[] result = toStringArray(datasource.getAllPermissions().toArray(new Permission[0]));
                        response.setType(ResponseMessages.TYPE_PERMISSION);

                        if (result.length > 0) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }
                    //----------------------CREATE_PROFILE--------------
                    else if (request.getFunctionID() == DataBaseRequests.CREATE_PROFILE) {
                        response.setType(ResponseMessages.TYPE_NONE);

                        datasource.createProfile(request.getProfile());

                        //First Report - Add Permission if not set yet
                        if (datasource.getPermissionByAppname(request.getAppname()) == null) {
                            datasource.createPermission(new Permission(request.getAppname(), Permission.PermissionStates.YES.toString()));
                        }

                        response.setMessage(ResponseMessages.SUCCESS);
                        response.setResponseArray(null);

                        return response;
                    }

                    //----------------------CREATE_PERMISSION --------------
                    else if (request.getFunctionID() == DataBaseRequests.CREATE_PERMISSION) {
                        response.setType(ResponseMessages.TYPE_NONE);

                        if(datasource.getPermissionByAppname(request.getAppname())!=null){
                            datasource.deletePermission(request.getPermission());
                        }

                        datasource.createPermission(request.getPermission());


                        response.setMessage(ResponseMessages.SUCCESS);
                        response.setResponseArray(null);

                        return response;
                    }

                    //----------------------GET_ABILITY_BY_CODE--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_ABILITY_BY_CODE) {
                        String[] result = toStringArray(new Ability[]{datasource.getAbilityByCode(request.getICFcode())});
                        response.setType(ResponseMessages.TYPE_ABILITY);

                        if (result != null) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }

                    //----------------------GET_PROFILES_BY_CODE--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_PROFILES_BY_CODE) {
                        String[] result;
                        response.setType(ResponseMessages.TYPE_PROFILE);
                        // All parameters
                        if (request.getDepth() != -1 && request.getStartTime() != -1
                                && request.getEndTime() != -1) {
                            result = toStringArray(datasource.getProfilesByCode(request.getICFcode(), request.getDepth(), request.getStartTime(), request.getEndTime()).toArray(new Profile[0]));
                        }
                        // No endtime
                        else if (request.getDepth() != -1 && request.getStartTime() != -1) {
                            result = toStringArray(datasource.getProfilesByCode(request.getICFcode(), request.getDepth(), request.getStartTime()).toArray(new Profile[0]));
                        }
                        // No endtime/starttime
                        else if (request.getDepth() != -1) {
                            result = toStringArray(datasource.getProfilesByCode(request.getICFcode(), request.getDepth()).toArray(new Profile[0]));
                        }
                        // No endtime/starttime or depthtalerance valuse
                        else {
                            result = toStringArray(datasource.getProfilesByCode(request.getICFcode()).toArray(new Profile[0]));
                        }

                        if (result.length > 0) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }

                    //----------------------GET_RELATED_PROFILES_BY_CODE--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_RELATED_PROFILES_BY_CODE) {
                        String[] result = toStringArray(datasource.getRelatedProfilesByCode(request.getICFcode(), request.getDepth(), request.getRefTime(), request.getTimeDistance()).toArray(new Profile[0]));
                        response.setType(ResponseMessages.TYPE_PROFILE);
                        if (result != null) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }
                    //----------------------GET_RELATED_PROFILES_BY_PROFILE--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_RELATED_PROFILES_BY_PROFILE) {
                        String[] result = toStringArray(datasource.getRelatedProfilesByProfile(request.getProfile(), request.getTimeDistance()).toArray(new Profile[0]));
                        response.setType(ResponseMessages.TYPE_PROFILE);
                        if (result != null) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    }
                    //----------------------GET_PERMISSION_BY_APPNAME--------------
                    else if (request.getFunctionID() == DataBaseRequests.GET_PERMISSION_BY_APPNAME) {
                        String[] result = toStringArray(new Permission[]{datasource.getPermissionByAppname(request.getAppname())});
                        response.setType(ResponseMessages.TYPE_PERMISSION);
                        if (result != null) {
                            response.setMessage(ResponseMessages.SUCCESS);
                            response.setResponseArray(result);
                        } else {
                            response.setMessage(ResponseMessages.NO_RESULT);
                            response.setResponseArray(null);
                        }
                        return response;
                    } else if (request.getFunctionID() == DataBaseRequests.GET_USER_ID) {
                        response.setType(ResponseMessages.TYPE_NONE);
                        response.setMessage(ResponseMessages.USER_ID + Settings.Secure.getString(context.getContentResolver(),
                                Settings.Secure.ANDROID_ID));
                        response.setResponseArray(null);
                        return response;

                    } else {
                        sendExceptionMessage(requestID, ResponseMessages.EXCEPTION_WRONG_FORMAT);
                        return null;
                    }

                }
            }else{
                sendExceptionMessage(requestID, ResponseMessages.EXCEPTION_NO_PERMISSION);
            }
        } else {
            sendExceptionMessage(requestID, ResponseMessages.EXCEPTION_WRONG_FORMAT);
        }

        return null;
    }

    private String[] toStringArray(Ability[] abilities) {
        String[] result = new String[abilities.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = abilities[i].toString();
        }
        return result;
    }

    private String[] toStringArray(Profile[] profiles) {
        String[] result = new String[profiles.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = profiles[i].toString();
        }
        return result;
    }

    private String[] toStringArray(Permission[] permissions) {
        String[] result = new String[permissions.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = permissions[i].toString();
        }
        return result;
    }

    private boolean hasPermission(APParameters request) {
        if (request.getAppname().equals("AbilityProfilingToolkitManager"))
            return true;

        List<Permission> allP = datasource.getAllPermissions();

        for (Permission p : allP){
            if(p.getAppName().equals(request.getAppname()) && p.getPermissionState()!= Permission.PermissionStates.NO.toString())
                return true;
        }

        //Adding a Permission
        Permission p = new Permission(request.getAppname(), Permission.PermissionStates.YES.toString());
        datasource.createPermission(p);

        Log.i(TAG, "Added Permission");

        return false;
    }

    @Override
    public void updateSettings(ContextPluginSettings settings) {
        // Not supported
    }

    @Override
    public void setPowerScheme(PowerScheme scheme) {
        // Not supported
    }


    @Override
    public boolean addContextlistener(ContextListenerInformation listenerInfo) {
        return true;
    }

}