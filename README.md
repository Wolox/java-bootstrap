Java Bootstrap
===============

## First steps

#### Installing Java

Get the latest version of Java following the guide in the [official website]().

### [Kickoff] Application Setup

After cloning the bootstrap, follow the [kickoff guide](https://github.com/Wolox/tech-guides/blob/master/java/docs/kickoff/README.md#kickoff).
And happy coding!

#### Installing Java
1. Install the Java Development Kit (JDK), available in
   https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Set Java Home as an environment variable in your computer. To do so, you must follow these steps:

   a. Launch the Terminal and type in sudo su.

   b. Open the bashsrc file for edition by typing gedit .bashrc

   c. Add the following lines at the end of the file:

      JAVA_HOME=/usr/lib/jvm/default-java/bin

      export JAVA_HOME

      PATH=$PATH:$JAVA_HOME

      export PATH

   d. Close and reopen the terminal and type echo $JAVA_HOME, and verify that it has been set properly.

3. Verify proper installation by typing java -version in a new terminal.

#### Add the Lombok IntelliJ plugin:

We use [Lombok](https://www.baeldung.com/intro-to-project-lombok) for DTOs and logs.

It provides annotations to easily add getters/setters/constructors/logs to your code.

You can always override the methods that Lombok generates if any additional logic were required. 

1. Go to File > Settings > Plugins.
2. Click on Browse repositories...
3. Search for Lombok Plugin.
4. Click on Install plugin.
5. Restart IntelliJ IDEA.

Then you need to go to Preferences > Build, Execution, Deployment > Compiler > Annotation Processors and make sure of the following:
* "Annotation Processing" box is checked.
* "Obtain processors from project classpath" option is selected.

#### IntelliJ Save Action Plugin
We use the IntelliJ Save Action plugin to format the code on save, organize imports
and methods.

Follow these instructions to install it:
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


#### SonarLint installation in IntelliJ
A linter is a tool that can be used to analyze source code and flag errors, bugs, style errors, etc.
SonarLint is a plugin for IntelliJ that exists with this purpose. In order to configure it, follow
these instructions:
1. Go to File > Settings > Plugins.
2. Search for plugin "Sonarlint" and select "Browse repositories". The following screen will appear:

![sonarlint plugin](https://image.ibb.co/gvDxsf/sonarlint1.png)


The tool will be automatically configured in IntelliJ. When writing code, some problematic sections
will be highlighted and the tool will provide suggestions to fix the potential bugs.

![sonarlint highlight](https://image.ibb.co/bFriXf/sonarlint2.png)


The tool can be customized by changing highlight colors if desired. To do this go to
File > Settings SonarLint:

![sonarlint colors](https://image.ibb.co/b5O8yL/sonarlint3.png)


#### Database and Hibernate
Change the database name, user and password in the properties file for your own


They are located in the application.properties file
```
spring.datasource.url=jdbc:postgresql://localhost:5432/DATABASENAME
spring.datasource.username=DBUSERNAME
spring.datasource.password=DBPASSWORD
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## About

This project is maintained by [Luciano Zemin](https://github.com/zeminlu) and [Lucas Mujica](https://github.com/Mujics) along with [Jimena Roselló](https://github.com/Jimenarosello) and [Nicolas Gianni](https://github.com/GNico95) and it was written by [Wolox](http://www.wolox.com.ar).

![Wolox](https://raw.githubusercontent.com/Wolox/press-kit/master/logos/logo_banner.png)

## License

**java-bootstrap** is available under the MIT [license](LICENSE.md).

    Copyright (c) 2017 Wolox

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
