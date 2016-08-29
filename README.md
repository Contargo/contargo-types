Contargo Types
===============

This is a library aimed at providing a collection of re-usable Contargo objects.

## Getting started

Using the `contargo-types` as a library in your project, means simply including
it as a Maven dependency:

```xml
<dependency>
    <groupId>net.contargo</groupId>
    <artifactId>contargo-types</artifactId>
    <version>LATEST</version>
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

## Development

This is a pretty straight-forward Java-project, use `mvn` to build, test and
deploy.

Happy hacking!
