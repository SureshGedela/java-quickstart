/*   Copyright (c) 2012 Zuora, Inc.
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
package com.zuora.api.sample;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.Account;
import com.zuora.api.axis2.ZuoraServiceStub.Contact;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.PaymentMethod;
import com.zuora.api.axis2.ZuoraServiceStub.QueryLocator;
import com.zuora.api.axis2.ZuoraServiceStub.QueryOptions;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.axis2.ZuoraServiceStub.Usage;
import com.zuora.api.util.ZuoraAPIHelper;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class AccountSample.
 */
public class AccountSample {

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/**
	 * Test account.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createAccount() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		// Create new Account data
		Account acc1 = makeAccount();
		ID accountId = helper.create(acc1);
		boolean active = true;
		if (active) {

			// create contact
			Contact con = makeContact();
			con.setAccountId(accountId);
			ID contactId = helper.create(con);
			// create payment method
			// create payment method using credit card
			PaymentMethod pm = makePaymentMethod();
			// create payment method using ACH
			// PaymentMethod pm = makePaymentMethodACH();
			pm.setAccountId(accountId);
			ID pmId = helper.create(pm);

			// set required active fields and activate
			Account accUpdate = new Account();
			accUpdate.setId(accountId);
			accUpdate.setStatus("Active");
			accUpdate.setSoldToId(contactId);
			accUpdate.setBillToId(contactId);
			accUpdate.setAutoPay(true);
			accUpdate.setPaymentTerm("Due Upon Receipt");
			accUpdate.setDefaultPaymentMethodId(pmId);
			helper.update(accUpdate);
		}

		Account accountQuery = queryAccount(accountId);
		ZuoraUtility.print("Create Active Account");
		ZuoraUtility.print(ZuoraUtility.toString(accountQuery));
		return accountId;
	}

	/**
	 * Test update account.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateAccount() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();

		// Update Account
		Account account = queryAccountByName("ACC-1334917351457");
		Account accUpdate = new Account();
		accUpdate.setId(account.getId());
		accUpdate.setStatus("Active");
		ID accountId = helper.update(accUpdate);
		ZuoraUtility.print("Account Updated:" + accountId);
		return accountId;
	}

	/**
	 * Test delete account.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteAccount() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		boolean isDeleted = false;
		// /Delete Account
		Account account = queryAccountByName("ACC-1336111439515");
		if (account != null) {
			isDeleted = helper.delete("Account", account.getId());
		}
		ZuoraUtility.print("Account Deleted: " + isDeleted);
		return isDeleted;
	}

	/**
	 * Update payment method.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void updatePaymentMethod() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		Account account = queryAccountByName("SomeAccount1334747203879");
		PaymentMethod paymentMethod = queryPaymentMethod(account.getId());
		paymentMethod.setCreditCardExpirationYear(Calendar.getInstance().get(Calendar.YEAR) + 5);
		ID pmid = helper.update(paymentMethod);
		ZuoraUtility.print("PaymentMethod ID: " + pmid);
	}

	/**
	 * Delete payment method.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void deletePaymentMethod() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		Account account = queryAccountByName("SomeAccount1334747203879");
		PaymentMethod paymentMethod = queryPaymentMethod(account.getId());
		helper.delete("PaymentMethod", paymentMethod.getId());
	}

	/**
	 * Creates the usage.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createUsage() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		Usage usage = new Usage();
		Account account = queryAccountByName("ACC-1334899124957");
		RatePlanCharge charge = queryRatePlanCharge("PRPC-1335426631281");
		usage.setAccountId(account.getId());
		usage.setQuantity(new BigDecimal(1));
		usage.setStartDateTime(ZuoraUtility.getCurrentDate());
		usage.setEndDateTime(ZuoraUtility.getNextMonth());
		usage.setUOM("Each");
		usage.setDescription("Test Usage");
		usage.setChargeId(charge.getId());
		ID usageId = helper.create(usage);
		ZuoraUtility.print("Usage Id : " + usageId);
		return usageId;
	}

	/**
	 * Update usage.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateUsage() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		Account account = queryAccountByName("ACC-1334899124957");
		Usage usage = queryUsageByAccount(account.getId());
		Usage usage2 = new Usage();
		usage2.setId(usage.getId());
		usage2.setQuantity(new BigDecimal(2));
		ID usageId = helper.update(usage2);
		ZuoraUtility.print("Usage ID:" + usageId);
		return usageId;
	}

	/**
	 * Delete usage.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteUsage() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		Account account = queryAccountByName("ACC-1334899124957");
		Usage usage = queryUsageByAccount(account.getId());
		boolean isDeleted = helper.delete("Usage", usage.getId());
		return isDeleted;
	}

	/**
	 * Query usage.
	 * 
	 * @param accountid
	 *          the account id
	 * @return the usage
	 * @throws Exception
	 *           the exception
	 */
	public Usage queryUsageByAccount(ID accountid) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id, AccountId, SourceType, UOM, Quantity, "
		    + "RbeStatus, StartDateTime, EndDateTime, SourceName, SubmissionDateTime" + " FROM Usage WHERE AccountId = '"
		    + accountid + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Usage usage = (Usage) qResult.getRecords()[0];
		return usage;
	}

	/**
	 * Query usage.
	 * 
	 * @param usageid
	 *          the usageid
	 * @return the usage
	 * @throws Exception
	 *           the exception
	 */
	public Usage queryUsage(ID usageid) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id, AccountId, SourceType, UOM, Quantity, "
		    + "RbeStatus, StartDateTime, EndDateTime, SourceName, SubmissionDateTime" + " FROM Usage WHERE id = '"
		    + usageid + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Usage usage = (Usage) qResult.getRecords()[0];
		return usage;
	}

	/**
	 * Query rate plan charge.
	 * 
	 * @param name
	 *          the name
	 * @return the rate plan charge
	 * @throws Exception
	 *           the exception
	 */
	public RatePlanCharge queryRatePlanCharge(String name) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id FROM RatePlanCharge WHERE Name = '" + name + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		RatePlanCharge charge = (RatePlanCharge) qResult.getRecords()[0];
		return charge;
	}

	/**
	 * Query payment method.
	 * 
	 * @param accountid
	 *          the account id
	 * @return the payment method
	 * @throws Exception
	 *           the exception
	 */
	public PaymentMethod queryPaymentMethod(ID accountid) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM PaymentMethod WHERE AccountId = '" + accountid + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		PaymentMethod pm = (PaymentMethod) qResult.getRecords()[0];
		return pm;
	}

	/**
	 * Query account.
	 * 
	 * @param accId
	 *          the account id
	 * @return the account
	 * @throws Exception
	 *           the exception
	 */
	public Account queryAccount(ID accId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, name, accountnumber FROM account WHERE id = '" + accId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Account acc = (Account) qResult.getRecords()[0];
		return acc;
	}

	/**
	 * Query account by name.
	 * 
	 * @param accountName
	 *          the account name
	 * @return the account
	 * @throws Exception
	 *           the exception
	 */
	public Account queryAccountByName(String accountName) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, name, accountnumber FROM account WHERE name = '" + accountName + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Account acc = (Account) qResult.getRecords()[0];
		return acc;
	}

	/**
	 * Make account.
	 * 
	 * @return the account
	 */
	public Account makeAccount() {
		long time = System.currentTimeMillis();
		Account acc = new Account();
		acc.setAccountNumber("T-" + time); // string
		acc.setBatch("Batch1"); // enum
		acc.setBillCycleDay(1); // int
		acc.setAllowInvoiceEdit(true); // boolean
		acc.setAutoPay(false);
		acc.setCrmId("SFDC-" + time);
		acc.setCurrency("USD"); // standard DB enum
		acc.setCustomerServiceRepName("CSR Dude");
		acc.setName("ACC-" + time);
		acc.setPurchaseOrderNumber("PO-" + time);
		acc.setSalesRepName("Sales Dude");
		acc.setPaymentTerm("Due Upon Receipt");
		acc.setStatus("Draft");
		return acc;
	}

	/**
	 * Make contact.
	 * 
	 * @return the contact
	 */
	public Contact makeContact() {
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

	/**
	 * Make payment method.
	 * 
	 * @return the payment method
	 */
	public PaymentMethod makePaymentMethod() {
		PaymentMethod pm = new PaymentMethod();
		pm.setType("CreditCard");
		pm.setCreditCardType("Visa");
		pm.setCreditCardAddress1("52 Vexford Lane");
		pm.setCreditCardCity("Anaheim");
		pm.setCreditCardState("California");
		pm.setCreditCardPostalCode("92808");
		pm.setCreditCardCountry("United States");
		pm.setCreditCardHolderName("Firstly Lastly");
		pm.setCreditCardExpirationYear(Calendar.getInstance().get(Calendar.YEAR) + 1);
		pm.setCreditCardExpirationMonth(12);
		pm.setCreditCardNumber("4111111111111111");
		return pm;
	}

	/**
	 * Make payment method ach.
	 * 
	 * @return the payment method
	 */
	public PaymentMethod makePaymentMethodACH() {
		PaymentMethod pm = new PaymentMethod();
		pm.setType("ACH");
		pm.setAchAbaCode("123123123");
		pm.setAchAccountName("testAccountName");
		pm.setAchAccountNumber("23232323232323");
		pm.setAchAccountType("Saving");
		pm.setAchBankName("Test Bank");
		pm.setCreatedDate(Calendar.getInstance());
		return pm;
	}

	/**
	 * Test query more.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void queryMore() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		// construct a query call
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.setBatchSize(2);
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Invoice");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, queryOptions, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ZuoraUtility.printQueryResult(qResult);
		ZuoraServiceStub.QueryMore queryMore = new ZuoraServiceStub.QueryMore();
		while (!qResult.getDone()) {
			// Get the QueryLocator to Query more records
			QueryLocator queryLocator = qResult.getQueryLocator();
			queryMore.setQueryLocator(queryLocator);
			queryOptions.setBatchSize(10);
			ZuoraServiceStub.QueryMoreResponse qMoreResponse = stub.queryMore(queryMore, queryOptions, this.header);
			qResult = qMoreResponse.getResult();
			// Print query results
			ZuoraUtility.printQueryResult(qResult);
		}

	}

}
