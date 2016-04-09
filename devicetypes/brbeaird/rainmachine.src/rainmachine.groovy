/**
 *	RainMachine Smart Device
 *
 *	Author: Jason Mok/Brian Beaird
 *	Date: 2016-04-08
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
	definition (name: "RainMachine", namespace: "brbeaird", author: "Jason Mok/Brian Beaird") {
		capability "Valve"
		capability "Refresh"
		capability "Polling"
	        
		attribute "runTime", "number"        
        attribute "lastRefresh", "string"
        attribute "lastStarted", "string"
	        
		//command "pause"
        //command "resume"
		command "stopAll"
		command "setRunTime"
	}

	simulator { }

	tiles {
		standardTile("contact", "device.contact", width: 2, height: 2, canChangeIcon: true) {
			state("closed",  label: 'inactive', action: "valve.open",  icon: "st.Outdoor.outdoor12", backgroundColor: "#ffffff")
			state("open",    label: 'active',   action: "valve.close", icon: "st.Outdoor.outdoor12", backgroundColor: "#1e9cbb")		
			//state("opening", label: 'pending',  action: "valve.close", icon: "st.Outdoor.outdoor12", backgroundColor: "#D4741A")
            state("opening", label: '${name}',  icon: "st.Outdoor.outdoor12", backgroundColor: "#D4741A")
            state("closing", label: '${name}',  icon: "st.Outdoor.outdoor12", backgroundColor: "#D4741A")
            
            //state("opening", label:'${name}', icon:"st.doors.garage.garage-opening", backgroundColor:"#ffe71e", nextState: "open")
			//state("closing", label:'${name}', icon:"st.doors.garage.garage-closing", backgroundColor:"#ffe71e", nextState: "closed")
            
            
		}
       /* standardTile("pausume", "device.switch", inactiveLabel: false, decoration: "flat") {
			state("resume", label:'resume', action:"pause", icon:"st.sonos.play-icon",  nextState:"pause")
            state("pause",  label:'pause', action:"resume", icon:"st.sonos.pause-icon", nextState:"resume")
            
		} */
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state("default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh")
		}
		standardTile("stopAll", "device.switch", inactiveLabel: false, decoration: "flat") {
			state("default", label:'Stop All', action:"stopAll", icon:"st.secondary.off")
		}
		controlTile("runTimeControl", "device.runTime", "slider", height: 1, width: 2, inactiveLabel: false) {
			state("setRunTime", action:"setRunTime", backgroundColor: "#1e9cbb")
		}
		valueTile("runTime", "device.runTime", inactiveLabel: false, decoration: "flat") {
			state("runTimeValue", label:'${currentValue} mins', backgroundColor:"#ffffff")
		}
        valueTile("lastRefresh", "device.lastRefresh", height: 1, width: 3, inactiveLabel: false, decoration: "flat") {
			state("lastRefreshValue", label:'Last refresh: ${currentValue}', backgroundColor:"#ffffff")
		}

		main "contact"
		details(["contact","refresh","stopAll","runTimeControl","runTime","lastActivity","lastRefresh"])
	}
}

// installation, set default value
def installed() { 
	runTime = 5 
    //poll()
}

//def parse(String description) {}

// turn on sprinkler
def open()  { 
    sendEvent(name: "contact", value: "opening", display: true, displayed: false)   
    parent.sendCommand2(this, "start", (device.currentValue("runTime") * 60))
    //poll()
}
// turn off sprinkler
def close() { 
    sendEvent(name: "contact", value: "closing", display: true, displayed: false)
    parent.sendCommand2(this, "stop",  (device.currentValue("runTime") * 60)) 
    //poll()
}

// refresh status
def refresh() {		
    sendEvent("name":"lastRefresh", "value": "Checking..." , display: false , displayed: false)    
    parent.refresh()
	//poll()
}

//resume sprinkling
def resume() {
    poll()
}

//pause sprinkling
def pause() {
    poll()
}

// update status
def poll() {
	log.info "Polling.."
	//deviceStatus(parent.getDeviceStatus(this))
    //def lastRefresh = parent.getDeviceLastRefresh(this)
    //log.debug "Last refresh: " + lastRefresh
    //sendEvent("name":"lastRefresh", "value": lastRefresh)    
}



