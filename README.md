# What is NTC C.H.I.P. Java GPIO Library?

Library which helps to communicate with CHIP GPIO using Java language.

# How add into your project (maven)?

Clone project and install in local repository.

```shell
git clone https://github.com/LuciferAndDiablo/NTC-C.H.I.P.-JavaGPIOLib.git
cd GPIOChipLib
mvn install
```

Add maven dependency into your project.
```xml
<dependencies>
  ...
  <dependency>
    <groupId>free.lucifer</groupId>
    <artifactId>chiplib</artifactId>
    <version>0.1.2</version>
  </dependency>
  ...
</dependencies>
```

# How use it?

Just read data.
```java
    Chip.I.open(); // Prepare low level access
    Chip.Pin pin1 = Chip.Pin.XIO_P7;
    Chip.I.pinMode(pin1, Chip.Pin.PinMode.INPUT); // Setup pin mode
    while (true) {
        System.out.println(Chip.I.digiatalRead(pin1));
    }
```

Read DHT11 sensor
```java
    DHT11 dht = new DHT11(Chip.Pin.CSID0);
    Chip.I.addTask(dht);
    while(dht.getStatus() != DHT11.Status.OK) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
    }
    
    System.out.println("Temperature: " + dht.getTemperature());
    System.out.println("Humidity: " + dht.getHumidity());
```

...

# Limitation

If you want to use all of CHIP pins, you should run your project thru **sudo** or add your user to **super user** group.
...

# Some info

Library uses JNA to access to native IO libraries.
Also library uses direct access to memory instead of file system access (which may improve the speed).

# Tasks

- [x] SPI
- [x] DHT11 (temperature sensor)
- [x] I2C
- [x] SSD1306 (LCD)
- [ ] nRF24L01+ (network)
- [ ] A4988 (stepper driver)
- [ ] Soft PWM at high speed
