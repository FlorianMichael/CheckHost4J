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
public class Test {
    private static long time = System.currentTimeMillis();

    public static void main(String[] args) throws IOException {
        final var field = CheckHostAPI.createPingRequest("<your ip>", 10);
        while (true) {
            if ((System.currentTimeMillis() - time) >= 2500) {
                System.out.println("Updating");
                field.update();

                System.out.println("#####################################");
                for (CHServer server : field.servers()) {
                    final var result = field.getResult().get(server);

                    System.out.println(server.name() + " | " + server.country());
                    if (result != null) {
                        for (String s : result.pingEntries().stream().map(pingEntry -> pingEntry.address() + "|" + pingEntry.ping()).toList()) {
                            System.out.println(s);
                        }
                    } else {
                        System.out.println("No result for this server");
                    }
                }
                System.out.println("#####################################");
                time = System.currentTimeMillis();
            }
        }
    }
}
```
You can use these functions in the CheckHostAPI class:
**createPingRequest, createTCPRequest, createUDPRequest, createHTTPRequest and createDNSRequest** <br>
The field you get you have to save, and then you can update it in a certain delay by calling the update method, keep in mind that this method will send an API request to CheckHost every time you call the create utils, an API request will also be sent automatically.
