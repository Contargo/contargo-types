Contargo Types
===============
## v0.8.0 (Release on 2018-03-12)

* Changes phone number validation, to not assert `null` or empty string as a
  validation failure of the phone number, and instead opt to ignore and return
  `true`, in order to allow for composition with `@NonEmpty` or `@NonNull`
  validators. 

## v0.7.0 (Release on 2018-03-08)

* Add DIN5008 compliant formatting of phone numbers

## v0.6.7 (Release on 2018-03-07)

* Increase robustness of phone number validation

## v0.6.5 (Release on 2018-03-05)

* Add validation and formatting of phone numbers using Google's libphonenumber

## v0.6.3 (Release on 2017-12-14)

* Add pre-computation of normalized value and validation state of LicensePlate objects.
* Add a Container Type for ISO-Code 25UT
* Formats container numbers without dashes
 
## v0.6.2 (Release on 2017-08-28)

* Fix ISO 6346 validation of container numbers: add check for equipment category and remove this
  check in mere format validation
* Add some more container types

## v0.6.1 (Release on 2017-07-04)

* Add example of correct license plates for every country (extends LicensePlateCountry enum)

## v0.6.0 (Release on 2017-06-29)

* Introduce handler for unknown license plate countries (every license plate string is valid)

## v0.5.0 (Release on 2017-06-28)

* Rename isoCode and internationalIsoCode in @ContainerType to contargoHandlingCode and isoCode
* Add some more ContainerTypes

## v0.4.0 (Release on 2016-11-17)

* Introduce bean validation via annotation for container numbers (@ContainerNumber)

## v0.3.0 (Release on 2016-09-12)

* Introduce Contargo type for direction of a unit movement regarding one location.
* Add check on maximum length for Dutch license plates.

## v0.2.0 (Release on 2016-08-29)

* The license plate type is now bound to a country. Formatting and validation
  of license plates depends on the bound country.
  Currently supported countries are:
  * Germany
  * Netherlands
  * Belgium
  * Switzerland
  * France
  * Poland
  * Czech Republic
* Introduce Contargo types for container type and loading state.

## v0.1.0 (Release on 2016-07-05)

* Introduce Contargo types for container number and license plate providing
  basic functionality for formatting and validation.
