#Pactera Pulse

This folder/repository contains Pactera Pulse, Pactera pulse is an app to record how you feel at any point in time. Your feedback is recorded on a backend server and result of your feelings in last 24 hour in a bar graph. 

##Directory Structure
At the top level we have two directories

- PacteraPulse: Contains the xcode for iOS.
- PacteraPulseTests: Contains unit tests for featues

Inside `PacteraPulse` directory we have following structure

- Helpers: All classes which have common functionality to be ussed accross all classes are placed here.
- Resources: All resources images etc are placed here.
- Others: All files which dont fit in other structure are placed here. Like storyboard, plists etc
- All Features have their own folder group each Feature is divided further into
	- Controller: Controller(Busines logic) classes.
	- Model: Any data model or data related files are here.
	- View: All files related to display are here.

##How to Build The Client

This project uses Cocoa Pods for dependancy management. Switch to the project directory in the terminal (the directory containing `Podfile`) and run:

    pod install

Visit [cocoapods.org](http://cocoapods.org) for more information on cocoapods.

Once you have run **pod install** on terminal you need to open `PacteraPulse.xcworkspace` in XCode to compile and run the iOS Client. 

##Frameworks and libraries used

We have used following frameworks for developing this app

- [AFNetworking](https://github.com/AFNetworking/AFNetworking): For all networking related calls and operations.
- [MBProgressHUD](https://github.com/jdg/MBProgressHUD): For showing progress spinners while doing API calls or network calls.
- [CorePlot](https://github.com/core-plot/core-plot.git): For drawing graphs on summary page.