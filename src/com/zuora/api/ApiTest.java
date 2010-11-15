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
package com.zuora.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.*;
import com.zuora.api.axis2.ZuoraServiceStub.Error;

public class ApiTest {
   
   private static final String FILE_PROPERTY_NAME = "test.properties";
   private static final String PROPERTY_ENDPOINT = "endpoint";
   private static final String PROPERTY_USERNAME = "username";
   private static final String PROPERTY_PASSWORD = "password";
   private static final String PROPERTY_PRODUCT_NAME = "productName";

   private ZuoraServiceStub stub;
   private SessionHeader header;
   private Properties properties;

   public static void main(String[] arg) {
            
      try {
         ApiTest test = new ApiTest();
         test.login();
         
         if("all".equals(arg[0])){
        	 test.testCreateAccount();
        	 print("");
        	 test.testSubscribe();
        	 print("");
        	 test.testSubscribeWithNoPayment();
        	 print("");
        	 test.testUpgradeAndDowngrade();
        	 print("");
        	 test.testSubscribeWithExistingAccount();
        	 print("");
        	 test.testCancelSubscription();
        	 print("");
        	 test.testMakePayment();
        	 print("");
        	 test.testAddUsage();
         }
         else if("c-account".equals(arg[0])){
        	 test.testCreateAccount();
         }
         else if("c-subscribe".equals(arg[0])){
        	test.testSubscribe(); 
         }
         else if("c-subscribe-no-p".equals(arg[0])){
        	 test.testSubscribeWithNoPayment(); 
         }
         else if("c-subscribe-w-existingAccount".equals(arg[0])){
        	 test.testSubscribeWithExistingAccount(); 
         }
         else if("c-subscribe-w-amendment".equals(arg[0])){
        	 test.testUpgradeAndDowngrade();
         }
         else if("cnl-subscription".equals(arg[0])){
        	 test.testCancelSubscription();
         }
         else if("c-payment".equals(arg[0])){
        	 test.testMakePayment();
         }
         else if("c-usage".equals(arg[0])){
        	 test.testAddUsage();
         }
         else if("help".equals(arg[0])){
        	 printHelp();
         }
         else{
        	 print("command not found.");
        	 printHelp();
         }
      } catch(com.zuora.api.axis2.LoginFault e) {
         print("Login Failure : " + e.getFaultMessage().getLoginFault().getFaultMessage());
      } catch (Exception e) {
         print(e.getMessage());
         e.printStackTrace();
      }
   }
	
	public ApiTest() throws AxisFault {
	     try {
	         this.stub = new ZuoraServiceStub();
	         
	         // set new endpoint
	         String endpoint = getPropertyValue(PROPERTY_ENDPOINT);
	         if (endpoint != null && endpoint.trim().length() > 0){
	            ServiceClient client = stub._getServiceClient();
	            client.getOptions().getTo().setAddress(endpoint);
	         }

	      } catch (AxisFault e) {
	         print(e.getMessage());
	         throw e;
	      }
	}

   private void login() throws Exception {
      
      Login login = new Login();
      login.setUsername(getPropertyValue(PROPERTY_USERNAME));
      login.setPassword(getPropertyValue(PROPERTY_PASSWORD));
      
      LoginResponse resp = stub.login(login);
      LoginResult result = resp.getResult();

      // create session for all subsequent calls
      this.header = new SessionHeader();
      this.header.setSession(result.getSession());
      
   }
   
   private void testAccountCRUD() throws Exception {
      
      ID accId = createAccount(true);
      
      Account accountQuery = queryAccount(accId);
      print("Create Active Account");
      print(toString(accountQuery));
      
      Account accUpdate = new Account();
      accUpdate.setId(accId);
      accUpdate.setName("testUpdate" + System.currentTimeMillis());
      
      update(accUpdate);

      accountQuery = queryAccount(accId);
      print("Updated Account");
      print(toString(accountQuery));
      
      boolean deleted = delete("Account", accId);
      print("Deleted Account: " + deleted);
   }
   /*
    * 
	   1. create account
	   2. create bill to/sold to
	   3. create default payment method
	   4. update account to active
    */
   public ID createAccount(boolean active) throws Exception {

      // create account
      Account acc1 = makeAccount();
      ID accountId = create(acc1);

      if (active) {

          // create contact
          Contact con = makeContact();
          con.setAccountId(accountId);
          ID contactId = create(con);

          PaymentMethod pm = makePaymentMethod();
          pm.setAccountId(accountId);
          ID pmId = create(pm);

          // set required active fields and activate
          Account accUpdate = new Account();
          accUpdate.setId(accountId);
          accUpdate.setStatus("Active");
          accUpdate.setSoldToId(contactId);
          accUpdate.setBillToId(contactId);
          accUpdate.setAutoPay(true);
          accUpdate.setPaymentTerm("Due Upon Receipt");
          accUpdate.setDefaultPaymentMethodId(pmId);
          update(accUpdate);
      }

      return accountId;
  }
   
