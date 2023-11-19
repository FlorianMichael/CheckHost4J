# CheckHost4J
Java implementation of the check-host.net API

## Contact
If you encounter any issues, please report them on the [issue tracker](https://github.com/FlorianMichael/CheckHost4J/issues).  
If you just want to talk or need help with CheckHost4J feel free to join my [Discord](https://discord.gg/BwWhCHUKDf).

## How to add this to your project

### Gradle/Maven
To use CheckHost4J with Gradle/Maven you can use [Maven Central](https://mvnrepository.com/artifact/de.florianmichael/CheckHost4J), [Lenni0451's repository](https://maven.lenni0451.net/#/releases/de/florianmichael/CheckHost4J) or [Jitpack](https://jitpack.io/#FlorianMichael/CheckHost4J).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/CheckHost4J/actions) or use the [Release](https://github.com/FlorianMichael/CheckHost4J/releases).

This library requires you to have [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1) in your
class path

## Example usage
The API main class is `CheckHost4J`, it contains all the methods to interact with the API.

```java
final CheckHost4J checkHost = CheckHost4J.INSTANCE;
```

You can either use the `CheckHost4J.INSTANCE` or create a new instance of `CheckHost4J` yourself, by creating
an own instance you can define the `IRequester` yourself, which is used to send the requests to the API.

```java
final CheckHost4J checkHost = new CheckHost4J(...);
```

The default `IRequester` is `JavaRequester`, which can be accessed via `JavaRequester.INSTANCE`, you can also
create a new instance using `new JavaRequester("<user-agent>")`.

```java
final CheckHost4J checkHost = new CheckHost4J(new JavaRequester("MyUserAgent"));
```

You can use the methods `CheckHost4J#ping`, `CheckHost4J#http`, `CheckHost4J#tcpPort`, `CheckHost4J#udpPort`
and `CheckHost4J#dns`
to get a `ResultNode<T>` where T is the result type of the request (e.g. `PingResult`, `TCPResult`).

```java
final ResultNode<PingResult> pingResult=checkHost.ping("example.com", 80 /* max nodes */);
```

After you got the `ResultNode<T>` you can use the `tickResults()` to update the `getResults()` list.

```java
// This will update the results list by sending the check-result request to the API,
// This might not update all results, because some might not be finished yet
// Which means you have to call this method multiple times to get all results (e.g. with a delay of 5 seconds)
pingResult.tickResults();

final Map<ServerNode, PingResult> results = pingResult.getResults();
// All results which are not finished yet will be null
```

You can also get all the server nodes which are being checked by using the `getNodes()` method.

```java
final List<ServerNode> nodes = pingResult.getNodes();
```

The `de.florianmichael.checkhost4j.model.result` package contains all the result classes, which are used
to store the result of the requests.

To get a list of all Request types you can use the `ResultType` enum.
