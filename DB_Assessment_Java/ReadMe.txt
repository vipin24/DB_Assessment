DB Coding Assessment
======================================

This is a Spring Boot RESTful application which will give following Operations on Accounts of Customer.

 -Creating a new account
 -Reading an account
 -Making a transfer between two accounts

How to run
------------------------------

Build the project using gradle. From project root:
./gradlew clean build
------------------------------

Once the project built is completed, you can run the jar as follows:
-----------------
java -jar build\libs\DB_Assessment_Java-0.0.1.jar
-----------------

Example request(You can run using PostMan or any Other tool supporing these requests:
1.Creating Account of Customer(POST Request):
http://localhost:8888/v1/customers/
{
"customerId":"1234567",
"customerbalance":"123.0"
}

2. GET Request(For fetching Account details of Customerby Id):
http://localhost:8888/customers/accountNo/12345


3POST Request(For transferring funds from one Customer Account to another):
http://localhost:8888/customers/transfer
{
  "customerFromId":"12345",
  "customerToId":"1234567",
  "amount": "1.55"
}

Assumptions:
-Same Accounts transfer is not possible .

Further improvements
--------------------

-For Documentation part we can use Swagger in this so that for testing and readbility of application can be good. For this very little configurations
is to be done in code and dependancy need to be added in 'build.gradle'
-CustomerId validation should be performed like someone is not using 12345 directly or customer is not using its first name or lastname directly.
