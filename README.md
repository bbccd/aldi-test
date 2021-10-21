# aldi-test
Testing Aldi Süd web site using Selenium and TestNG (Java).

## Set up

1. Clone from GitHub using: `git clone https://github.com/bbccd/aldi-test`
2. Put a Selenium `chromedriver` into the drivers directory (under `/src/test/resources/drivers` - if run under Windows, check the driver's path in the test cases under `/src/test/java/tests` subdirectory)
3. Run the test using: `mvn test`

The test searches for the availability of one specific item in one specific store. The set up happens in stored properties on test case `SearchAndAvailabilityCheckTest.java`. The items are defined by the search term (the term that will be searched for on the home page) and the product name (this is a String used for assertions). The store used for availability check is defined by the property `postalCode` (test currently uses first result). The availability is checked against a threshold value defined here in `availabilityLevel`: if this is set to "0", only "0" values are accepted. If set to higher values, anything at least as large or larger is accepted. Currently, Aldi Süd returns three possible availability values that are interpreted as non-zero: 1 ("Wenig verfügbar"), "2" ("Geringe Verfügbarkeit"???) and "3" ("Verfügbar").
