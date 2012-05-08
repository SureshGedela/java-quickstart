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

import java.util.Calendar;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.Account;
import com.zuora.api.axis2.ZuoraServiceStub.Contact;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.PaymentMethod;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlan;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanChargeData;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanData;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.axis2.ZuoraServiceStub.Subscribe;
import com.zuora.api.axis2.ZuoraServiceStub.SubscribeRequest;
import com.zuora.api.axis2.ZuoraServiceStub.SubscribeResponse;
import com.zuora.api.axis2.ZuoraServiceStub.SubscribeResult;
import com.zuora.api.axis2.ZuoraServiceStub.Subscription;
import com.zuora.api.axis2.ZuoraServiceStub.SubscriptionData;
import com.zuora.api.util.ZuoraAPIHelper;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class SubscribeSample.
 */
public class SubscribeSample {

	/** The Constant PROPERTY_ACCOUNTING_CODE. */
	private static final String PROPERTY_ACCOUNTING_CODE = "accountingcode";

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/**
	 * Test subscribe.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createSubscribe() throws Exception {
		ZuoraUtility utility = new ZuoraUtility();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();

		ProductRatePlanCharge charge = getChargeByAccountingCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));
		AccountSample testData = new AccountSample();
		Account acc = testData.makeAccount();
		Contact con = testData.makeContact();
		PaymentMethod pm = testData.makePaymentMethod();
		Subscription subscription = makeSubscription();

		SubscriptionData sd = new SubscriptionData();
		sd.setSubscription(subscription);

		RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(charge);
		sd.setRatePlanData(subscriptionRatePlanDataArray);

		SubscribeRequest subReq = new SubscribeRequest();
		subReq.setAccount(acc);
		subReq.setBillToContact(con);
		subReq.setPaymentMethod(pm);
		subReq.setSubscriptionData(sd);

		SubscribeRequest[] subscribes = new SubscribeRequest[1];
		subscribes[0] = subReq;

		Subscribe subscribe = new Subscribe();
		subscribe.setSubscribes(subscribes);
		SubscribeResponse resp = stub.subscribe(subscribe, this.header);
		SubscribeResult result = resp.getResult()[0];
		if (result.getErrors() == null) {
			ZuoraUtility.print("Subscribe status : Success");

		}
		else {
			ZuoraUtility.print("Subscribe status: Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(result));
		}
		return result.getSubscriptionId();
	}

	/**
	 * Gets the charge by accounting code.
	 * 
	 * @param accountingCode
	 *          the accounting code
	 * @return the charge by accounting code
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlanCharge getChargeByAccountingCode(String accountingCode) throws Exception {

		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("select Id, ProductRatePlanId from ProductRatePlanCharge where AccountingCode = '"
		    + accountingCode + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlanCharge rec = (ProductRatePlanCharge) qResult.getRecords()[0];
		return rec;

	}

	/**
	 * Creates a Subscription object reading the values from the property.
	 * 
	 * @return Subscription
	 */
	private Subscription makeSubscription() {

		Subscription sub = new Subscription();
		sub.setName("SUB-" + System.currentTimeMillis());
		sub.setTermStartDate(ZuoraUtility.getCurrentDate());
		// set ContractEffectiveDate = current date to generate invoice
		// Generates invoice at the time of subscription creation. uncomment for
		// invoice generation
		// sub.setContractEffectiveDate(ZuoraUtility.getCurrentDate());
		sub.setContractAcceptanceDate(ZuoraUtility.getCurrentDate());
		sub.setServiceActivationDate(ZuoraUtility.getCurrentDate());
		// set IsInvoiceSeparate=true //To generate invoice separate for every
		// subscription
		sub.setIsInvoiceSeparate(true);
		sub.setAutoRenew(true);
		sub.setInitialTerm(12);// sets value for next renewal date
		sub.setRenewalTerm(12);
		sub.setNotes("This is a test subscription");
		return sub;
	}

