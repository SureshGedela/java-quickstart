Zuora API Java Quickstart

INTRODUCTION
------------

Thank you for downloading the Zuora QuickStart Java Toolkit.  This download contains code designed to help you begin using Zuora APIs.

REQUIREMENTS
------------

JDK 1.6 
Apache Axis 1.4
Apache Ant 

CONTENTS
--------

This sample zip contains:

    /readme.txt - this file
    /build.xml - ant build files to compile and run the test code manually 
  	/testdata.properties - external property file for setting different values while running the application
	/zuora.17.0.wsdl - The latest version of the WSDL
    /lib - contains all dependent jar files to run the sample
    /src - example code to execute the Zuora APIs
    /.classpath & .project - project files for eclipse

DOCUMENTATION & SUPPORT
-----------------------

API Documentation is available at http://developer.zuora.com

PRE-REQUISITES
--------------

The following are pre-requisites to successfully run the sample code:

1. A Zuora Tenant
2. A Zuora User
    a.) with the User Role Permission to create Invoices (http://knowledgecenter.zuora.com/index.php/Z-Billing_Admin#Manage_User_Roles)
3. A Product created with a Rate Plan & Rate Plan Component (http://knowledgecenter.zuora.com/index.php/Product_Catalog), with
    a.) The Effective Period (Start & End) of the Product/Rate Plan not expired (start < today and end > today)
    b.) An Accounting Code specified on the Rate Plan Component (Update $AccountingCode in main.php with the code you specify)
4. A Zuora Gateway set up (http://knowledgecenter.zuora.com/index.php/Z-Payments_Admin#Setup_Payment_Gateway)
    a.) Either Authorize.net, CyberSource, PayPal Payflow Pro (production or test)
    b.) The setting "Verify new credit card" disabled

RUNNING THE EXAMPLE
-------------------

1. Unzip the files contained in the quickstart_java.zip file to a folder on you hard drive.  
2. In test.properties, specify:
    a.) the username for your Zuora user.
    b.) the password for your Zuora user.
    c.) if you are testing against apisandbox, change the endpoint to https://apisandbox.zuora.com/apps/services/a/17.0
3. From the command line, run the test using ant
    a.) "ant crud" to test create, update and delete of active account
    b.) "ant subscribe" to test subscribe() call

COMPILING THE EXAMPLE
--------------------- 

1. From the command line, run "ant compile" to generate the Java stubs and compile the test code

