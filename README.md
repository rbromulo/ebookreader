AuJur E-Book Reader for Android
========================================================

AuJur E-Book Reader is a free, open-source ebook reader. It is licensed under the GPL-V3 license and based on PageTurner.

![AuJur E-Book Reader Sample Screenshot][1]


Building AuJur E-Book Reader
------------------------------

# Install Java
*   On Ubuntu

        sudo apt-get install openjdk-7-jdk
*   On Windows install the JDK from http://www.oracle.com/technetwork/java/javase/downloads/index.html

# Install the Android SDK 

1.   Download at http://developer.android.com/sdk/index.html
2.   Unzip
3.   Update 

        sdk/tools/android update sdk --no-ui

4. On Ubuntu install ia32-libs
        apt-get install ia32-libs

5. Add sdk/tools/ and sdk/platform-tools to your PATH

# Install USB drivers for your device

*   Make sure adb devices shows your device, for example

        $ adb devices
        List of devices attached 
        015d18ad5c14000c        device

NOTE: Currently AuJur E-Book Reader won't build using Maven.

Eclipse
-------

The recommended way to build AuJur E-Book Reader in Eclipse is using the m2e-android plugin.

=======
You can follow these steps to use dependencies (jar files):

1.   Download and unpack the sources        

2.   In Eclipse, select "New Android Project" -> "From existing source" and
     point it to the folder you unpacked AuJur E-Book Reader in.

3.   inside the source folder, there is a folder called "libs". Put all JAR files in the classpath of the project


[1]: https://github.com/rbromulo/aujur/blob/master/assets/screens/home.png