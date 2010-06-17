package com.zuora.api;

import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.*;
import com.zuora.api.axis2.ZuoraServiceStub.Error;

public class ZuoraInterface {

   public static String USERNAME = "<your username>";
   public static String PASSWORD = "<your password>";
   public static String ENDPOINT = "https://www.zuora.com/apps/services/a/17.0";

   private static Boolean GENERATE_INVOICE = false;
   private static Boolean PROCESS_PAYMENTS = false;

   protected ZuoraServiceStub binding;
   private SessionHeader header;

   public ZuoraInterface() throws AxisFault {
       this(USERNAME, PASSWORD, ENDPOINT);
   }

   public ZuoraInterface(String username, String password, String endpoint) throws AxisFault {

      // create a new instance of the web service proxy class
      binding = new ZuoraServiceStub();
      if (endpoint != null && endpoint.trim().length() > 0){
         ServiceClient client = binding._getServiceClient();
         client.getOptions().getTo().setAddress(endpoint);
      }

      login(username, password);

   }

   protected boolean login(String username, String password) {

      try {
         // execute the login placing the results
         // in a LoginResult object
         Login loginRequest = new Login();
         loginRequest.setUsername(username);
         loginRequest.setPassword(password);
         LoginResponse loginResponse = binding.login(loginRequest);

         // set the session id header for subsequent calls
         header = new SessionHeader();
         header.setSession(loginResponse.getResult().getSession());

         // reset the endpoint url to that returned from login
         // binding.Url = loginResult.ServerUrl;

         print("Session: " + loginResponse.getResult().getSession());
         print("ServerUrl: " + loginResponse.getResult().getServerUrl());

         return true;
      }
      catch (Exception ex) {
         // Login failed, report message then return false
         print("Login failed with message: " + ex.getMessage());
         return false;
      }
   }

   protected String create(ZObject acc) throws Exception {
      Create req = new Create();
      req.setZObjects(new ZObject[] { acc });
      SaveResult[] result = binding.create(req, header).getResult();
      return result[0].getId().getID();
   }

   protected String update(ZObject acc) throws Exception {
      Update req = new Update();
      req.setZObjects(new ZObject[] { acc });
      SaveResult[] result = binding.update(req, header).getResult();
      return result[0].getId().getID();
   }

   protected boolean delete(String type, ID id) throws Exception {

      Delete req = new Delete();
      req.setType(type);
      req.setIds(new ID[] { id });

      DeleteResult[] result = binding.delete(req, header).getResult();
      return result[0].getSuccess();

   }

   protected QueryResult query(String zoql) throws Exception {

      Query query = new Query();
      query.setQueryString(zoql);
      return binding.query(query, header).getResult();

   }
   
   protected Account queryAccount(String accId) throws Exception {
      QueryResult qResult = query("SELECT id, name, accountnumber FROM account WHERE id = '" + accId + "'");
      Account rec = (Account) qResult.getRecords()[0];
      return rec;
   }

   // BUGBUG: deal with expiration later
   public Product[] queryProducts() throws Exception {

      QueryResult result = query("select Id, Name FROM Product");
      ZObject[] recs = (ZObject[]) result.getRecords();
      Product[] res = null;
      if (result.getSize() > 0 && recs != null && recs.length > 0) {
         res = new Product[recs.length];
         for (int i = 0; i < recs.length; i++) {
            res[i] = (Product) recs[i];
         }

      }
      return res;

   }

   // BUGBUG: deal with expiration later
   public ProductRatePlan[] queryRatePlansByProduct(String productId) throws Exception {

      QueryResult result = query("select Id, Name FROM ProductRatePlan where ProductId = '" + productId + "'");
      ZObject[] recs = (ZObject[]) result.getRecords();
      ProductRatePlan[] res = null;
      if (result.getSize() > 0 && recs != null && recs.length > 0) {
         res = new ProductRatePlan[recs.length];
         for (int i = 0; i < recs.length; i++) {
            res[i] = (ProductRatePlan) recs[i];
         }

      }
      return res;

   }

   private static String CHARGE_SELECT_LIST = "Id, Name, AccountingCode, DefaultQuantity, Type, Model, ProductRatePlanId";