   private ID create(ZObject acc) throws Exception {

      Create create = new Create();
      create.setZObjects(new ZObject[]{acc});
      
      CreateResponse cResponse = stub.create(create, this.header);
      ZuoraServiceStub.SaveResult result = cResponse.getResult()[0];
      ID id = result.getId();

      return id;
   }
   
   private Account queryAccount(ID accId) throws Exception {
      ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
      query.setQueryString("SELECT id, name, accountnumber,DefaultPaymentMethodId FROM account WHERE id = '"+accId.getID()+"'");
      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
      Account rec = (Account)qResult.getRecords()[0];
      return rec;
   }
   
   private Subscription querySubscription(ID subscriptionId) throws Exception {
      ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
      query.setQueryString("SELECT id, name,status,version FROM Subscription WHERE Id = '"+subscriptionId.getID()+"'");
      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
      Subscription rec = (Subscription)qResult.getRecords()[0];
      return rec;
   }
   
   private Subscription queryPreviousSubscription(ID id) throws Exception {
	      ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
	      query.setQueryString("SELECT id, name,status,version FROM Subscription WHERE PreviousSubscriptionId = '"+id.getID()+"'");
	      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
	      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
	      Subscription rec = (Subscription)qResult.getRecords()[0];
	      return rec;
	   }
   
   private ID update(ZObject acc) throws Exception {

      Update update = new Update();
      update.setZObjects(new ZObject[]{acc});
      
      UpdateResponse cResponse = stub.update(update, this.header);
      ZuoraServiceStub.SaveResult result = cResponse.getResult()[0];
      ID id = result.getId();

      return id;
   }

   private boolean delete(String type, ID id) throws Exception {
      
      Delete delete = new Delete();
      delete.setType(type);
      delete.setIds(new ID[]{id});
      
      DeleteResponse cResponse = stub.delete(delete, this.header);
      ZuoraServiceStub.DeleteResult result = cResponse.getResult()[0];

      return result.getSuccess();

   }
   /*
    * 8 User case start: 
    */
   /*
    * # CREATE ACTIVE ACCOUNT
	   # method to create an active account. requires that you have:
		#
		#   1.) a gateway setup, 
		#   2.) gateway configured to not verify new credit cards
		#
		# if you want to verify a new credit card, make sure the card info
		# you specify is correct.
    */
   private void testCreateAccount()throws Exception{
	   print("Account Create....");
	   ID accountId = createAccount(true);
	   print("\tAccount Created:"+accountId.toString());
   }
   
   /*
    * # CREATE NEW SUBSCRIPTION, ONE-CALL

	   1. query product catalog for product rate plan (no charge, to simplify)
	   2. subscribe() call with account/contact/payment method (one call)
	   3. query subscription
   */
   private void testSubscribe() throws Exception {
	   print("Subscribe call....");
	   SubscribeResult[] result = createSubscribe(Boolean.TRUE);
	   print("\t"+createMessage(result));
	   Subscription sQuery = querySubscription(result[0].getSubscriptionId());
       print("\tSubscription created:"+ sQuery.getName());
   }
   /*
    * # CREATE NEW SUBSCRIPTION, ONE-CALL, NO PAYMENTS

	   1. query product catalog for product rate plan (no charge, to simplify)
	   2. subscribe call w/ #1 above
	   3. subscribe options processpayments=false
	   4. query subscription
    */
   private void testSubscribeWithNoPayment()throws Exception{
	   print("Subscribe(no payments) call....");
	   SubscribeResult[] result = createSubscribe(Boolean.FALSE);
	   print("\t"+createMessage(result));
	   Subscription sQuery = querySubscription(result[0].getSubscriptionId());
       print("\tSubscription created:"+ sQuery.getName());
   }
   /*
	   # CREATE NEW SUBSCRIPTION ON EXISTING ACCOUNT
	
	   1. create active account
	   2. query product catalog for product rate plan (no charge, to simplify)
	   3. subscribe w/ existing
	   4. query subscription
	*
	*/
   private void testSubscribeWithExistingAccount()throws Exception{
	   ID accountId = createAccount(true);
	   print("Subscribe(with existing account["+accountId.getID()+"]) call....");
	   createSubscribeWithExistingAccount(accountId);
   }
   
