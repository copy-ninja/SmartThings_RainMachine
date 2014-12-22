/**
 *	RainMachine Smart Device
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
	definition (name: "RainMachine", namespace: "copy-ninja", author: "Jason Mok") {
		capability "Valve"
		capability "Refresh"
        capability "Polling"
        
        attribute "runTime", "number"
        
        command "stopAll"
        command "setRunTime"
	}

	simulator { }

	tiles {
		standardTile("contact", "device.contact", width: 2, height: 2, canChangeIcon: true) {
			state "closed", label: 'inactive', action: "valve.open", icon: "st.Outdoor.outdoor12", backgroundColor: "#ffffff"
            state "open", label: 'active', action: "valve.close", icon: "st.Outdoor.outdoor12", backgroundColor: "#1e9cbb" 			
            state "opening", label: 'pending', action: "valve.close", icon: "st.Outdoor.outdoor12", backgroundColor: "#D4741A" 	
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        standardTile("stopAll", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'Stop All', action:"stopAll", icon:"st.secondary.off"
		}
        controlTile("runTimeControl", "device.runTime", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setRunTime", action:"setRunTime", backgroundColor: "#1e9cbb"
        }
        valueTile("runTime", "device.runTime", inactiveLabel: false, decoration: "flat") {
			state "runTimeValue", label:'${currentValue} mins', backgroundColor:"#ffffff"
		}

		main "contact"
		details(["contact","refresh","stopAll","runTimeControl","runTime"])
	}
}

def installed() {
	runTime = 5
}

def parse(String description) {}

def open()  { 
	def runTime = device.currentValue("runTime")
	def runTimeInSecs = runTime * 60
    parent.sendCommand(this, "start", runTimeInSecs, "") 
    poll()  
}
def close() { 
	def runTime = device.currentValue("runTime")
	def runTimeInSecs = runTime * 60
    parent.sendCommand(this, "stop",  runTimeInSecs, "") 
    poll()  
}

def refresh() {
    parent.refresh()
    poll()
}

def poll() {
    def deviceStatus = parent.getDeviceStatus(this)
    if (deviceStatus == "inactive") {
        sendEvent(name: "contact", value: "closed", display: true, descriptionText: device.displayName + " was inactive")
    }
    if (deviceStatus == "active") {
        sendEvent(name: "contact", value: "open", display: true, descriptionText: device.displayName + " was active")
    }   
    if (deviceStatus == "pending") {
        sendEvent(name: "contact", value: "opening", display: true, descriptionText: device.displayName + " was pending")
    }  
}

def stopAll() {
	parent.sendStopAll()
    poll()
}

void setRunTime(runTimeSecs) {
	sendEvent("name":"runTime", "value": runTimeSecs)
}
