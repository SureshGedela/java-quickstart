package com.zuora.api.sample;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.Amend;
import com.zuora.api.axis2.ZuoraServiceStub.AmendOptions;
import com.zuora.api.axis2.ZuoraServiceStub.AmendRequest;
import com.zuora.api.axis2.ZuoraServiceStub.AmendResponse;
import com.zuora.api.axis2.ZuoraServiceStub.AmendResult;
import com.zuora.api.axis2.ZuoraServiceStub.Amendment;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.PreviewOptions;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlan;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlan;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanChargeData;
import com.zuora.api.axis2.ZuoraServiceStub.RatePlanData;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.axis2.ZuoraServiceStub.Subscription;
import com.zuora.api.util.ZuoraAPIHelper;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class AmendmentSample.
 */
public class AmendmentSample {

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/** The Constant PROPERTY_ACCOUNTING_CODE. */
	private static final String PROPERTY_ACCOUNTING_CODE = "accountingcode";

	/**
	 * Creates the amendment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createAmendment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336124321437");

		Amendment amendment = new Amendment();
		amendment.setDescription("Test Amendment");
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("TermsAndConditions");// Change value for type of
																						// Amendment

		ID amendmentId = helper.create(amendment);
		ZuoraUtility.print("AmendmentId: " + amendmentId);
		// Query amendment
		Amendment amendmentQuery = queryAmendment(amendmentId);
		ZuoraUtility.print("Amendment Queried: " + amendmentQuery.getName());
		return amendmentId;
	}

	/**
	 * Creates the amendment for new product.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createAmendmentForNewProduct() throws Exception {
		SubscribeSample subscribeData = new SubscribeSample();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		ZuoraUtility utility = new ZuoraUtility();
		Subscription subscription = getSubscriptionByName("SUB-1334917426051");
		ZuoraUtility.print("subscription :" + subscription);

		Calendar calendar = Calendar.getInstance();
		Amendment amendment = new Amendment();

		amendment.setDescription("Amendment for NewProduct");
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setSubscriptionId(subscription.getId());
		amendment.setTermStartDate(calendar);
		amendment.setType("NewProduct");
		amendment.setEffectiveDate(calendar);

		ProductRatePlanCharge charge = queryChargeByAccountingCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));
		RatePlanData[] ratePlanData = subscribeData.makeRatePlanData(charge);
		amendment.setRatePlanData(ratePlanData[0]);

		ID amendmentId = helper.create(amendment);
		ZuoraUtility.print("Created AmendmentId: " + amendmentId);
		return amendmentId;
	}

	/**
	 * Update amendment for tnc.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateAmendmentForTnC() throws Exception {
		// Update Amendment
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Calendar currentDate = Calendar.getInstance();
		ID amendmentUpdateId = null;
		Amendment amendmentUpdate = queryAmendmentByName("AMEND-1336127091406");
		if (amendmentUpdate != null) {
			amendmentUpdate.setDescription("Amendment updated for TnC");
			amendmentUpdate.setContractEffectiveDate(ZuoraUtility.getNextMonth());
			amendmentUpdate.setStatus("Completed");
			// For Terms And Conditions Amendment: initial term,renewal term,term
			// start date are required.
			amendmentUpdate.setInitialTerm(3);
			amendmentUpdate.setRenewalTerm(3);
			amendmentUpdate.setTermStartDate(currentDate);
			amendmentUpdateId = helper.update(amendmentUpdate);
			ZuoraUtility.print("AmendmentUpdateId :" + amendmentUpdateId);

		}
		return amendmentUpdateId;
	}

	/**
	 * Update amendment for new product.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void updateAmendmentForNewProduct() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		// Get Amendment by Name
		Amendment amendment = queryAmendmentByName("Amendment NewProduct");
		// Get ProductRatePlan by Name
		ProductRatePlan productRatePlan = queryProductRatePlanByName("New Rate Plan");
		RatePlan ratePlan = new RatePlan();
		ratePlan.setAmendmentId(amendment.getId());
		ratePlan.setAmendmentType("NewProduct");
		ratePlan.setProductRatePlanId(productRatePlan.getId());
		ID ratePlanId = helper.create(ratePlan);
		ZuoraUtility.print("ratePlanId: " + ratePlanId);
	}

	/**
	 * Activate amendment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateAmendment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Amendment amendmentUpdate = queryAmendmentByName("AMEND-1336127091406");
		// Contract effective date must be on or after the most recent invoice date
		amendmentUpdate.setContractEffectiveDate(ZuoraUtility.getNextMonth());
		amendmentUpdate.setStatus("Completed");
		ID amendmentUpdateId = helper.update(amendmentUpdate);
		ZuoraUtility.print("Amendment Activte Id :" + amendmentUpdateId);
		return amendmentUpdateId;
	}

	/**
	 * Delete amendment.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteAmendment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		boolean isDeleted = false;
		// query to delete Amendment
		Amendment amendment = queryAmendmentByName("AMEND-1336127091406");
		if (amendment != null) {
			isDeleted = helper.delete("Amendment", amendment.getId());
		}

		ZuoraUtility.print("Amendment isDeleted: " + isDeleted);
		return isDeleted;
	}

	/**
	 * Creates the amendment for update product.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createAmendmentForUpdateProduct() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336383000027");
		Calendar calendar = Calendar.getInstance();
		Amendment amendment = new Amendment();
		amendment.setDescription("Amendment for UpdateProduct");
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setSubscriptionId(subscription.getId());
		amendment.setTermStartDate(calendar);
		amendment.setType("UpdateProduct");
		amendment.setEffectiveDate(calendar);
		ID amendmentId = helper.create(amendment);
		ZuoraUtility.print("AmendmentId: " + amendmentId);
		return amendmentId;
	}

	/**
	 * Update amendment for update product.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void updateAmendmentForUpdateProduct() throws Exception {
		ZuoraUtility utility = new ZuoraUtility();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		RatePlanCharge charge = getRatePlanChargeByAccCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));
		RatePlanCharge charge2 = new RatePlanCharge();
		charge2.setId(charge.getId());
		charge2.setQuantity(new BigDecimal(1));
		charge2.setPrice(new BigDecimal(1));
		charge2.setDescription("Test charge updated by 1");
		ID chargeId = helper.update(charge);
		ZuoraUtility.print("Updated chargeId :" + chargeId);
	}

	/**
	 * Query amendment.
	 * 
	 * @param amendmentId
	 *          the amendment id
	 * @return the amendment
	 * @throws Exception
	 *           the exception
	 */
	public Amendment queryAmendment(ID amendmentId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, name FROM amendment WHERE id = '" + amendmentId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Amendment amendment = (Amendment) qResult.getRecords()[0];
		return amendment;
	}