   /*
    * # CREATE NEW SUBSCRIPTION, UPGRADE AND DOWNGRADE

	   1. create new order, one-call (#2)
	   2. create amendment w/ new product (upgrade)
	         1. new amendment
	         2. new rate plan, type=NewProduct
	         3. update amendment
	   3. query subscription 
	   4. create amendment w/ remove product (downgrade)
	
	         1. new amendment
	         2. new rate plan, type=RemoveProduct
	         3. update amendment
    */
   private void testUpgradeAndDowngrade()throws Exception{
	   print("Subscribe(do upgrade and downgrade) call....");
	   //subscribe
	   SubscribeResult[] result = createSubscribe(Boolean.TRUE);
	   Subscription sub = querySubscription(result[0].getSubscriptionId());
	   print("\t"+createMessage(result));
	   print("\tSubscription created:"+ sub.getName());
	   print("\tSubscription Version :"+sub.getVersion());
	   Calendar ca = Calendar.getInstance();
	   
	   //upgrade (add new product)
	   ID rpID = createAmendmentWithNewProduct(sub.getId(),ca);
	   
	   //query new version subscription
	   Subscription newSub_new = queryPreviousSubscription(sub.getId());
	   print("\tSubscription Version :"+newSub_new.getVersion());
	   
	   //downgrade (remove new product)
	   createAmendmentWithRemoveProduct(newSub_new.getId(),rpID,ca);
	   
	   //query new subscription
	   Subscription newSub_remove = queryPreviousSubscription(newSub_new.getId());
	   print("\tSubscription Version :"+newSub_remove.getVersion());
   }
   
   /*
    * # CANCEL SUBSCRIPTION

	   1. create new order, one-call (#2)
	   2. create amendment w/ remove product
	         1. new amendment
	         2. new rate plan, type=RemoveProduct
	         3. update amendment
	   3. query new subscription
	    */
   private void testCancelSubscription()throws Exception{
	   print("Cancel Subscribe....");
	   SubscribeResult[] result = createSubscribe(Boolean.TRUE);
	   print("\t"+createMessage(result));
	   Subscription sub = querySubscription(result[0].getSubscriptionId());
       print("\tSubscription created:"+ sub.getName());
       print("\tSubscrption status :"+sub.getStatus());
       
	   Calendar effectiveDate = Calendar.getInstance();
	   effectiveDate.add(Calendar.DAY_OF_MONTH, 1);
	   Amendment amd = new Amendment();
	   amd.setName("Amendment:Cancellation");
	   amd.setEffectiveDate(effectiveDate);
	   amd.setType("Cancellation");
	   amd.setSubscriptionId(sub.getId());
	   amd.setStatus("Draft");
	   ID amdID= create(amd);
	   
	   Amendment updateAmd = new Amendment();
	   updateAmd.setId(amdID);
	   updateAmd.setContractEffectiveDate(effectiveDate);
	   updateAmd.setStatus("Completed");
	   amdID= update(updateAmd);
	   print("\tDowngrade completed(amendment id:"+amdID+").");
	   
	  //query new subscription
	   Subscription newSub_cancel = queryPreviousSubscription(sub.getId());
	   print("\tSubscrption status :"+newSub_cancel.getStatus());
   }
   /*
	   # CREATE PAYMENT ON INVOICE
	
	   1. subscribe() call with account/contact/payment method (one call)
	   2. create payment against invoice
   */
   private void testMakePayment()throws Exception{
	   print("Create Payment against Invoice....");
	   SubscribeResult[] result = createSubscribe(Boolean.FALSE);
	   print("\t"+createMessage(result));
	   Subscription subscription = querySubscription(result[0].getSubscriptionId());
	   print("\tSubscription created:"+ subscription.getName());
	   
	   ID iId = result[0].getInvoiceId();
	   ID aId = result[0].getAccountId();
	   Account account = queryAccount(aId);
	   
	   Payment payment = new Payment();
	   payment.setEffectiveDate(Calendar.getInstance());
	   payment.setAccountId(aId);
	   payment.setAmount(1.00);
	   payment.setPaymentMethodId(account.getDefaultPaymentMethodId());
	   payment.setType("Electronic");
	   payment.setStatus("Draft");
	   ID pId = create(payment);
	   
	   InvoicePayment ip = new InvoicePayment();
	   ip.setAmount(payment.getAmount());
	   ip.setInvoiceId(iId);
	   ip.setPaymentId(pId);
	   ID ipId = create(ip);
	   Payment updatePayment = new Payment();
	   updatePayment.setId(pId);
	   updatePayment.setStatus("Processed");
	   pId = update(updatePayment);
	   
	   print("\n\tPayment created:"+pId);	   
   }
   
