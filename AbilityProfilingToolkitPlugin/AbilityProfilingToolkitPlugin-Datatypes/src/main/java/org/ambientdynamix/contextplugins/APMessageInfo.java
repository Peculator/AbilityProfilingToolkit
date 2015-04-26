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

import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

class APMessageInfo implements IAPMessageInfo {
    /**
     * Required CREATOR field that generates instances of this Parcelable class from a Parcel.
     *
     * @see http://developer.android.com/reference/android/os/Parcelable.Creator.html
     */
    public static Parcelable.Creator<APMessageInfo> CREATOR = new Parcelable.Creator<APMessageInfo>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public APMessageInfo createFromParcel(Parcel in) {
            return new APMessageInfo(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public APMessageInfo[] newArray(int size) {
            return new APMessageInfo[size];
        }
    };
    // Public static variable for our supported context message
    public static String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.apmessage";

    // Private data
    private int messageType;

    private String message;



    private String[] resultArray;


    public String[] getResultArray() {
        return resultArray;
    }

    public void setResultArray(String[] resultArray) {
        this.resultArray = resultArray;
    }

    /**
     * Returns the message of the context information represented by the IContextInfo. This string must match one of the
     * supported context information message strings described by the source ContextPlugin.
     */
    @Override
    public String getContextType() {
        return CONTEXT_TYPE;
    }

    /**
     * Returns the fully qualified class-name of the class implementing the IContextInfo interface. This allows Dynamix
     * applications to dynamically cast IContextInfo objects to their original message using reflection. A Java
     * "instanceof" compare can also be used for this purpose.
     */
    @Override
    public String getImplementingClassname() {
        return this.getClass().getName();
    }

    /**
     * Returns a Set of supported string-based context representation format types or null if no representation formats
     * are supported. Examples formats could include MIME, Dublin Core, RDF, etc. See the plug-in documentation for
     * supported representation types.
     */
    @Override
    public Set<String> getStringRepresentationFormats() {
        Set<String> formats = new HashSet<String>();
        formats.add("text/plain");
        return formats;
    }

    /**
     * Returns a string-based representation of the IContextInfo for the specified format string (e.g.
     * "application/json") or null if the requested format is not supported.
     */
    @Override
    public String getStringRepresentation(String format) {
        if (format.equalsIgnoreCase("text/plain"))
            return "Type: " + message;
        else
            // Format not supported, so return an empty string
            return "";
    }

    /**
     * Create a MyBatteryLevelInfo
     *
     * @param type
     *            The device's detected battery level as a percentage of 100.
     */

    public APMessageInfo(int messageType, String message, String[] requestResults) {

        setMessage(message);
        setResultArray(requestResults);
        setMessageType(messageType);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String myMessage) {
        this.message = myMessage;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    };

    /**
     * Used by Parcelable when sending (serializing) data over IPC.
     */
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(messageType);
        out.writeString(message);

        out.writeStringArray(resultArray);

    }

    /**
     * Used by the Parcelable.Creator when reconstructing (deserializing) data sent over IPC.
     */
    private APMessageInfo(final Parcel in) {

        this.messageType = in.readInt();
        this.message = in.readString();
        this.resultArray = in.createStringArray();

    }

    /**
     * Default implementation that returns 0.
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }
}