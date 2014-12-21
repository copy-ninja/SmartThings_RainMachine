/**
 *	RainMachine Device Type
 *
 *	Author: Jason Mok
 *	Date: 2014-12-20
 *
 ***************************
 *
 *  Copyright 2014 Jason Mok
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 **************************
 *
 * REQUIREMENTS:
 * Refer to RainMachine Service Manager SmartApp
 *
 **************************
 * 
 * USAGE:
 * Put this in Device Type. Don't install until you have all other device types scripts added
 * Refer to RainMachine Service Manager SmartApp
 *
 */
metadata {
	definition (name: "RainMachine", author: "Jason Mok", namespace: "copy-ninja") {
		capability "Polling"
        capability "Refresh"
        capability "Valve"        

        command "start"
        command "stop"
	}

	tiles {    
		standardTile("sprinkler", "device.contact", width: 1, height: 1, canChangeIcon: false) {
			state "close", label: "inactive", icon: "st.Outdoor.outdoor12", action: "contact.open",  backgroundColor: "#ffffff"
			state "open",  label: "active",   icon: "st.Outdoor.outdoor12", action: "contact.close", backgroundColor: "#79b821"
		}        
        
        standardTile("refresh", "device.contact", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
                
        main(["sprinkler"])
        details(["sprinkler", "refresh"])
    }

}

def refresh() {
  parent.refresh()
  poll()
}
 
def poll() {
    def deviceStatus = parent.getDeviceStatus(this)
    if (deviceStatus == "inactive") {
        sendEvent(name: "contact", value: "close", display: true, descriptionText: "RainMachine is inactive")
    }
    if (deviceStatus == "active") {
        sendEvent(name: "contact", value: "open", display: true, descriptionText: "RainMachine is active")
    }     
}

def open()  { start() }
def close() { stop() }
def start() { if(parent.sendCommand(this, "start", 300, "")) { poll() } }
def stop()  { if(parent.sendCommand(this, "stop", 300, ""))  { poll() } }
