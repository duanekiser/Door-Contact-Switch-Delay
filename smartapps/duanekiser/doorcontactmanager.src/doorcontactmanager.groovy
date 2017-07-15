/**
 *  KeenManager
 *
 *  Copyright 2017 Duane Kiser
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
 */
definition(
    name: "DoorContactManager",
    namespace: "duanekiser",
    author: "Duane Kiser",
    description: "Trips Door Sensor",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("When Door Sensor") {
        input "mytrip", "capability.contactSensor", required: true, title: "Which?"
    }

	section("Sensor Status") {
        input name: "mystatus", type: "enum", title: "Open/Closed?", options: ["open", "closed"], description: "Enter enum", required: true
    }

	section("Then Turn ON Switch") {
        input "myswitch", "capability.switch", required: true, title: "Which?"
    }
    
    section("Delay Switch X Minutes then OFF") {
        input name: "myminutes", type: "number", required: true, title: "Minutes?"
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(mytrip, "contactSensor.open", openContactHandler)
    subscribe(mytrip, "contactSensor.closed", closedContactHandler)
}


def openContactHandler(evt) {
	log.debug "Contact Value : $mytrip"
	if (mytrip == "open") {
	        log.debug "door status open switch turned on!"
	        myswitch.on()
	        def mydelay = 1000 * 60 * myminutes
	        log.debug "waiting on delay: ${mydelay}"
	        myswitch.off(delay: mydelay)       
	    } else {
	        log.debug "door status closed switch turned off!"
	        myswitch.off()
	    }     
	} 
    
def closedContactHandler(evt) {
	if (mytrip == "closed") {
	        log.debug "door status closed switch turned on!"
	        myswitch.on()
	        def mydelay = 1000 * 60 * myminutes
	        log.debug "waiting on delay: ${mydelay}"
	        myswitch.off(delay: mydelay)
	    } else {
	        log.debug "door status open switch turned off!"
	        myswitch.off()
	      }    
    }
