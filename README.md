# Venuscode README

Venuscode is a little barcode kinda thing i made for fun, If you do install this PLEASE make sure you have java 21+ by doing java -version in your terminal/cmd prompt, You can get the CLI or the GUI, The GUI is easier to set up. You use it like this:

### THIS IS UNTESTED ON WINDOWS AND LINUX! IF YOU WANT TO TEST IT ON THOSE OPERATING SYSTEMS, TRY, I CANNOT GUARANTEE IT WILL WORK

If you want to encode and decode a message in venuscode, you type this into your terminal:

venuscode encode "Message" ~/Desktop/filename.png

venuscode decode ~/Desktop/filename.png

Two examples and an image:

venuscode encode "https://www.wikipedia.org" ~/Desktop/filename.png

<img width="170" height="240" alt="example" src="https://github.com/user-attachments/assets/5d0a2631-e705-4d83-b11d-37b0af7ed797" />

<small>image made with venuscode</small>

venuscode decode ~/Desktop/filename.png

For windows encoding and decoding examples, [Check out this file](EXAMPLEwin.md)

## Again, currently there is only a CLI

# How to install (mac release, for windows check [here](EXAMPLEwin.md))

Firstly download the zip.

Unzip it anywhere you like.

Using terminal enter: cd ~/Desktop/mac.linux.release

Then enter: zsh install.sh

You will get a little "VenusCode installed successfully!" thingamajing.

You can then enter: source ~/.zshrc and you can now encode and decode!

# How to install the GUI

The GUI is universal and is the easiest to install.

Download the .jar from the releases page.

Double click it.

The GUI will open and you can make your stuff!
