SmartThings_RainMachine
=======================

This is in use with SmartThings(www.smartthings.com) and will integrate RainMachine(http://www.rainmachine.com)

Requirements:
  * This only works for firmware version 3.63 on RainMachine
  * You know your external IP address
  * You have forwarded port 80 (Currently does not work with SSL 443/18443, this is smartthings limitation). 
  * You must have all scripts installed 
  
Installation:
  * Create a SmartApp and put RainMachine.SmartApp.groovy there.
  * Create a Device Type and put RainMachine.DeviceType.groovy there.
  * Make sure you publish it for yourself by clicking "Publish > For Me"
  * Configure the first page which collects your ip address & port and password to log in to RainMachine
  * For each items you pick on the Programs/Zones page, it will create a device
  * Enjoy!
 
Limitations:
  * SmartThings doesn't have a good way to call the REST services through LAN, at least there's no documentation on how to retrieve replies through SmartHub. RainMachine is purely local on your network. In order to communicate, this has to be exposed through port forwarding
  * SmartThings can't handle self signed SSL for now. RainMachine SSL gives you cert that won't be recognized by any other addresses other than RainMachine's own address.
 
Future Enhancements:
  * Allow Pause, the continue later
  * Allow only to create 1 device