   // BUGBUG: deal with expiration later
   public ProductRatePlanCharge[] queryChargesByProductRatePlan(String prpId) throws Exception {

      QueryResult result = query("select " + CHARGE_SELECT_LIST + " from ProductRatePlanCharge where ProductRatePlanId = '" + prpId + "'");
      ZObject[] recs = (ZObject[]) result.getRecords();
      ProductRatePlanCharge[] res = null;
      if (result.getSize() > 0 && recs != null && recs.length > 0) {
         res = new ProductRatePlanCharge[recs.length];
         for (int i = 0; i < recs.length; i++) {
            res[i] = (ProductRatePlanCharge) recs[i];
         }

      }
      return res;

   }

   public ProductRatePlanCharge queryChargeById(String id) throws Exception {

      QueryResult result = query("select " + CHARGE_SELECT_LIST + " from ProductRatePlanCharge where Id = '" + id + "'");
      ProductRatePlanCharge rec = (ProductRatePlanCharge) result.getRecords()[0];
      return rec;

   }

   public ProductRatePlanCharge queryChargeByAccountingCode(String accountingCode) throws Exception {

      QueryResult result = query("select " + CHARGE_SELECT_LIST + " from ProductRatePlanCharge where AccountingCode = '" + accountingCode + "'");
      ProductRatePlanCharge rec = (ProductRatePlanCharge) result.getRecords()[0];
      return rec;

   }

   protected Account makeAccount(String Name, String CurrencyIso) {
      Account acc = new Account();
      acc.setName(Name);
      acc.setCurrency(CurrencyIso);
      acc.setStatus("Draft");
      acc.setPaymentTerm("Due Upon Receipt");
      acc.setBatch("Batch1");
      acc.setBillCycleDay(1);
      acc.setAllowInvoiceEdit(true);
      acc.setAutoPay(false);

      // uncomment if you want to specify your own account number
      // acc.AccountNumber = "ACC-" + DateTime.Now.Ticks;

      // optional values
      // acc.CustomerServiceRepName = "CSR Dude";
      // acc.SalesRepName = "Sales Dude";
      // acc.PurchaseOrderNumber = "PO-" + time;
      // acc.CrmId = "SFDC-" + time;
      return acc;
   }

   protected Contact makeContact(String FirstName, String LastName, String WorkEmail, String WorkPhone, String Address1, String Address2, String City, String State, String Country, String PostalCode) {
      Contact con = new Contact();
      con.setFirstName(FirstName);
      con.setLastName(LastName);
      con.setAddress1(Address1);
      con.setCity(City);
      con.setState(State);
      con.setCountry(Country);
      con.setPostalCode(PostalCode);
      con.setWorkEmail(WorkEmail);
      con.setWorkPhone(WorkPhone);
      return con;
   }

   /**
    * Creates a Subscription object reading the values from the property
    * 
    * @return
    */
   protected Subscription makeSubscription(String subscriptionName, String subscriptionNotes) {

      Calendar calendar = Calendar.getInstance();

      Subscription sub = new Subscription();
      sub.setName(subscriptionName);
      sub.setNotes(subscriptionNotes);

      sub.setTermStartDate(calendar);
      sub.setContractEffectiveDate(calendar);
      sub.setContractAcceptanceDate(calendar);
      sub.setServiceActivationDate(calendar);

      sub.setInitialTerm(12);
      sub.setRenewalTerm(12);

      return sub;
   }

   protected RatePlanData[] makeRatePlanData(ProductRatePlanCharge[] charges) {

      RatePlanData[] data = new RatePlanData[charges.length];

      for (int i = 0; i < charges.length; i++) {
         ProductRatePlanCharge charge = charges[i];
         RatePlanData ratePlanData = new RatePlanData();

         RatePlan ratePlan = new RatePlan();
         ratePlanData.setRatePlan(ratePlan);
         ratePlan.setAmendmentType("NewProduct");
         ratePlan.setProductRatePlanId(charge.getProductRatePlanId());

         RatePlanChargeData ratePlanChargeData = new RatePlanChargeData();
         ratePlanData.setRatePlanChargeData(new RatePlanChargeData[] { ratePlanChargeData });

         RatePlanCharge ratePlanCharge = new RatePlanCharge();
         ratePlanChargeData.setRatePlanCharge(ratePlanCharge);

         ratePlanCharge.setProductRatePlanChargeId(charge.getId());
         // if it has a default quantity, default to 1
         if (charge.getDefaultQuantity() > 0) {
            ratePlanCharge.setQuantity(1);
         }
         ratePlanCharge.setTriggerEvent("ServiceActivation");

         data[i] = ratePlanData;
      }

      return data;
   }

