/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package net.erdfelt.android.sdkfido.local;

import net.erdfelt.android.sdkfido.FetchException;

public class AndroidPlatformNotFoundException extends FetchException {
    private static final long serialVersionUID = 7075630317209742177L;

    public AndroidPlatformNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AndroidPlatformNotFoundException(String message) {
        super(message);
    }
}