   private void testAddUsage()throws Exception{
	   print("Create Usage....");
	   ID aId = createAccount(true);
	   
	   Usage usage = new Usage();
	   usage.setAccountId(aId);
	   usage.setQuantity(20.0);
	   usage.setUOM("Each");
	   usage.setStartDateTime(Calendar.getInstance());
	   ID uID = create(usage);
	   
	   print("\tUsage created:"+uID.getID());
   }
   
   /*
    * 8 user case end
    */
   private SubscribeResult[] createSubscribe(boolean isProcessPayment)throws Exception{
	  ProductRatePlan prp = getProductRatePlanByProductName(getPropertyValue(PROPERTY_PRODUCT_NAME));
	      
      Account acc = makeAccount();
      Contact con = makeContact();
      PaymentMethod pm = makePaymentMethod();
      Subscription subscription = makeSubscription();

      SubscribeOptions sp = new SubscribeOptions();
      sp.setGenerateInvoice(Boolean.TRUE);
      sp.setProcessPayments(isProcessPayment);
      
      SubscriptionData sd = new SubscriptionData();
      sd.setSubscription(subscription);
      
      RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(prp);
      sd.setRatePlanData(subscriptionRatePlanDataArray);
      
      SubscribeRequest sub = new SubscribeRequest();
      sub.setAccount(acc);
      sub.setBillToContact(con);
      sub.setPaymentMethod(pm);
      sub.setSubscriptionData(sd);
      sub.setSubscribeOptions(sp);

      SubscribeRequest[] subscribes = new SubscribeRequest[1];
      subscribes[0] = sub;

      Subscribe sr = new Subscribe();
      sr.setSubscribes(subscribes);

      SubscribeResponse resp = stub.subscribe(sr, this.header);
      return resp.getResult();
      
   }
   
   private ID createAmendmentWithNewProduct(ID subID,Calendar effectiveDate)throws Exception{
	   ProductRatePlan prp = getProductRatePlanByProductName(getPropertyValue(PROPERTY_PRODUCT_NAME));
	   //create Amendment 
	   Amendment amd = new Amendment();
	   amd.setName("Amendment:new Product");
	   amd.setType("NewProduct");
	   amd.setSubscriptionId(subID);
	   amd.setStatus("Draft");
	   ID amdID= create(amd);
	   
	   //create rate plan
	   RatePlan rp = new RatePlan();
	   rp.setAmendmentId(amdID);
	   rp.setAmendmentType(amd.getType());
	   rp.setProductRatePlanId(prp.getId());
	   ID rpID = create(rp);
	   
	   Amendment updateAmd = new Amendment();
	   updateAmd.setId(amdID);
	   updateAmd.setContractEffectiveDate(effectiveDate);
	   updateAmd.setStatus("Completed");
	   amdID = update(updateAmd);
	   print("\tUpgrade completed(amendment id:"+amdID+")");
	   return rpID;
   }
   
   private ID createAmendmentWithRemoveProduct(ID subID,ID rpID ,Calendar effectiveDate)throws Exception{
	   ProductRatePlan prp = getProductRatePlanByProductName(getPropertyValue(PROPERTY_PRODUCT_NAME));
	   //create Amendment 
	   Amendment amd = new Amendment();
	   amd.setName("Amendment:remove Product");
	   amd.setEffectiveDate(effectiveDate);
	   amd.setType("RemoveProduct");
	   amd.setSubscriptionId(subID);
	   amd.setStatus("Draft");
	   ID amdID= create(amd);
	   
	   //create rate plan
	   RatePlan rp = new RatePlan();
	   rp.setAmendmentId(amdID);
	   rp.setAmendmentType(amd.getType());
	   rp.setProductRatePlanId(prp.getId());
	   rp.setAmendmentSubscriptionRatePlanId(rpID);
	   create(rp);
	   
	   Amendment updateAmd = new Amendment();
	   updateAmd.setId(amdID);
	   updateAmd.setContractEffectiveDate(effectiveDate);
	   updateAmd.setStatus("Completed");
	   amdID = update(updateAmd);
	   print("\tDowngrade completed(amendment id:"+amdID+").");
	   return amdID;
   }
   
