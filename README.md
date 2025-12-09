## A tentative implementation of CRaC Resource management in conjunction with Logback.

During parsing of *logback.xml* configuration files, logback transforms the XML into a model tree. The model tree does not hold any references to the original XML.

Once created, the model tree is handed to a model processor which configures logback. Other processors can perform other types of processing.
As an example of "other processing", [logback-tyler](https://github.com/qos-ch/logback-tyler) transforms the model tree into a Java class.

If the XML file can be parsed without errors, the model tree is stored in object map of the `LoggerContext` under the key "CoreConstants.SAFE_JORAN_CONFIGURATION"

Given this background, the [LogbackCracDemo](https://github.com/ceki/logback-crac-demo/blob/main/src/main/java/ch/qos/logback/crac/LogbackCracDemo.java) application demonstrates a possible technique for integrating logback and CRaC.

### Quick overview of `LogbackCracDemo`
 
After reading the configuration file, `LogbackCracDemo` instantiates the CRaC resource [LogbackCracDelegate](https://github.com/ceki/logback-crac-demo/blob/main/src/main/java/ch/qos/logback/crac/LogbackCracDelegate.java) and registers it with CraC's global context.

`LogbackCracDelegate` retrieves the model tree  when its `beforeCheckpoint()` method is called. The model tree is saved in an instance variable.

At restoration time, the `afterRestore()` method of `LogbackCracDelegate` hands the previously saved model to the `JoranConfigurator.processModel()` method which configures the logback context anew.

Proceeding this way, all properties of the `LoggerContext` that were described in the *logback.xml* file are restored in just a few milli-seconds.

### Other remarks:

Tests were performed with Liberica JDK version 17.0.17-crac downloaded from https://bell-sw.com

Tested on Debian 13 and "criu" package version 4.1.1.1 installed.

Workaround: the `criu` binary that comes with Liberica JDK in
$JAVA_HOME/lib/criu was somehow defective. I had to replace it with
/usr/sbin/criu by copying it to $JAVA_HOME/lib/criu

Moreover, I was unable to run the test without **root privileges**, hence the `sudo` (see below).

### Testing yourself

Commands to run

```
export JAVA_HOME=/some/path/jdk-17.0.17-crac
mvn install   # creates target/logback-crac-demo-1.0-SNAPSHOT-all.jar
mkdir ./checkpoint-dir
sudo $JAVA_HOME/bin/java -XX:CRaCCheckpointTo=./checkpoint-dir/ -jar target/logback-crac-demo-1.0-SNAPSHOT-all.jar
```
Sample output

```
Crac Logback integration test
Logback configuration took 116 ms
Registering LogbackCracDelegate with CRaC...
LogbackCracDelegate instantiated
CRac 19:08:45.014 - Crac Logback integration test logging...1
CRac 19:08:46.015 - Crac Logback integration test logging...2
CRac 19:08:47.015 - Crac Logback integration test logging...3
CRac 19:08:48.016 - Crac Logback integration test logging...4
```

from another shell
```
> export JAVA_HOME=/some/path/jdk-17.0.17-crac
> sudo $JAVA_HOME/bin/jcmd target/logback-crac-demo-1.0-SNAPSHOT-all.jar  JDK.checkpoint
```
Sample output:
```
1551025:
CR: Checkpoint ...
```

from the first shell
```
> sudo $JAVA_HOME/bin/java -XX:CRaCRestoreFrom=./checkpoint-dir
```

Sample output

```
afterRestore called - Logback restoring from model...
Logback restore from model took 3 ms
CRac 19:09:20.775 - Crac Logback integration test logging...7
CRac 19:09:21.775 - Crac Logback integration test logging...8
CRac 19:09:22.776 - Crac Logback integration test logging...9
```

Note that while the initial configuration took **116ms**, the restoration from the model only took **3ms**.







