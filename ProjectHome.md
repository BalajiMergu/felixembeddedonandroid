I currently do my diploma thesis about Android and dynamic Software, so I was testing around with the nice tutorial on:

http://ipojo-dark-side.blogspot.com/2008/10/ipojo-on-android.html

Unfortunatly it wasn't compatible with Android 1.5r2 and Apache Felix 1.9.0snapshot.

After some work I got it fixed. Thanks goes to:

Clement Escoffier (provided the tutorial above, made the basic patch for felix 1.9.0 to let it run on real devices using new public Methods in Android 1.5)

Karl Pauls and Richard S. Hall for patching Felix 1.9.0 which fixed an classloading problem and for supporting me in Apache Felix users mailing list.

btw: If someone of you don't want that this project is hosted here in public, please contact me an I will delete it immediately.


To start projects: checkout with svn client, and import created Folder as an new Eclipse Project. Then create proper run/debug configuration. See wiki for all other questions.


Also visit my blog: http://www.matthias-neubert.de


Buzzwords:
Android
Apache Felix
Felix
OSGi
OSGi on Android
Felix on Android
Felix Android
Apache Felix on Android
Felix OSGi Android