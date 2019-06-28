Contargo Types
===============

## v0.17.5

* Optimize ValidPhoneNumberValidator, checking for different constraints such as phone number is zero number, can not be formatted or is to long.

## v0.17.4

* Introduce the possibility to reset UniquenessValidator implementations. Reset means that all in-memory data is
  deleted. Reprovisioning the data lies in the responsibility of the calling code.

## v0.17.3

* fix PhoneNumberFormatter, the numbers was formatted by the google library and then splitted into country code, areaCode 
and connectionNumber. If the Number does have connectionNumbers with mor than on empty space splitted only the first part
was used. 

## v0.17.2

* fix broken tests from PhoneNumbers. 

## v0.17.1

* use PhoneNumber in ContactInformation instead of String for phone- and mobile numbers
* change PhoneNumber only format number if method is called. Has country code, raw phone number and extension 
to a phone number. 

## v0.17.0

* Adds PhoneNumber to format phone numbers and get information to a specific phone number, 
like the country code or country calling code.
* Change the amount of characters for license plates to max. 15 characters.

## v0.16.1

* Adds handlingcode 20OS with ISO-Code 22G2

## v0.15.0

* Separated uniqueness- and completeness validators in order to support use cases where only one of the checks
  is needed.

## v0.14.0

* BulgarianLicensePlateHandler: Adds missing letters to validation string.

## v0.13.0

* ContactInfoValidator: Report ContactInformation with duplicate mobile number as violation even 
  if an email address is present (previously this was not the case).

## v0.12.0

* Add bulgrian license plate

## v0.11.x

* Add support for removal of users and change of contact options for existing users
  to the RequiredContactInfoValidationService.

## v0.10.x

* Add validation service that checks for valid contact options of a given COLA user

## v0.9.0

* Add romanian license plate

## v0.8.1 (Release on 2018-03-13)

* Update `contargo-domain` to latest Jitpack-based version v0.3.0.

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
