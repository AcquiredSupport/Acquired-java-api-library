# Acquired Java Library 1.0.0

## Description ##
The Acquired API Library for Java enables you to work with Acquired APIs.

## Directory ##
```html
|--WebContent.examples   (html files)
    |--public
        |--css
            general.css
    auth_.html    
    refund.html
    ...
|--src.com.Acquired
    |--form   (recieve html post data)
        Auth_.java
        Refund.java
    |--helper   (Acquired api sdk)
        AQPay.java
        AQPayCommont.java
        AQPayConfig.java
|--WebContent.WEB-INF.lib  
    gson-2.7.jar
readme.md  
index.html
``` 

## Documentation  ##
https://docs.acquired.com/api.php

## Installation ##
You can simply Download the Release

## Examples ##
#### Get start

1. set config parameters in AQPayConfig.java.
2. import the below file in the example files.

```php
import com.Acquired.helper.AQPay;
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

## Requirements

gson-2.7.jar