// stop everything
def stopAll() {
	sendEvent(name: "contact", value: "closing", display: true, displayed: false)
    parent.sendCommand2(this, "stopall",  (device.currentValue("runTime") * 60))
    
    //parent.sendStopAll()
	//poll()
}

// update the run time for manual zone 
void setRunTime(runTimeSecs) {
	sendEvent("name":"runTime", "value": runTimeSecs)    
}

def updateDeviceLastRefresh(lastRefresh){
    log.debug "Last refresh: " + lastRefresh
    
    def refreshDate = new Date()
    def hour = refreshDate.format("h", location.timeZone)
    def minute =refreshDate.format("m", location.timeZone)
    def ampm =refreshDate.format("a", location.timeZone)
    //def finalString = refreshDate.getDateString() + ' ' + hour + ':' + minute + ampm
    
    def finalString = new Date().format('MM/d/yyyy hh:mm',location.timeZone)
    sendEvent(name: "lastRefresh", value: finalString, display: false , displayed: false)
}

def updateDeviceStatus(status){
	deviceStatus(status)
}

// update status
def deviceStatus(status) {
	log.debug "Old Device Status: " + device.currentValue("contact")
    log.debug "New Device Status: " + status
    
	
    if (status == 0) {	//Device has turned off
		
        //Go ahead and mark the valve as closed
        def oldStatus = device.currentValue("contact")
        sendEvent(name: "contact", value: "closed",  display: true, descriptionText: device.displayName + " was inactive")
        
        //If device has just recently closed, send notification
        if (oldStatus != 'closed' && oldStatus != null){

            //Take note of how long it ran and send notification
            log.debug "lastStarted: " + device.currentValue("lastStarted")
            def lastStarted = device.currentValue("lastStarted")
            def lastActivityValue = "Unknown."
            
            if (lastStarted != null){
            	lastActivityValue = ""
                long lastStartedLong = lastStarted.toLong()
            
                log.debug "lastStarted converted: " + lastStarted
                

                def diffTotal = now() - lastStartedLong
                def diffDays  = (diffTotal / 86400000) as long
                def diffHours = (diffTotal % 86400000 / 3600000) as long
                def diffMins  = (diffTotal % 86400000 % 3600000 / 60000) as long

                if      (diffDays == 1)  lastActivityValue += "${diffDays} Day "
                else if (diffDays > 1)   lastActivityValue += "${diffDays} Days "

                if      (diffHours == 1) lastActivityValue += "${diffHours} Hour "
                else if (diffHours > 1)  lastActivityValue += "${diffHours} Hours "

                if      (diffMins == 1 || diffMins == 0 )  lastActivityValue += "${diffMins} Min"
                else if (diffMins > 1)   lastActivityValue += "${diffMins} Mins"  
            }

            def deviceName = device.displayName
            def message = deviceName + " finished watering. Run time: " + lastActivityValue
            log.debug message

            if (parent.prefSendPush && deviceName.contains("Zone")) {
        		//parent.sendAlert(message)
                parent.sendPushMessage(message)
    		}
            
            
            //sendEvent(name: "pausume", value: "resume")
		}
        
        
	}
	if (status == 1) {	//Device has turned on
		log.debug "Zone turned on!"
        
        //Go ahead and mark the valve as closed
        def oldStatus = device.currentValue("contact")
        sendEvent(name: "contact", value: "open",    display: true, descriptionText: device.displayName + " was active")
        
        //If device has just recently opened, take note of time
        if (oldStatus != 'open' && oldStatus != null){
            //Take note of current time the zone started
            def refreshDate = new Date()
            def hour = refreshDate.format("h", location.timeZone)
            def minute =refreshDate.format("m", location.timeZone)
            def ampm =refreshDate.format("a", location.timeZone)
            def finalString = new Date().format('MM/d/yyyy hh:mm',location.timeZone)
            sendEvent(name: "lastStarted", value: now(), display: false , displayed: false)
            log.debug "stored lastStarted as : " + device.currentValue("lastStarted")
            //sendEvent(name: "pausume", value: "pause")
        }
	}   
	if (status == 2) {  //Device is pending
		sendEvent(name: "contact", value: "opening", display: true, descriptionText: device.displayName + " was pending")
        //sendEvent(name: "pausume", value: "pause")
	}
}
