Contargo Types
===============

[![Build Status](https://travis-ci.org/Contargo/contargo-types.svg?branch=master)](https://travis-ci.org/Contargo/contargo-types)
[![](https://jitpack.io/v/Contargo/contargo-types.svg)](https://jitpack.io/#Contargo/contargo-types)

This is a library aimed at providing a collection of re-usable Contargo objects.

## Getting started

Using the `contargo-types` as a library in your project, means simply including
it as a Maven dependency. We recommend using the [Jitpack](https://jitpack.io)
repository:

```xml
<dependency>
    <!-- From Jitpack-repo -->
    <groupId>com.github.Contargo</groupId>
    <artifactId>contargo-types</artifactId>
    <version>${SOME-TAG}</version>
</dependency>
```

## Usage

### Container

#### Container number

```java
ContainerNumber containerNumber = ContainerNumber.forValue(string);
```

The built container number instance provides various information, such as the
validity on `isValid()` or the formatted value on `toString()`.


##### Validation

```java
 public class Container {

    @ContainerNumber
    public String containerNumber;
    ...
 }
```

Bean validation for container numbers via ```@ContainerNumber``` based on the ISO6346

Provide `validation.unit.number.error.message` in your message.properties to set the validators error message.

#### Container type

```java
ContainerType.FORTY_HC
```

The `ContainerType` enum provides the known container types.

#### Loading state

```java
LoadingState.EMPTY
LoadingState.FULL
```

The `LoadingState` enum provides the possible container loading states.

### Truck

#### License plate

```java
LicensePlate licensePlate = LicensePlate.forValue(string).withCountry(LicensePlateCountry.GERMANY);
```

The built license plate instance provides various information depending on the
bound country, such as the validity on `isValid()` or the formatted value on
`toString()`.

To find out which countries are supported, take a look at the
`LicensePlateCountry` enum.

### Transport

#### Direction

```java
Direction.TURN_IN
Direction.TURN_OUT
```

The `Direction` enum describes the direction of a unit movement regarding one location.

### Phone numbers

```java
PhoneNumber phoneNumber = new PhoneNumber(string);
```
The built phone number instance provides various information about the phone number, such as the country code or country calling code or the regional phone number. 

#### Format
The phone number instance can return the phone number in a international phone number formate.

## Development

This is a pretty straight-forward Java-project, use `mvn` to build, test and
deploy.

## License

This project is distributed under the Apache 2.0 License. The full set of
terms and conditions can be seen in the [LICENSE](LICENSE) file.


Happy hacking!