   protected PaymentMethod makePaymentMethod(String HolderName, String Address, String City, String State, String Country, String PostalCode, String CreditCardType, String CreditCardNumber,
      int CreditCardExpirationMonth, int CreditCardExpirationYear)

   {
      PaymentMethod pm = new PaymentMethod();
      pm.setType("CreditCard");
      pm.setCreditCardType(CreditCardType);
      pm.setCreditCardAddress1(Address);
      pm.setCreditCardCity(City);
      pm.setCreditCardState(State);
      pm.setCreditCardPostalCode(PostalCode);
      pm.setCreditCardCountry(Country);
      pm.setCreditCardHolderName(HolderName);
      pm.setCreditCardExpirationYear(CreditCardExpirationYear);
      pm.setCreditCardExpirationMonth(CreditCardExpirationMonth);
      pm.setCreditCardNumber(CreditCardNumber);

      return pm;
   }

   private SubscribeResult subscribe(String SubscriptionName, ProductRatePlanCharge[] charges, String Name, String FirstName, String LastName, String WorkEmail, String WorkPhone, String Address1,
      String Address2, String City, String State, String Country, String PostalCode, String CreditCardType, String CreditCardNumber, String CreditCardHolderName, int CreditCardExpirationMonth,
      int CreditCardExpirationYear) throws Exception {

      Account acc = makeAccount(Name, "USD");
      Contact con = makeContact(FirstName, LastName, WorkEmail, WorkPhone, Address1, Address2, City, State, Country, PostalCode);

      PaymentMethod pm = makePaymentMethod(CreditCardHolderName, Address1, City, State, Country, PostalCode, CreditCardType, CreditCardNumber, CreditCardExpirationMonth, CreditCardExpirationYear);
      Subscription subscription = makeSubscription(SubscriptionName, null);

      SubscriptionData sd = new SubscriptionData();
      sd.setSubscription(subscription);

      RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(charges);
      sd.setRatePlanData(subscriptionRatePlanDataArray);

      SubscribeOptions options = new SubscribeOptions();
      options.setGenerateInvoice(GENERATE_INVOICE);
      options.setProcessPayments(PROCESS_PAYMENTS);

      SubscribeRequest sub = new SubscribeRequest();
      sub.setAccount(acc);
      sub.setBillToContact(con);
      sub.setPaymentMethod(pm);
      sub.setSubscriptionData(sd);
      sub.setSubscribeOptions(options);

      SubscribeRequest[] subscribes = new SubscribeRequest[1];
      subscribes[0] = sub;

      Subscribe subscribeRequest = new Subscribe();
      subscribeRequest.setSubscribes(subscribes);
      
      SubscribeResult[] result = binding.subscribe(subscribeRequest, header).getResult();

      return result[0];

   }

   /**
    * Creates the print format of the subscribe result value.
    * 
    * @param resultArray
    * @return
    */
   public static String createMessage(SubscribeResult result) {
      String resultString = null;
      if (result != null) {
         if (result.getSuccess()) {
            resultString = resultString + "<b>Subscribe Result: Success</b>";
            resultString = resultString + "<br>&nbsp;&nbsp;Account Id: " + result.getAccountId();
            resultString = resultString + "<br>&nbsp;&nbsp;Account Number: " + result.getAccountNumber();
            resultString = resultString + "<br>&nbsp;&nbsp;Subscription Id: " + result.getSubscriptionId();
            resultString = resultString + "<br>&nbsp;&nbsp;Subscription Number: " + result.getSubscriptionNumber();
            resultString = resultString + "<br>&nbsp;&nbsp;Invoice Number: " + result.getInvoiceNumber();
            resultString = resultString + "<br>&nbsp;&nbsp;Payment Transaction: " + result.getPaymentTransactionNumber();
         }
         else {
            resultString = resultString + "<b>Subscribe Result: Failed</b>";
            Error[] errors = result.getErrors();
            if (errors != null) {
               for (Error error : errors) {
                  resultString = resultString + "<br>&nbsp;&nbsp;Error Code: " + error.getCode().getValue();
                  resultString = resultString + "<br>&nbsp;&nbsp;Error Message: " + error.getMessage();
               }
            }
         }
      }
      return resultString;
   }

   public static Boolean isValidId(String id) {
      return id != null && id.length() == 32;
   }

   protected void print(String p) {
      System.out.print(p);
   }

}
