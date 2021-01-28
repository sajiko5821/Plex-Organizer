/**
 *  Plex Organizer
 *
 *  Copyright 2021 Lukas Weier
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
    name: "Plex Organizer",
    namespace: "sajiko",
    author: "Lukas Weier",
    description: "The Plex Organizer",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    page(name: "mainPage", title: "Configure Everything", install: true, uninstall: true,submitOnChange: true) {
        section("Select Plex States") {
		    input "plexPlay", "capability.switch", required: false, title: "Play", multiple: false
            input "plexPause", "capability.switch", required: false, title: "Pause", multiple: false
            input "plexStop", "capability.switch", required: false, title: "Stop", multiple: false
	    }
    
		section("Play Scene"){
        	input "dimmersPlay", "capability.switchLevel", title: "Turn on these lights", multiple: true, required: false
           	input name: "iLevelOnPlay", type: "number", title: "Level on Play", defaultValue:0, required: false
            input "offLightsPlay", "capability.switch", title: "Turn off these lights", multiple: true, required: false
        }
        
        section("Pause Scene"){
        	input "dimmersPause", "capability.switchLevel", title: "Turn on these lights", multiple: true, required: false
           	input name: "iLevelOnPause", type: "number", title: "Level on Pause", defaultValue:0, required: false
            input "offLightsPause", "capability.switch", title: "Turn off these lights", multiple: true, required: false
        }
        
        section("Stop Scene"){
        	input "dimmersStop", "capability.switchLevel", title: "Turn on these lights", multiple: true, required: false
           	input name: "iLevelOnStop", type: "number", title: "Level on Stop", defaultValue:0, required: false
            input "offLightsStop", "capability.switch", title: "Turn off these lights", multiple: true, required: false
        }
        
        section("Execute between"){
            input "fromTime", "time", title: "From"
            input "toTime", "time", title: "To"
        }
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
	
  	subscribe(plexPlay, "switch.on", onActive)
    subscribe(plexPause, "switch.on", onPause)
    subscribe(plexStop, "switch.on", onStop)
    log.debug "Subscribed to input switches"
}

def onActive(evt){
	log.debug "onActive executed"
    def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
    if(between){
    	log.debug "Time correct"
        if(iLevelOnPlay){
            dimmersPlay.setLevel(iLevelOnPlay)
        }
        offLightsPlay.off()
    }
    plexPlay.off()
}

def onPause(evt){
	log.debug "onPause executed"
    def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
    if(between){
    	log.debug "Time correct"
    	if(iLevelOnPause){
			dimmersPause.setLevel(iLevelOnPause)
    	}
    	offLightsPause.off()
   	}
    plexPause.off()
}

def onStop(evt){
	log.debug "onStop executed"
    def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
    if(between){
    	log.debug "Time correct"
    	if(iLevelOnStop){
			dimmersStop.setLevel(iLevelOnStop)
    	}
    	offLightsStop.off()
   	}
    plexStop.off()
}