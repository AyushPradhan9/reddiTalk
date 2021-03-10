# reddiTalk
reddiTalk is a Group chatting application with similarities to Reddit's community structure which means it is open to all discussion application with support of chatting with individual users as well. reddiTalk is built with support of Java frameworks and MySQL database to store user data and messages. It was built with an idea of being a discussion forum for sharing views, exploring ideas and answering questions about a topic. For example topics related to technology, classwork, work questions or real life incidents, possibilities are immense!

## Getting Started
reddiTalk is divided into 2 different categories, one being the server interface containing API calls for the client and database for storing various information collected from the client and the other part being the client user interface which will be offered the functionalities in much user-friendly way. reddiTalk requires the following 3 easy steps to set up:
1) Setting up of the database which will act as a server by executing [databaseSetup](https://github.com/AyushPradhan9/reddiTalk/blob/main/server/databaseSetup.java) file from server folder which will make database structure as follows:

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/database.png)

2) Executing the [serverMain](https://github.com/AyushPradhan9/reddiTalk/blob/main/server/serverMain.java) file from the server folder which will act as the access point for API calls to get the functionalities for the user/client. Keep the serverMain file running in the background to get requests from the user.
3) Finally all the backend part has been set up and ready, it is now time to run the [LoginGUI](https://github.com/AyushPradhan9/reddiTalk/blob/main/client/LoginGUI.java) file from client folder to chat with friends and share ideas to the masses!

## Functionalities
- Sign-up using a unique username and password.

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/SignupGUI.jpg)
- Login to your account

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/LoginGUI.jpg)
- Change password of your account

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/SettingGUI.jpg)
- See online users on the application
- Join a particular topic

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/MainPanelGUI.jpg)
- Message online users

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/DirectMsgGUI.jpg)
- Message in the joined topic
- Leave the joined topic

![](https://raw.githubusercontent.com/AyushPradhan9/reddiTalk/main/img/GroupMsgGUI.jpg)

## Prerequisites
Compilation of the project requires the standard version of java run-time environment pre-installed with an Eclipse IDE (recommended) and MySQL plugins.

## Built with
Eclipse IDE
