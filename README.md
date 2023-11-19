# CheckHost4J
Java implementation of the check-host.net API

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/FlorianMichael/CheckHost4J/issues).  
If you just want to talk or need help with CheckHost4J feel free to join my
[Discord](https://discord.gg/BwWhCHUKDf).

## How to add this to your project
### Gradle/Maven
To use CheckHost4J with Gradle/Maven you can use this [Maven server](https://maven.lenni0451.net/#/releases/de/florianmichael/CheckHost4J) or [Jitpack](https://jitpack.io/#FlorianMichael/CheckHost4J).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/CheckHost4J/actions) or use the [Release](https://github.com/FlorianMichael/CheckHost4J/releases).

This library requires you to have [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1) in your class path

## Example usage
Here is an example of how to use the API
```java
long time = System.currentTimeMillis();
final ResultNode<PingResult> result = CheckHost4J.INSTANCE.ping("gommehd.net", 80);
while (true) {
    if ((System.currentTimeMillis() - time) >= 2500) { // 2.5 seconds passed since last update
        System.out.println("Updating results...");
        result.tickResults();

        // Print current results
        System.out.println("#####################################");
        result.getResults().forEach((serverNode, pingResult) -> {
            System.out.println(serverNode.name + " | " + serverNode.country + " | " + serverNode.ip + " | " + serverNode.asName);
            if (pingResult != null) {
                for (PingResult.PingEntry entry : pingResult.pingEntries) {
                    System.out.println(entry.address + " | " + entry.ping);
                }
            } else {
                System.out.println("No result for this server");
            }
        });
        System.out.println("#####################################");
        time = System.currentTimeMillis();
    }
}
```
You can use these functions in the CheckHostAPI class:
**ping, http, tcpPort, udpPort and dns** <br>
The field you get you have to save, and then you can update it in a certain delay by calling the update method, keep in mind that this method will send an API request to CheckHost every time you call the create utils, an API request will also be sent automatically.
