# Acquired Java Library 1.0.0

## Description ##
The Acquired API Library for Java enables you to work with Acquired APIs.

## Directory ##
```html
|--src.main.java.com.acquired
    AQPay.java
    AQPayCommont.java
pom.xml
readme.md  
``` 

## Documentation  ##
https://developer.acquired.com/integrations

## Installation ##
You can simply Download the Release

### Maven ###
Add this dependency to your project's POM:
```java
<dependency>
    <groupId>com.acquired</groupId>
    <artifactId>acquired-java-api-library</artifactId>
    <version>1.0.2</version>	    
</dependency>
```



## Examples ##
#### Get start

1. import the below file in the example files.

```php
import com.acquired.*;
```

#### How to use
It is very simply to use like this:
1. new a AQPay obj.
```java
AQPay aqpay = new AQPay();
```
2. set parameters.
```java
aqpay.setParam("amount", "1");
```
3. post parameters according to your transaction type.
```java
JsonObject result = aqpay.capture();
```
4. deal response.
```java
if(aqpay.isSignatureValid(result)) {
    
    // do your job
    
}
```
The code examples on using this library are located in the library section of the api-sdk-java repository: https://github.com/AcquiredSupport/Acquired-api-sdk-java


## Requirements

gson-2.7.jar

