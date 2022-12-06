# RioDB-Headless-Client
A headless client for [RioDB](https://github.com/RioDB/riodb).

## Installation
1. Make sure to have [RioDB](https://github.com/RioDB/riodb) properly setup.
2. Install/Clone this repository
3. Done!

## Start the client
1. Start your RioDB instance (for example via the .bat file)
2. Compile the client (`javac -d . RiodbClient.java PostHTTP.java PostHTTPS.java`)
3. Run the compiled code (`java client.RiodbClient`)
4. Enter the setup settings
   1. Enter the selected host
   2. If the host is HTTPS then you have to enter your username and password for your RioDB instance.
5. Finished! Use the client in order to communicate with the RioDB instance. You can enter `system status;` to see if it works.

## Commands
* `clear` // clears the terminal
* `exit;` // exit the terminal
* `.` // reset your statement
* `;` // end your statement

## Having trouble?
If you have trouble connecting to the client, check your localhost and the settings of your internet network (for example:
 can I ping?). If this doesn't work or if you have any other question, please message [northern-64bit](https://github.com/northern-64bit).
