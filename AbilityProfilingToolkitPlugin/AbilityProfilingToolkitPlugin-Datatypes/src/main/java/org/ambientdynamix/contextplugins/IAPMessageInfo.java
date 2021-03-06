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

import org.ambientdynamix.api.application.IContextInfo;

/**
 * Represents battery status information
 *
 * @author Darren Carlson
 *
 */
public interface IAPMessageInfo extends IContextInfo {

    /**
     * Integer field containing the current battery level, from 0 to Scale (obtained from getScale()).
     *
     * @return battery level.
     */
    public String getMessage();

    public void setMessage(String level);

    public String[] getResultArray();

    public int getMessageType();

}