	/**
	 * Make rate plan data.
	 * 
	 * @param charge
	 *          the charge
	 * @return the rate plan data[]
	 */
	public RatePlanData[] makeRatePlanData(ProductRatePlanCharge charge) {
		RatePlanData ratePlanData = new RatePlanData();
		RatePlan ratePlan = new RatePlan();
		ratePlanData.setRatePlan(ratePlan);
		// ratePlan.setAmendmentType("NewProduct");
		ratePlan.setProductRatePlanId(charge.getProductRatePlanId());

		RatePlanChargeData ratePlanChargeData = new RatePlanChargeData();
		ratePlanData.setRatePlanChargeData(new RatePlanChargeData[] { ratePlanChargeData });

		RatePlanCharge ratePlanCharge = new RatePlanCharge();
		ratePlanChargeData.setRatePlanCharge(ratePlanCharge);

		ratePlanCharge.setProductRatePlanChargeId(charge.getId());
		// ratePlanCharge.setQuantity(new BigDecimal(1));
		ratePlanCharge.setTriggerEvent("ServiceActivation");
		return new RatePlanData[] { ratePlanData };
	}

	/**
	 * Test subscribe with existing account.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createSubscribeWithExistingAccount() throws Exception {

		ZuoraUtility utility = new ZuoraUtility();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();

		ProductRatePlanCharge charge = getChargeByAccountingCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));
		Account account = getAccountByName("ACC-1336382492856");
		Subscription subscription = makeSubscription();

		SubscriptionData sd = new SubscriptionData();
		sd.setSubscription(subscription);

		RatePlanData[] subscriptionRatePlanDataArray = makeRatePlanData(charge);
		sd.setRatePlanData(subscriptionRatePlanDataArray);

		SubscribeRequest subRequest = new SubscribeRequest();
		subRequest.setAccount(account);
		subRequest.setSubscriptionData(sd);

		SubscribeRequest[] subscribes = new SubscribeRequest[1];
		subscribes[0] = subRequest;

		Subscribe sr = new Subscribe();
		sr.setSubscribes(subscribes);

		SubscribeResponse resp = stub.subscribe(sr, this.header);
		SubscribeResult result = resp.getResult()[0];
		if (result.getErrors() == null) {
			ZuoraUtility.print("Subscribe status : Success");
		}
		else {
			ZuoraUtility.print("Subscribe status: Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(result));
		}
		return result.getSubscriptionId();
	}

	/**
	 * Update subscribe.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateSubscribe() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		ID id = null;
		Subscription subscription = querySubscriptionByName("SUB-1336383000027");
		if (subscription != null) {
			subscription.setContractEffectiveDate(Calendar.getInstance());
			id = helper.update(subscription);
			ZuoraUtility.print("SubscribeId :" + id);
		}
		return id;
	}

	/**
	 * Delete subscribe.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteSubscribe() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		boolean isDeleted = false;
		Subscription subscription = querySubscriptionByName("SUB-1336125236281");
		if (subscription != null) {
			isDeleted = helper.delete("Subscription", subscription.getId());
		}
		ZuoraUtility.print("Subscribtion isDeleted :" + isDeleted);
		return isDeleted;
	}

	/**
	 * Query subscription by name.
	 * 
	 * @param name
	 *          the name
	 * @return the subscription
	 * @throws Exception
	 *           the exception
	 */
	public Subscription querySubscriptionByName(String name) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id,AccountId,Name FROM Subscription where Name = '" + name + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Subscription sub = (Subscription) qResult.getRecords()[0];
		return sub;
	}

	/**
	 * Query subscription.
	 * 
	 * @param subscribeId
	 *          the subscribe id
	 * @return the subscription
	 * @throws Exception
	 *           the exception
	 */
	public Subscription querySubscription(ID subscribeId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id,AccountId,Name FROM Subscription where id = '" + subscribeId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Subscription sub = (Subscription) qResult.getRecords()[0];
		return sub;
	}

	/**
	 * Gets the account by name.
	 * 
	 * @param accountName
	 *          the account name
	 * @return the account by name
	 * @throws Exception
	 *           the exception
	 */
	public Account getAccountByName(String accountName) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, name, accountnumber FROM account WHERE name = '" + accountName + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Account rec = (Account) qResult.getRecords()[0];
		return rec;
	}

}
