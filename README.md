# RioDB-Headless-Client
A headless client for [RioDB](https://github.com/RioDB/riodb).

## Installation
1. Make sure to have RioDB properly setup.
2. Install/Clone this repository
3. Done!

## Start the client
1. Start your RioDB instance (for example via the .bat file)
2. Compile the client (javac -d . Client.java postHTTP.java postHTTPS.java)
3. Run the compiled code (java client.Client)
4. Enter the setup settings
   1. Type of request: HTTP or HTTPS
   2. If HTTPS is selected then enter your username and password for your RioDB instance.
   3. Enter the selected host
5. Finished! Use the client in order to communicate with the RioDB instance. You can enter "system status;" to see if it works.

## Commands
* clear // clears the terminal
* exit; // exit the terminal
* . // reset your statement
* ; // end your statement