   private void createSubscribeWithExistingAccount(ID accountId)throws Exception{
	  ProductRatePlan prp = getProductRatePlanByProductName(getPropertyValue(PROPERTY_PRODUCT_NAME));
	  Subscription subscription = makeSubscription();

      SubscribeOptions sp = new SubscribeOptions();
      sp.setGenerateInvoice(Boolean.TRUE);
      sp.setProcessPayments(Boolean.TRUE);
      
      SubscriptionData sd = new SubscriptionData();
      sd.setSubscription(subscription);
      
      RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(prp);
      sd.setRatePlanData(subscriptionRatePlanDataArray);
      
      SubscribeRequest sub = new SubscribeRequest();
      sub.setAccount(queryAccount(accountId));
      sub.setSubscriptionData(sd);
      sub.setSubscribeOptions(sp);

      SubscribeRequest[] subscribes = new SubscribeRequest[1];
      subscribes[0] = sub;

      Subscribe sr = new Subscribe();
      sr.setSubscribes(subscribes);

      SubscribeResponse resp = stub.subscribe(sr, this.header);
      SubscribeResult[] result = resp.getResult();
      print("\t"+createMessage(result));
      Subscription sQuery = querySubscription(result[0].getSubscriptionId());
      print("\tSubscription created:"+ sQuery.getName());
   }
   
   private RatePlanData[] makeRatePlanData(ProductRatePlan ProductRatePlan) {

      RatePlanData ratePlanData = new RatePlanData();

      RatePlan ratePlan = new RatePlan();
      ratePlan.setAmendmentType("NewProduct");
      ratePlan.setProductRatePlanId(ProductRatePlan.getId());
         
      ratePlanData.setRatePlan(ratePlan);
      return new RatePlanData[]{ratePlanData};
   }

   private ProductRatePlan getProductRatePlanByProductName(String productName) throws Exception {
      ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
      query.setQueryString("select Id from Product where Name = '"+productName+"'");
      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
      if(qResult == null || qResult.getSize() == 0){
    	  print("No Product found with Name '"+productName+"'");
      }
      Product p = (Product)qResult.getRecords()[0];
      query.setQueryString("select Id,Name from ProductRatePlan where ProductId = '"+p.getId().getID()+"'");
      qResponse = stub.query(query, this.header);
      qResult = qResponse.getResult();
      ProductRatePlan rp = (ProductRatePlan)qResult.getRecords()[0];
      return rp;

   }
   
   /**
    * Creates the print format of the subscribe result value.
    * @param resultArray
    * @return
    */
   private String createMessage(SubscribeResult[] resultArray) {
      StringBuilder resultString = new StringBuilder("SusbscribeResult :\n");
      if (resultArray != null) {
         SubscribeResult result = resultArray[0];
         if (result.getSuccess()) {
            resultString.append("\n\tAccount Id: ").append(result.getAccountId())
            .append("\n\tAccount Number: ").append(result.getAccountNumber())
            .append("\n\tSubscription Id: ").append(result.getSubscriptionId())
            .append("\n\tSubscription Number: ").append(result.getSubscriptionNumber())
            .append("\n\tInvoice Number: ").append(result.getInvoiceNumber())
            .append("\n\tPayment Transaction: ").append(result.getPaymentTransactionNumber());
         } else {
            resultString.append("\nSubscribe Failure Result: \n");
            Error[] errors = result.getErrors();
            if (errors != null) {
               for (Error error : errors) {
                  resultString.append("\n\tError Code: ").append(error.getCode().toString())
                            .append("\n\tError Message: ").append(error.getMessage());                   
               }
            }
         }
      }
      return resultString.toString();
   }

   /**
    * Creates a Subscription object reading the values from the property
    * @return
    */
   private Subscription makeSubscription() {

      Calendar calendar = Calendar.getInstance();
      
      Subscription sub = new Subscription();
      sub.setName("SomeSubscription" + System.currentTimeMillis());
      sub.setTermStartDate(calendar);
      sub.setContractEffectiveDate(calendar);
      sub.setContractAcceptanceDate(calendar);
      sub.setServiceActivationDate(calendar);

      sub.setInitialTerm(12);
      sub.setRenewalTerm(12);
      sub.setNotes("This is a test subscription");

      return sub;
   }

