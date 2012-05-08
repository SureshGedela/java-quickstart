package com.zuora.api.sample;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.Product;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlan;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlanCharge;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlanChargeTier;
import com.zuora.api.axis2.ZuoraServiceStub.ProductRatePlanChargeTierData;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.util.ZuoraAPIHelper;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class ProductSample.
 */
public class ProductSample {

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/**
	 * Test product.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createProduct() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();

		Product product = makeProduct();
		ID productId = helper.create(product);
		ZuoraUtility.print("Product ID:" + productId);

		// Make ProductRatePlan
		ProductRatePlan productRatePlan = new ProductRatePlan();
		productRatePlan.setDescription("TestProductRatePlan");
		productRatePlan.setEffectiveEndDate(ZuoraUtility.getEndDate());
		productRatePlan.setEffectiveStartDate(ZuoraUtility.getCurrentDate());
		productRatePlan.setName("PRP-" + System.currentTimeMillis());
		productRatePlan.setProductId(productId);

		ID productRatePlanId = helper.create(productRatePlan);
		ZuoraUtility.print("productRatePlan ID: " + productRatePlanId);

		// Make ProductRatePlanCharge
		ProductRatePlanCharge productRatePlanCharge = new ProductRatePlanCharge();
		productRatePlanCharge.setAccountingCode("ACCODE-" + System.currentTimeMillis());
		productRatePlanCharge.setBillCycleType("DefaultFromCustomer");
		productRatePlanCharge.setBillingPeriod("Month");
		productRatePlanCharge.setChargeModel("FlatFee");
		productRatePlanCharge.setChargeType("Recurring");
		productRatePlanCharge.setDescription("Test ProductRatePlanCharge");
		productRatePlanCharge.setName("PRPC-" + System.currentTimeMillis());
		productRatePlanCharge.setTriggerEvent("ContractEffective");
		productRatePlanCharge.setProductRatePlanId(productRatePlanId);

		// Make ProductRatePlanChargeTier
		ProductRatePlanChargeTier chargeTier = new ProductRatePlanChargeTier();
		chargeTier.setActive(true);
		chargeTier.setCurrency("USD");
		chargeTier.setPrice(BigDecimal.valueOf(10));
		chargeTier.setPriceFormat("Flat Fee");
		chargeTier.setStartingUnit(BigDecimal.valueOf(1));

		ProductRatePlanChargeTierData chargeTierData = new ProductRatePlanChargeTierData();
		chargeTierData.setProductRatePlanChargeTier(new ProductRatePlanChargeTier[] { chargeTier });
		productRatePlanCharge.setProductRatePlanChargeTierData(chargeTierData);
		ID productRatePlanChargeId = helper.create(productRatePlanCharge);
		ZuoraUtility.print("productRatePlanCharge ID:" + productRatePlanChargeId);
		return productId;
	}

	/**
	 * Test update product.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateProduct() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();

		// Get Product
		Product product = queryProductByName("PROD-1336115807843");
		product.setDescription("Update Product");
		// product.setName("PROD-"+System.currentTimeMillis());
		ID productId = helper.update(product);
		ZuoraUtility.print("Product ID:" + productId);

		// Get ProductRatePlan
		ProductRatePlan productRatePlan = queryProductRatePlan("PRP-1336115808796");
		productRatePlan.setDescription("UpdateProductRatePlan");
		productRatePlan.setEffectiveEndDate(ZuoraUtility.getCurrentDate());
		productRatePlan.setEffectiveStartDate(ZuoraUtility.getCurrentDate());
		// productRatePlan.setName("PRP-"+System.currentTimeMillis());
		productRatePlan.setProductId(productId);
		ID productRatePlanId = helper.update(productRatePlan);
		ZuoraUtility.print("ProductRatePlan ID: " + productRatePlanId);

		// ProductRatePlanCharge is optional. to update uncomment the below code.

		/*
		 * // Get ProductRatePlanCharge ProductRatePlanCharge productRatePlanCharge
		 * = queryProductRatePlanCharge("PRPC-1336115809593");
		 * productRatePlanCharge.setBillingPeriod("Month");
		 * 
		 * // Get ProductRatePlanChargeTier ProductRatePlanChargeTier chargeTier =
		 * queryProductRatePlanChargeTier("4028e6963716432e013716b5504c1ce1");
		 * chargeTier.setActive(true); chargeTier.setPrice(BigDecimal.valueOf(5));
		 * chargeTier.setPriceFormat("Flat Fee");
		 * 
		 * ProductRatePlanChargeTierData chargeTierData = new
		 * ProductRatePlanChargeTierData();
		 * chargeTierData.setProductRatePlanChargeTier(new
		 * ProductRatePlanChargeTier[] { chargeTier });
		 * productRatePlanCharge.setProductRatePlanChargeTierData(chargeTierData);
		 * ID chargeId = helper.update(productRatePlanCharge);
		 * ZuoraUtility.print("productRatePlanCharge ID:"+ chargeId);
		 */
		return productId;
	}

	/**
	 * Adds the new rate plan charge.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void addNewRatePlanCharge() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();

		// Get existing ProductRatePlan
		ProductRatePlan productRatePlan = queryProductRatePlan("PRP-1336115808796");
		ZuoraUtility.print("productRatePlan ID: " + productRatePlan.getId());

		// Make ProductRatePlanCharge
		ProductRatePlanCharge productRatePlanCharge = new ProductRatePlanCharge();
		productRatePlanCharge.setAccountingCode("ACCODE-" + System.currentTimeMillis());
		productRatePlanCharge.setBillCycleType("DefaultFromCustomer");
		productRatePlanCharge.setBillingPeriod("Month");
		productRatePlanCharge.setChargeModel("Per Unit Pricing");
		productRatePlanCharge.setChargeType("Usage");
		productRatePlanCharge.setDescription("Usage ProductRatePlanCharge");
		productRatePlanCharge.setName("PRPC-" + System.currentTimeMillis());
		productRatePlanCharge.setTriggerEvent("ContractEffective");
		productRatePlanCharge.setProductRatePlanId(productRatePlan.getId());
		productRatePlanCharge.setUOM("Each");

		// Make ProductRatePlanChargeTier
		ProductRatePlanChargeTier chargeTier = new ProductRatePlanChargeTier();
		chargeTier.setActive(true);
		chargeTier.setCurrency("USD");
		chargeTier.setPrice(BigDecimal.valueOf(10));
		chargeTier.setPriceFormat("Flat Fee");
		chargeTier.setStartingUnit(BigDecimal.valueOf(1));

		ProductRatePlanChargeTierData chargeTierData = new ProductRatePlanChargeTierData();
		chargeTierData.setProductRatePlanChargeTier(new ProductRatePlanChargeTier[] { chargeTier });
		productRatePlanCharge.setProductRatePlanChargeTierData(chargeTierData);
		ID productRatePlanChargeId = helper.create(productRatePlanCharge);
		ZuoraUtility.print("productRatePlanCharge ID:" + productRatePlanChargeId);
	}

	/**
	 * Test delete product.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteProduct() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		header = helper.getHeader();
		stub = helper.getStub();
		boolean isDeleted = false;
		Product product = queryProductByName("PROD-1336115954828");
		if (product != null) {
			isDeleted = helper.delete("Product", product.getId());
		}

		ZuoraUtility.print("Product deleted  " + isDeleted);
		return isDeleted;
	}

	/**
	 * Make product.
	 * 
	 * @return the product
	 */
	private Product makeProduct() {
		Product product = new Product();
		product.setName("PROD-" + System.currentTimeMillis());
		product.setCategory("Base Products");
		product.setDescription("Test Product");
		product.setEffectiveEndDate(ZuoraUtility.getEndDate());
		product.setEffectiveStartDate(Calendar.getInstance());
		return product;
	}

	/**
	 * Query product by name.
	 * 
	 * @param productName
	 *          the product name
	 * @return the product
	 * @throws Exception
	 *           the exception
	 */
	public Product queryProductByName(String productName) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, Name, SKU FROM product WHERE Name = '" + productName + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Product rec = (Product) qResult.getRecords()[0];
		return rec;
	}

	/**
	 * Query product.
	 * 
	 * @param productId
	 *          the product id
	 * @return the product
	 * @throws Exception
	 *           the exception
	 */
	public Product queryProduct(ID productId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id,name from product WHERE id = '" + productId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Product product = (Product) qResult.getRecords()[0];
		return product;
	}

	/**
	 * Query product rate plan.
	 * 
	 * @param productRatePlan
	 *          the product rate plan
	 * @return the product rate plan
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlan queryProductRatePlan(String productRatePlan) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, Name FROM ProductRatePlan WHERE Name = '" + productRatePlan + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlan prp = (ProductRatePlan) qResult.getRecords()[0];
		return prp;
	}

	/**
	 * Query product rate plan charge.
	 * 
	 * @param productRatePlanCharge
	 *          the product rate plan charge
	 * @return the product rate plan charge
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlanCharge queryProductRatePlanCharge(String productRatePlanCharge) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id, Name FROM ProductRatePlanCharge WHERE Name = '" + productRatePlanCharge + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlanCharge prpc = (ProductRatePlanCharge) qResult.getRecords()[0];
		return prpc;
	}

	/**
	 * Query product rate plan charge tier.
	 * 
	 * @param tierId
	 *          the tier id
	 * @return the product rate plan charge tier
	 * @throws Exception
	 *           the exception
	 */
	public ProductRatePlanChargeTier queryProductRatePlanChargeTier(String tierId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM ProductRatePlanChargeTier WHERE id = '" + tierId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		ProductRatePlanChargeTier prpct = (ProductRatePlanChargeTier) qResult.getRecords()[0];
		return prpct;
	}

}
