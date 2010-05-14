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
	private static final String PROPERTY_ACCOUNTING_CODE = "accountingcode";

   private ZuoraServiceStub stub;
   private SessionHeader header;
   private Properties properties;

   public static void main(String[] arg) {
      
      boolean testSubscribe = (arg != null && arg.length == 1 && "subscribe".equals(arg[0]));
      
      try {
         ApiTest test = new ApiTest();

         test.login();
         if (testSubscribe){
            test.testSubscribe();
         } else {
            test.testAccountCRUD();
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
      query.setQueryString("SELECT id, name, accountnumber FROM account WHERE id = '"+accId.getID()+"'");
      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
      Account rec = (Account)qResult.getRecords()[0];
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
   
   private void testSubscribe() throws Exception {
      
      ProductRatePlanCharge charge = getChargeByAccountingCode(getPropertyValue(PROPERTY_ACCOUNTING_CODE));
      
      Account acc = makeAccount();
      Contact con = makeContact();
      PaymentMethod pm = makePaymentMethod();
      Subscription subscription = makeSubscription();

      SubscriptionData sd = new SubscriptionData();
      sd.setSubscription(subscription);
      
      RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(charge);
      sd.setRatePlanData(subscriptionRatePlanDataArray);
      
      SubscribeRequest sub = new SubscribeRequest();
      sub.setAccount(acc);
      sub.setBillToContact(con);
      sub.setPaymentMethod(pm);
      sub.setSubscriptionData(sd);

      SubscribeRequest[] subscribes = new SubscribeRequest[1];
      subscribes[0] = sub;

      Subscribe sr = new Subscribe();
      sr.setSubscribes(subscribes);

      SubscribeResponse resp = stub.subscribe(sr, this.header);
      SubscribeResult[] result = resp.getResult();
      print(createMessage(result));
      
   }
   
   private RatePlanData[] makeRatePlanData(ProductRatePlanCharge charge) {

      RatePlanData ratePlanData = new RatePlanData();

      RatePlan ratePlan = new RatePlan();
      ratePlanData.setRatePlan(ratePlan);
      ratePlan.setAmendmentType("NewProduct");
      ratePlan.setProductRatePlanId(charge.getProductRatePlanId());
         
      RatePlanChargeData ratePlanChargeData = new RatePlanChargeData();
      ratePlanData.setRatePlanChargeData(new RatePlanChargeData[]{ratePlanChargeData});
      
      RatePlanCharge ratePlanCharge = new RatePlanCharge();
      ratePlanChargeData.setRatePlanCharge(ratePlanCharge);

      ratePlanCharge.setProductRatePlanChargeId(charge.getId());
      ratePlanCharge.setQuantity(1);
      ratePlanCharge.setTriggerEvent("ServiceActivation");

      return new RatePlanData[]{ratePlanData};
   }

   private ProductRatePlanCharge getChargeByAccountingCode(String accountingCode) throws Exception {

      ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
      query.setQueryString("select Id, ProductRatePlanId from ProductRatePlanCharge where AccountingCode = '"+accountingCode+"'");
      ZuoraServiceStub.QueryResponse qResponse = stub.query(query, this.header);
      ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
      ProductRatePlanCharge rec = (ProductRatePlanCharge)qResult.getRecords()[0];
      return rec;

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
            resultString.append("\nSubscribe Result: \n")
            .append("\n\tAccount Id: ").append(result.getAccountId())
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
	
	protected boolean isNotNull(String name) {
		return name != null && name.length() > 0;
	}
}