   private PaymentMethod makePaymentMethod() {
      PaymentMethod pm = new PaymentMethod();
      pm.setType("CreditCard");
      pm.setCreditCardType("Visa");
      pm.setCreditCardAddress1("52 Vexford Lane");
      pm.setCreditCardCity("Anaheim");
      pm.setCreditCardState("California");
      pm.setCreditCardPostalCode("92808");
      pm.setCreditCardCountry("United States");
      pm.setCreditCardHolderName("Firstly Lastly");
      pm.setCreditCardExpirationYear(Calendar.getInstance().get(Calendar.YEAR)+1);
      pm.setCreditCardExpirationMonth(12);
      pm.setCreditCardNumber("4111111111111111");

      return pm;
   }

   public static Contact makeContact() {
      long time = System.currentTimeMillis();
      Contact con = new Contact();
      con.setFirstName("Firstly" + time);
      con.setLastName("Secondly" + time);
      con.setAddress1("52 Vexford Lane");
      con.setCity("Anaheim");
      con.setState("California");
      con.setCountry("United States");
      con.setPostalCode("92808");
      con.setWorkEmail("contact@test.com");
      con.setWorkPhone("4152225151");
      return con;
  }

   private Account makeAccount() {
      long time = System.currentTimeMillis();
      Account acc = new Account();
      acc.setAccountNumber("t-" + time); // string
      acc.setBatch("Batch1"); // enum
      acc.setBillCycleDay(1); // int
      acc.setAllowInvoiceEdit(true); // boolean
      acc.setAutoPay(false);
      acc.setCrmId("SFDC-" + time);
      acc.setCurrency("USD"); // standard DB enum
      acc.setCustomerServiceRepName("CSR Dude");
      acc.setName("SomeAccount" + time);
      acc.setPurchaseOrderNumber("PO-" + time);
      acc.setSalesRepName("Sales Dude");
      acc.setPaymentTerm("Due Upon Receipt");
      acc.setStatus("Draft");
      return acc;
   }
   
   private Amendment makeAmendment(String type, String subscriptionID,
			String amendmentName) {
		amendmentName = amendmentName == null ? "test_amend"
				+ System.currentTimeMillis() : amendmentName;
		type = type == null ? "NewProduct" : type;
		Amendment amd = new Amendment();
		ID id = new ID();
		id.setID(subscriptionID);
		amd.setSubscriptionId(id);
		amd.setName(amendmentName);
		amd.setEffectiveDate(Calendar.getInstance());
		amd.setType(type);
		return amd;
	}
   
   public boolean changeEndpoint() {
      return false;
   }
		
	public void loadProperties() {
		String subscribeDataFileName = System.getProperty(FILE_PROPERTY_NAME);
		try {
			properties = new Properties();
			if (subscribeDataFileName != null) {
				properties.load(new FileInputStream(subscribeDataFileName));
			}
		} catch (IOException e) {
			print("Error while reading input data file: " + subscribeDataFileName);
			print(e.getMessage());
		}
	}

	public Properties getProperties() {
		if (properties == null) {
			loadProperties();
		}
		return properties;
	}
	
	public String getPropertyValue(String propertyName) {
		return getProperties().getProperty(propertyName);
	}
	
   private String toString(Account acc) {
      StringBuilder buff = new StringBuilder();
      buff.append("\t" + acc.getId().getID());
      buff.append("\n\t" + acc.getName());
      buff.append("\n\t" + acc.getAccountNumber());
      return buff.toString();
   }
	
	public static void print(String message) {
		System.out.println(message);
	}
	public static void printHelp() {
		StringBuilder buff = new StringBuilder("The commands are:\n\t");
		buff.append("\"ant all\": run all test methods \n\t");
		buff.append("\"ant c-account\": Creates an Active Account  \n\t");
		buff.append("\"ant c-subscribe\": Creates new subscription,one-call \n\t");
		buff.append("\"ant c-subscribe-no-p\": Creates new subscription,one-call,no payments \n\t");
		buff.append("\"ant c-subscribe-w-existingAccount\": Creates new subscription on existing account \n\t");
		buff.append("\"ant c-subscribe-w-amendment\": Creates new subscription ,upgrade and downgrade \n\t");
		buff.append("\"ant cnl-subscription\": Cancel subscription \n\t");
		buff.append("\"ant c-payment\": Creates payment on invoice \n\t");
		buff.append("\"ant c-usage\": Add usage \n\t");
		print(buff.toString());
	}
	
	protected boolean isNotNull(String name) {
		return name != null && name.length() > 0;
	}
}
