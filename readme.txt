<<<<<<< HEAD
/*    Copyright (c) 2010 Zuora, Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *   this software and associated documentation files (the "Software"), to use copy, 
 *   modify, merge, publish the Software and to distribute, and sublicense copies of 
 *   the Software, provided no fee is charged for the Software.  In addition the
 *   rights specified above are conditioned upon the following:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   Zuora, Inc. or any other trademarks of Zuora, Inc.  may not be used to endorse
 *   or promote products derived from this Software without specific prior written
 *   permission from Zuora, Inc.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 *   ZUORA, INC. BE LIABLE FOR ANY DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *   ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

Zuora API Java Quickstart

INTRODUCTION
------------

Thank you for downloading the Zuora QuickStart Java Toolkit.  This download contains code designed to help you begin using Zuora APIs.

Zuora Support does not troubleshoot content from GitHub. The sample code is as an example of code 
that has worked for previous implementations and was created by both Zuora and non-Zuora authors. 
Please send any GitHub-related comments and feedback to Dev-Support@zuora.

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
  	/test.properties - external property file for setting different values while running the application
	/zuora.17.0.wsdl - The latest version of the WSDL
    /jsp/signup.jsp - drop-in JSP page that uses API to display sign-up page and process new order via subscribe() call.
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
    b.) To keep things simple ,you'd better create a product with flat-fee of one-time charge for testing.
4. A Zuora Gateway set up (http://knowledgecenter.zuora.com/index.php/Z-Payments_Admin#Setup_Payment_Gateway)
    a.) Either Authorize.net, CyberSource, PayPal Payflow Pro (production or test)
    b.) The setting "Verify new credit card" disabled
5. Modify the Default Subscription Settings
	a.) Turn off the "Require Customer Acceptance of Orders?"
	b.) Turn off the "Require Service Activation of Orders?" 

COMPILING THE EXAMPLE
--------------------- 

1. From the command line, run "ant compile" to generate the Java stubs and compile the test code

RUNNING THE EXAMPLE
-------------------

1. Unzip the files contained in the quickstart_java.zip file to a folder on you hard drive.  
2. In test.properties, specify:
    a.) the username for your Zuora user.
    b.) the password for your Zuora user.
    c.) if you are testing against apisandbox, change the endpoint to https://apisandbox.zuora.com/apps/services/a/17.0
    d.) the productName as the name of the Product
    
3. From the command line, run the test using ant
    a). "ant c-account": Creates an Active Account (Account w/ Status=Active and Bill To Contact/Payment Method)
	b). "ant c-subscribe": Creates new subscription,one-call
	c). "ant c-subscribe-no-p": Creates new subscription,one-call,no payments
	d). "ant c-subscribe-w-existingAccount": Creates new subscription on existing account
	e). "ant c-subscribe-w-amendment": Creates new subscription ,upgrade and downgrade
	f). "ant cnl-subscription": Cancel subscription
	g). "ant c-payment": Creates payment on invoice
	h). "ant c-usage": Add usage
	I). "ant all": run all test case as above

4. To run the signup.jsp page, you need to have
    a.) Setup a webserver (Tomcat or Resin will do)
    b.) Set source path to include both the /src directory and the /generated/axis2/src paths
    c.) Include the /lib in the classpath
    d.) Run the webserver and hit the signup.jsp page

=======
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
  	/test.properties - external property file for setting different values while running the application
	  /zuora.a.38.0.wsdl - The latest version of the WSDL
    /lib - contains all dependent jar files to run the sample
    /src - example code to execute the Zuora APIs
    /generated - the Java classes generated from Zuora WSDL using Axis WSDL2Java toolkit.
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
    c.) if you are testing against apisandbox, change the endpoint to https://apisandbox.zuora.com/apps/services/a/38.0
3. From the command line, run the test using ant
    a.) "ant crud" to test create, update and delete of active account
    b.) "ant subscribe" to test subscribe() call
4. Run from command line as a java application.
	a.)"account-create" to test create Account. Get the proper argument from ApiTest.java to run the specific operation i,e account-update, subscribe-create etc.

COMPILING THE EXAMPLE
--------------------- 

1. From the command line, run "ant compile" to generate the Java stubs and compile the test code

DEBUGGING THE EXAMPLE
---------------------
1. Load the project into Eclipse.  
a) Copy the unzipped foler into your Eclipse workspace
b) In Eclipse, create a new Java project: 
- File -> New -> Java Project
- Enter project name, and point the project's root folder to the unzipped folder's location
- Click 'Next'
- Click 'Finish'
2. Create a Run/Debug Configuraiton for ApiTest.java
c) Navigate to src/com.zuora.api/ApiTest.java, and right click to select "Run As => Run Configurations".  Click on "Arguments" tab, and specify the VM parameters as: 
  -Dtest.properties=./test.properties
  
UPDATE THE WSDL VERSION
-----------------------
If you want to use another WSDL version instead of the current WSDL version in the sample code, you can follow the steps below: 
1. In test.properties file, change the zuora.wsdl.version value
2. In test.properties file, change the version number in the end point
3. Copy the WSDL file to the root folder, e.g. zuora.a.37.0.wsdl
4. Run "ant setup".  This will re-generate the Java classes from the new WSDL file.

>>>>>>> Created new Java sample code and unit tests and upgraded Zuora WSDL version to 38
