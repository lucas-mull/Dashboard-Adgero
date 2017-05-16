# Dashboard-Adgero
A Dashboard for a truck KERS by the Adgero Company.

<h1>What is this ?</h1>

![App screenshot](/Screenshots/simulation-2.png)

This app is a dashboard for a KERS used in trucks by the Adgero company.
In its current state, this is simply a straight-up user interface for display realtime data to the user. A simulation mode allows to test the UI in action.

The connexion between the KERS system and the app remains to be done. Although discovering devices with Bluetooth and peers with WiFi Direct is possible already, no connexion can be established yet.

You can see screenshots of this app in the [Screenshots](/Screenshots) folder. The interface shoud be responsive for smartphones AND tablets.

<h1>How do I use this app ?</h1>

Again, in its current state, the only thing you can do is discover devices and visualize a simulation of the UI in action.
* To discover devices, simply click on the desired method on the welcome screen.
![Welcome screen](/Screenshots/welcome_screen.png)
* To view the interface in action, press the simulation button at the start. Then, you will see two buttons at the bottom right of the screen. One is used to simulate an acceleration (upward arrow), the other to simulate braking (downard arrow).
![Simulation screen](/Screenshots/simulation-1.png)

<h1>How does it work ?</h1>

The data that will be used to update the UI is stored in a Singleton [Data Singleton](/app/src/main/java/com/ensiie/adgerodashboard/DataSingleton.java)

From there, a background Thread located in [the MainActivity](/app/src/main/java/com/ensiie/adgerodashboard/Activities/MainActivity.java) will periodically update the UI with the Singleton's data. The period can be sped up or slowed down to your liking. Simply change the value of REFRESH_RATE_MILLIS to whatever value suits you. Keep in mind that the lower the value, the lower the performance.

<h1>How can I make it work ?</h1>

Once you are decided with the communication method you want to use, here what remains to be done and how to do it :
* Establish a connexion between the communication card and the app. For this, you can check the Android [Bluetooth API](https://developer.android.com/guide/topics/connectivity/bluetooth.html) or [Direct Wifi API](https://developer.android.com/guide/topics/connectivity/wifip2p.html) depending on what you will be using.
* Receive data from the connexion. This is also explained at the above locations.
* Parse the data into a [JSON Object](https://developer.android.com/reference/org/json/JSONObject.html). This should be pretty straight forward.
* If you followed the previous steps, you should have a background Thread that reads input from the incoming connexion into a JSONObject. From there, simply call the DataSingleton method 
```java
public void setData(JSONObject data);
```

This will update all the Singleton values. Please make sure all the JSON keys are correct otherwise this method will obviously fail. Their values are stored in variables at the beginning of the Singleton class. 

The code should be sufficiently commented, but if you are struggling with anything, please feel free to contact me. I will do my best to try and help you.

<h1>Credits</h1>

This project was a group project made by Mebarek Agag, Mounir Charhrouchni, Sophie Ledos and Lucas Mull.
Thank you to Mack Murray, the president of Adgero, and Frederic Soullier, our tutor, for their precious help throughout the project.
