Java Bootstrap
===============

#### IntelliJ Save Action Plugin
We use the IntelliJ Save Action plugin to format the code on save, organize imports
and methods.

Follow theres instructions to install it:
1. Go to File > Settings > Plugins.
2. Search for plugin “Save Actions” and click install.
3. Search for “Save Actions” in the panel Settings and configure it as follows:

![save actions](https://image.ibb.co/jxeOCf/save-actions.png)


https://plugins.jetbrains.com/plugin/7642-save-actions

#### IntelliJ Google xml Code Style

We follow the Google Java code style and the best way to adhere it is
using the IntelliJ automatic formatter.
To accomplish this we need to configure the code style in IntelliJ using
the google xml code style configuration file.

You can find the xml file here
https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml

And follow this instructions to import it:
1. Download the .xml.
2. Go to File > Settings > Editor > Code style.
3. Import the .xml:

![code style](https://image.ibb.co/jRB9k0/code-style.png)

4. Go to the Java section and change the indent size to 4.

#### Database and Hibernate
Change the database name, user and password in the properties file for your own


They are located in the application.properties file
```
spring.datasource.url=jdbc:postgresql://localhost:5432/DATABASENAME
spring.datasource.username=DBUSERNAME
spring.datasource.password=DBPASSWORD
```