	/**
	 * Query amendment by name.
	 * 
	 * @param amendmentName
	 *          the amendment name
	 * @return the amendment
	 * @throws Exception
	 *           the exception
	 */
	public Amendment queryAmendmentByName(String amendmentName) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, name FROM amendment WHERE Name = '" + amendmentName + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Amendment amendment = (Amendment) qResult.getRecords()[0];
		return amendment;
	}

	/**
	 * Query product rate plan by name.
	 * 
	 * @param productPlanName
	 *          the product plan name
	 * @return the product rate plan
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlan queryProductRatePlanByName(String productPlanName) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, Name FROM ProductRatePlan  WHERE Name = '" + productPlanName + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlan rec = (ProductRatePlan) qResult.getRecords()[0];
		return rec;
	}

	/**
	 * Gets the rate plan charge by acc code.
	 * 
	 * @param accountingCode
	 *          the accounting code
	 * @return the rate plan charge by acc code
	 * @throws Exception
	 *           the exception
	 */
	public RatePlanCharge getRatePlanChargeByAccCode(String accountingCode) throws Exception {

		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("select Id, Quantity , Price from RatePlanCharge where AccountingCode = '" + accountingCode
		    + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		RatePlanCharge rec = (RatePlanCharge) qResult.getRecords()[0];
		return rec;

	}

	/**
	 * Query rate plan by name.
	 * 
	 * @param ratePlanName
	 *          the rate plan name
	 * @param id
	 *          the id
	 * @return the rate plan
	 * @throws Exception
	 *           the exception
	 */
	public RatePlan queryRatePlanByName(String ratePlanName, ID id) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, Name , AmendmentSubscriptionRatePlanId " + "FROM RatePlan  WHERE Name = '"
		    + ratePlanName + "' " + "and Amendmentid='" + id + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		RatePlan rateplan = (RatePlan) qResult.getRecords()[0];
		return rateplan;
	}

	/**
	 * Gets the subscription by name.
	 * 
	 * @param name
	 *          the name
	 * @return the subscription by name
	 * @throws Exception
	 *           the exception
	 */
	public Subscription getSubscriptionByName(String name) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id,AccountId,Name FROM Subscription where Name = '" + name + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Subscription sub = (Subscription) qResult.getRecords()[0];
		return sub;
	}

	/**
	 * Query charge by accounting code.
	 * 
	 * @param accountingCode
	 *          the accounting code
	 * @return the product rate plan charge
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlanCharge queryChargeByAccountingCode(String accountingCode) throws Exception {

		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("select Id, ProductRatePlanId from ProductRatePlanCharge where AccountingCode = '"
		    + accountingCode + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlanCharge rec = (ProductRatePlanCharge) qResult.getRecords()[0];
		return rec;

	}

	/**
	 * Test amend.
	 * 
	 * @return the i d[]
	 * @throws Exception
	 *           the exception
	 */
	public ID[] amend() throws Exception {

		ZuoraUtility utility = new ZuoraUtility();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336383000027");
		Amend amend = new Amend();
		Amendment amendment = new Amendment();
		amendment.setContractEffectiveDate(Calendar.getInstance());
		amendment.setCustomerAcceptanceDate(Calendar.getInstance());
		amendment.setDescription("TestAmendment");
		amendment.setEffectiveDate(Calendar.getInstance());
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setServiceActivationDate(Calendar.getInstance());
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("TermsAndConditions");// TermsAndConditions
		ProductRatePlanCharge charge = queryChargeByAccountingCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));

		RatePlanData ratePlanData = new RatePlanData();
		RatePlan ratePlan = new RatePlan();
		ratePlanData.setRatePlan(ratePlan);
		ratePlan.setProductRatePlanId(charge.getProductRatePlanId());

		RatePlanChargeData ratePlanChargeData = new RatePlanChargeData();
		ratePlanData.setRatePlanChargeData(new RatePlanChargeData[] { ratePlanChargeData });

		RatePlanCharge ratePlanCharge = new RatePlanCharge();
		ratePlanChargeData.setRatePlanCharge(ratePlanCharge);

		ratePlanCharge.setProductRatePlanChargeId(charge.getId());
		ratePlanCharge.setQuantity(new BigDecimal(1));

		amendment.setRatePlanData(ratePlanData);

		AmendRequest amendRequest = new AmendRequest();
		amendRequest.setAmendments(new Amendment[] { amendment });

		AmendOptions options = new AmendOptions();
		options.setGenerateInvoice(true);
		options.setProcessPayments(true);

		amendRequest.setAmendOptions(options);
		amendRequest.setPreviewOptions(null);
		amend.setRequests(new AmendRequest[] { amendRequest });
		AmendResponse response = helper.amend(amend);
		AmendResult amendResult = response.getResults()[0];
		ID[] amendId = amendResult.getAmendmentIds();
		return amendId;
	}

	/**
	 * Test amend update product.
	 * 
	 * @return the i d[]
	 * @throws Exception
	 *           the exception
	 */
	public ID[] amendUpdateProduct() throws Exception {
		ZuoraUtility utility = new ZuoraUtility();
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336383000027");
		Amendment amendmentOld = queryAmendmentByName("AMEND-1336383188085");
		Amend amend = new Amend();
		Amendment amendment = new Amendment();
		// amendment.setId(amendmentOld.getId());
		amendment.setContractEffectiveDate(Calendar.getInstance());
		amendment.setCustomerAcceptanceDate(Calendar.getInstance());
		amendment.setDescription("TestAmendment");
		amendment.setEffectiveDate(Calendar.getInstance());
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setServiceActivationDate(Calendar.getInstance());
		amendment.setStatus("Completed");
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("UpdateProduct");
		ProductRatePlanCharge charge = queryChargeByAccountingCode(utility.getPropertyValue(PROPERTY_ACCOUNTING_CODE));

		RatePlan rate = queryRatePlanByName("PRP-1336382589447", amendmentOld.getId());
		RatePlanData ratePlanData = new RatePlanData();
		RatePlan ratePlan = new RatePlan();
		ratePlanData.setRatePlan(ratePlan);
		ratePlan.setAmendmentSubscriptionRatePlanId(rate.getAmendmentSubscriptionRatePlanId());

		RatePlanChargeData ratePlanChargeData = new RatePlanChargeData();
		ratePlanData.setRatePlanChargeData(new RatePlanChargeData[] { ratePlanChargeData });

		RatePlanCharge ratePlanCharge = new RatePlanCharge();
		ratePlanChargeData.setRatePlanCharge(ratePlanCharge);

		ratePlanCharge.setProductRatePlanChargeId(charge.getId());
		// ratePlanCharge.setQuantity(new BigDecimal(2));
		ratePlanCharge.setPrice(new BigDecimal(20));

		amendment.setRatePlanData(ratePlanData);

		AmendRequest amendRequest = new AmendRequest();
		amendRequest.setAmendments(new Amendment[] { amendment });

		AmendOptions options = new AmendOptions();
		options.setGenerateInvoice(true);
		options.setProcessPayments(true);

		amendRequest.setAmendOptions(options);
		amendRequest.setPreviewOptions(null);
		amend.setRequests(new AmendRequest[] { amendRequest });
		AmendResponse response = helper.amend(amend);
		AmendResult amendResult = response.getResults()[0];
		ID[] amendId = amendResult.getAmendmentIds();
		return amendId;

	}

	/**
	 * Test amend remove product.
	 * 
	 * @return the i d[]
	 * @throws Exception
	 *           the exception
	 */
	public ID[] amendRemoveProduct() throws Exception {

		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1334917426051");
		Amendment amendmentOld = queryAmendmentByName("AMEND-1334918394176");
		Amend amend = new Amend();
		Amendment amendment = new Amendment();
		amendment.setContractEffectiveDate(Calendar.getInstance());
		amendment.setCustomerAcceptanceDate(Calendar.getInstance());
		amendment.setDescription("TestAmendmentRemoveProduct");
		amendment.setEffectiveDate(Calendar.getInstance());
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setAutoRenew(true);
		amendment.setServiceActivationDate(Calendar.getInstance());
		amendment.setStatus("Completed");
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("RemoveProduct");

		RatePlan rate = queryRatePlanByName("PRP-1334904342410", amendmentOld.getId());
		RatePlanData ratePlanData = new RatePlanData();
		RatePlan ratePlan = new RatePlan();
		ratePlanData.setRatePlan(ratePlan);
		ratePlan.setAmendmentSubscriptionRatePlanId(rate.getAmendmentSubscriptionRatePlanId());

		amendment.setRatePlanData(ratePlanData);

		AmendRequest amendRequest = new AmendRequest();
		amendRequest.setAmendments(new Amendment[] { amendment });

		AmendOptions options = new AmendOptions();
		options.setGenerateInvoice(true);
		options.setProcessPayments(true);

		amendRequest.setAmendOptions(options);
		amendRequest.setPreviewOptions(null);
		amend.setRequests(new AmendRequest[] { amendRequest });
		AmendResponse response = helper.amend(amend);
		AmendResult amendResult = response.getResults()[0];
		ID[] amendId = amendResult.getAmendmentIds();
		return amendId;

	}

	/**
	 * Test amend renew product.
	 * 
	 * @return the i d[]
	 * @throws Exception
	 *           the exception
	 */
	public ID[] amendRenewProduct() throws Exception {

		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336383000027");
		Amend amend = new Amend();
		Amendment amendment = new Amendment();
		amendment.setContractEffectiveDate(Calendar.getInstance());
		amendment.setCustomerAcceptanceDate(Calendar.getInstance());
		amendment.setDescription("TestAmendmentRenewal");
		amendment.setEffectiveDate(Calendar.getInstance());
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setServiceActivationDate(Calendar.getInstance());
		amendment.setStatus("Completed");
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("Renewal");

		AmendRequest amendRequest = new AmendRequest();
		amendRequest.setAmendments(new Amendment[] { amendment });

		AmendOptions options = new AmendOptions();
		options.setGenerateInvoice(true);
		options.setProcessPayments(true);

		amendRequest.setAmendOptions(options);
		amendRequest.setPreviewOptions(null);
		amend.setRequests(new AmendRequest[] { amendRequest });
		AmendResponse response = helper.amend(amend);
		AmendResult amendResult = response.getResults()[0];
		ID[] amendId = amendResult.getAmendmentIds();
		return amendId;

	}

	/**
	 * Test amend cancel.
	 * 
	 * @return the i d[]
	 * @throws Exception
	 *           the exception
	 */
	public ID[] amendCancel() throws Exception {

		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Subscription subscription = getSubscriptionByName("SUB-1336383000027");
		Amend amend = new Amend();
		Amendment amendment = new Amendment();
		amendment.setContractEffectiveDate(ZuoraUtility.getCurrentDate());
		amendment.setCustomerAcceptanceDate(ZuoraUtility.getNextMonth());
		amendment.setDescription("cancel subscription");
		amendment.setEffectiveDate(ZuoraUtility.getNextMonth());
		amendment.setName("AMEND-" + System.currentTimeMillis());
		amendment.setStatus("Completed");
		amendment.setSubscriptionId(subscription.getId());
		amendment.setType("Cancellation");

		AmendRequest amendRequest = new AmendRequest();
		amendRequest.setAmendments(new Amendment[] { amendment });

		AmendOptions amendOptions = new AmendOptions();
		amendOptions.setGenerateInvoice(true);
		amendOptions.setProcessPayments(true);

		PreviewOptions previewOptions = new PreviewOptions();
		previewOptions.setEnablePreviewMode(true);

		amendRequest.setAmendOptions(amendOptions);
		amendRequest.setPreviewOptions(null);
		amend.setRequests(new AmendRequest[] { amendRequest });
		AmendResponse response = helper.amend(amend);
		AmendResult amendResult = response.getResults()[0];
		ID[] amendId = amendResult.getAmendmentIds();
		return amendId;

	}

}
