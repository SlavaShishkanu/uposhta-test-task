# Test Task
###1. Technologies
* Java 1.8
* Spring 4.3.0.RELEASE
* Maven 3.3.3
* HSQLDB 2.3.2
* Hibernate 4.3.5.Final
* Jetty

###2. To Run this project locally
```shell
$ git clone https://github.com/Sabfir/test_task_20170427.git
$ cd test_task_20170427
$ mvn jetty:run
```

###3. To Use this project<br/>
Access ```http://localhost:8080/addresses``` to see list of addresses log to the db<br/>
Access ```http://localhost:8080/shipments``` to see list of shipments

###4. Task To Be Done</br>
Prerequisites:</br>
 - all tasks should be done independently in your own repository in the separate branch
 - finished work should include pool request ready to merge to master</br>
 - the link to the pool request should be sent as a result of the work
1. Run integration tests. Fix failed integration tests (IT) if any.</br>
Additional info. You should not change IT, they are correct, you should find errors in business logic.
2. We have Shipment.java class with the fields: weight, length, width, height, declaredPrice, price.
   The task is to create Parcel.java and ParcelItem.java classes.</br>
   Shipment should have a list of parcels and parcel should have a list of parcel items.</br>
   Shipment fields (weight, length, width, height, declaredPrice, price) should be removed to the class Parcel.java.</br>
   Shipment.price should be equals to the sum of prices of all Parcels in the Shipment.</br>
   The current shipment price calculation should be moved to the parcel, every parcel in the shipment should be calculated and the sum of parcels prices should be written into the shipement price.</br>
   ParcelItem.java should have fields: name, quantity, weight, price - all this info just for statistic and shouldn't be used for calculation price of the shipment or something.</br>
   You should implement (change existent) rest endpoint to create Shipment with the list of parcels and list of items in each parcel.</br>
   You should fix broken integration tests.</br>
   You should check your code using checkstyle plugin.</br>
   The data to set up checkstyle is in the resources/checkstyle folder.</br>
   All errors given by checkstyle should be fixed.</br>
