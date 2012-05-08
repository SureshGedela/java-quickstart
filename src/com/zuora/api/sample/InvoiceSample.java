package com.zuora.api.sample;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.Account;
import com.zuora.api.axis2.ZuoraServiceStub.Generate;
import com.zuora.api.axis2.ZuoraServiceStub.GenerateResponse;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.Invoice;
import com.zuora.api.axis2.ZuoraServiceStub.InvoiceAdjustment;
import com.zuora.api.axis2.ZuoraServiceStub.InvoiceItem;
import com.zuora.api.axis2.ZuoraServiceStub.InvoiceItemAdjustment;
import com.zuora.api.axis2.ZuoraServiceStub.InvoicePayment;
import com.zuora.api.axis2.ZuoraServiceStub.Payment;
import com.zuora.api.axis2.ZuoraServiceStub.PaymentMethod;
import com.zuora.api.axis2.ZuoraServiceStub.Refund;
import com.zuora.api.axis2.ZuoraServiceStub.SaveResult;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.util.ZuoraAPIHelper;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class InvoiceSample.
 * 
 * @author
 */
public class InvoiceSample {

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/**
	 * Creates the invoice.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createInvoice() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Invoice invoice = new Invoice();
		Account account = queryAccountByName("ACC-1336382492856");
		invoice.setAccountId(account.getId());
		invoice.setIncludesOneTime(true);
		invoice.setIncludesRecurring(true);
		invoice.setIncludesUsage(true);
		invoice.setInvoiceDate(ZuoraUtility.getCurrentDate());
		invoice.setTargetDate(ZuoraUtility.getNextMonth());
		Generate generate = new Generate();
		generate.addZObjects(invoice);
		GenerateResponse response = stub.generate(generate, this.header);
		SaveResult saveResult = response.getResult()[0];
		if (saveResult.getErrors() == null) {
			ZuoraUtility.print("Invoice Status: Success");
		}
		else {
			ZuoraUtility.print("Invoice status : Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(saveResult));
		}
		return saveResult.getId();
	}

	/**
	 * Update invoice.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateInvoice() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Invoice invoice = queryInvoiceByNumber("INV00000024");
		invoice.setStatus("Posted");
		ID invId = helper.update(invoice);
		ZuoraUtility.print("InvoiceId :" + invId);
		return invId;
	}

	/**
	 * Generate invoice pdf.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean generateInvoicePDF() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Invoice invoice = queryInvoiceBody("INV00000017");
		byte[] decodedData = ZuoraUtility.decodeBase64(invoice.getBody());
		ZuoraUtility.writeFile(decodedData);
		if (decodedData.length > 0)
			return true;
		else
			return false;
	}

	/**
	 * Creates the adjustment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		InvoiceAdjustment adjustment = new InvoiceAdjustment();
		adjustment.setAccountingCode("ACCCODE1");
		adjustment.setAdjustmentDate(Calendar.getInstance());
		adjustment.setAmount(new BigDecimal(4));
		adjustment.setComments("test comments");
		adjustment.setInvoiceNumber("INV00000024");
		adjustment.setReferenceId("RE-002");
		adjustment.setType("Credit");
		ID adjustmentId = helper.create(adjustment);
		ZuoraUtility.print("AdjustmentId: " + adjustmentId);
		return adjustmentId;
	}

	/**
	 * Cancel adjustment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID cancelAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		InvoiceAdjustment adjustment = queryAdjustment("IA-00000001");
		adjustment.setStatus("Canceled");
		ID adjustId = helper.update(adjustment);
		ZuoraUtility.print("Updated adjustId: " + adjustId);
		return adjustId;
	}

	/**
	 * Delete adjustment.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		boolean isDeleted = false;
		InvoiceAdjustment adjustment = queryAdjustment("IA-00000001");
		if (adjustment != null) {
			isDeleted = helper.delete("InvoiceAdjustment", adjustment.getId());
		}
		return isDeleted;
	}

	/**
	 * Creates the item adjustment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createItemAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();

		Invoice invoice = queryInvoiceByNumber("INV00000022");
		InvoiceItem item = queryInvoiceItem(invoice.getId());
		InvoiceItemAdjustment itemAdjustment = new InvoiceItemAdjustment();
		itemAdjustment.setAccountingCode("ACCCODE1");
		itemAdjustment.setAdjustmentDate(Calendar.getInstance());
		itemAdjustment.setAmount(new BigDecimal(1));
		itemAdjustment.setComment("Testing");
		itemAdjustment.setInvoiceNumber("INV00000022");
		itemAdjustment.setSourceId(item.getId().toString());
		itemAdjustment.setSourceType("InvoiceDetail");
		itemAdjustment.setType("Credit");
		ID id = helper.create(itemAdjustment);
		ZuoraUtility.print("ItemAdjustment ID: " + id);
		return id;
	}

	/**
	 * Cancel item adjustment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID cancelItemAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		InvoiceItemAdjustment itemAdjustment = queryItemAdjustment("IIA-00000001");
		itemAdjustment.setStatus("Canceled");
		ID id = helper.update(itemAdjustment);
		ZuoraUtility.print("ItemAdjustment ID: " + id);
		return id;
	}

	/**
	 * Delete item adjustment.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deleteItemAdjustment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		boolean isDeleted = false;
		InvoiceItemAdjustment itemAdjustment = queryItemAdjustment("IIA-00000001");
		if (itemAdjustment != null) {
			isDeleted = helper.delete("InvoiceItemAdjustment", itemAdjustment.getId());
		}
		return isDeleted;
	}

	/**
	 * Invoice payment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createPayment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Account account = queryAccountByName("ACC-1334899124957");
		Invoice invoice = queryInvoiceforPayment("INV00000022 ", account.getId());
		PaymentMethod paymentMethod = queryPaymentMethod(account.getId());

		Payment payment = new Payment();
		payment.setAccountId(account.getId());
		payment.setAppliedInvoiceAmount(new BigDecimal(1));
		payment.setEffectiveDate(Calendar.getInstance());
		payment.setInvoiceId(invoice.getId());
		payment.setPaymentMethodId(paymentMethod.getId());
		payment.setStatus("Processed");
		payment.setType("Electronic");
		ID paymentId = helper.create(payment);
		ZuoraUtility.print("PaymentId: " + paymentId);
		return paymentId;
	}

	/**
	 * Creates the payment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID invoicePayment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Account account = queryAccountByName("ACC-1336382492856");
		PaymentMethod paymentMethod = queryPaymentMethod(account.getId());
		Payment payment = new Payment();
		payment.setAccountId(account.getId());
		payment.setAmount(new BigDecimal(1));
		payment.setEffectiveDate(Calendar.getInstance());
		payment.setPaymentMethodId(paymentMethod.getId());
		payment.setStatus("Draft");
		payment.setType("Electronic");
		ID paymentId = helper.create(payment);
		ZuoraUtility.print("PaymentID: " + paymentId);

		Invoice invoice = queryInvoiceforPayment("INV00000030", account.getId());
		InvoicePayment invoicePayment = new InvoicePayment();
		invoicePayment.setAmount(new BigDecimal(1));
		invoicePayment.setInvoiceId(invoice.getId());
		invoicePayment.setPaymentId(paymentId);
		ID invPayId = helper.create(invoicePayment);
		ZuoraUtility.print("Invoice payment ID: " + invPayId);
		return invPayId;
	}

	/**
	 * Update payment.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updatePayment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Payment payment = queryPayment("P-00000029");
		payment.setStatus("Processed");
		ID paymentId = helper.update(payment);
		return paymentId;
	}

	/**
	 * Delete payment.
	 * 
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean deletePayment() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		boolean isDeleted = false;
		Payment payment = queryPayment("P-00000026");
		if (payment != null) {
			isDeleted = helper.delete("Payment", payment.getId());
		}
		return isDeleted;
	}

	/**
	 * Creates the refund.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID createRefund() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Payment payment = queryPayment("P-00000029");
		Refund refund = new Refund();
		refund.setType("Electronic");
		refund.setComment("Test Refund");
		refund.setPaymentId(payment.getId());
		refund.setAmount(new BigDecimal(0.5));
		ID refundId = helper.create(refund);
		ZuoraUtility.print("Refund Id: " + refundId);
		return refundId;
	}

	/**
	 * Update refund.
	 * 
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID updateRefund() throws Exception {
		ZuoraAPIHelper helper = new ZuoraAPIHelper();
		helper.login();
		stub = helper.getStub();
		header = helper.getHeader();
		Refund refund = queryRefund("R-00000003");
		refund.setStatus("Processed");
		ID refundId = helper.update(refund);
		return refundId;
	}

	/**
	 * Query item adjustment.
	 * 
	 * @param number
	 *          the number
	 * @return the invoice item adjustment
	 * @throws Exception
	 *           the exception
	 */
	public InvoiceItemAdjustment queryItemAdjustment(String number) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id  FROM InvoiceItemAdjustment WHERE AdjustmentNumber = '" + number + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		InvoiceItemAdjustment item = (InvoiceItemAdjustment) qResult.getRecords()[0];
		return item;
	}

	/**
	 * Query invoice item.
	 * 
	 * @param invoiceId
	 *          the invoice id
	 * @return the invoice item
	 * @throws Exception
	 *           the exception
	 */
	public InvoiceItem queryInvoiceItem(ID invoiceId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id  FROM InvoiceItem WHERE invoiceid = '" + invoiceId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		InvoiceItem item = (InvoiceItem) qResult.getRecords()[0];
		return item;
	}

	/**
	 * Query account by name.
	 * 
	 * @param accountName
	 *          the account name
	 * @return the Account
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
	 * Query payment method.
	 * 
	 * @param accountId
	 *          the account id
	 * @return the payment method
	 * @throws Exception
	 *           the exception
	 */
	public PaymentMethod queryPaymentMethod(ID accountId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id,AccountId,Type FROM PaymentMethod WHERE AccountId = '" + accountId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		PaymentMethod paymentMethod = (PaymentMethod) qResult.getRecords()[0];
		return paymentMethod;
	}

	/**
	 * Query payment.
	 * 
	 * @param number
	 *          the number
	 * @return the payment
	 * @throws Exception
	 *           the exception
	 */
	public Payment queryPayment(String number) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Payment WHERE PaymentNumber = '" + number + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Payment pay = (Payment) qResult.getRecords()[0];
		return pay;
	}

	/**
	 * Query payment by id.
	 * 
	 * @param paymentId
	 *          the payment id
	 * @return the payment
	 * @throws Exception
	 *           the exception
	 */
	public Payment queryPaymentById(ID paymentId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Payment WHERE id = '" + paymentId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Payment pay = (Payment) qResult.getRecords()[0];
		return pay;
	}

	/**
	 * Query invoice payment.
	 * 
	 * @param invoiceid
	 *          the invoice id
	 * @param paymentId
	 *          the payment id
	 * @return the invoice payment
	 * @throws Exception
	 *           the exception
	 */
	public InvoicePayment queryInvoicePayment(ID invoiceid, ID paymentId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM InvoicePayment WHERE " + "invoiceid = '" + invoiceid + "' and PaymentId= '"
		    + paymentId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		InvoicePayment invoicepay = (InvoicePayment) qResult.getRecords()[0];
		return invoicepay;
	}

	/**
	 * Query refund.
	 * 
	 * @param number
	 *          the number
	 * @return the refund
	 * @throws Exception
	 *           the exception
	 */
	public Refund queryRefund(String number) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Refund WHERE RefundNumber = '" + number + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Refund refund = (Refund) qResult.getRecords()[0];
		return refund;
	}

	/**
	 * Query refund by id.
	 * 
	 * @param refundId
	 *          the refund id
	 * @return the refund
	 * @throws Exception
	 *           the exception
	 */
	public Refund queryRefundById(ID refundId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Refund WHERE id = '" + refundId + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Refund refund = (Refund) qResult.getRecords()[0];
		return refund;
	}

	/**
	 * Query invoice.
	 * 
	 * @param invoiceNumber
	 *          the invoice number
	 * @return the invoice
	 * @throws Exception
	 *           the exception
	 */
	public Invoice queryInvoiceByNumber(String invoiceNumber) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Invoice WHERE InvoiceNumber = '" + invoiceNumber + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Invoice inv = (Invoice) qResult.getRecords()[0];
		return inv;
	}

	/**
	 * Query invoice.
	 * 
	 * @param invoiceid
	 *          the ID
	 * @return the invoice
	 * @throws Exception
	 *           the exception
	 */
	public Invoice queryInvoice(ID invoiceid) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM Invoice WHERE id = '" + invoiceid + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Invoice inv = (Invoice) qResult.getRecords()[0];
		return inv;
	}

	/**
	 * Query invoice for payment.
	 * 
	 * @param invoiceNumber
	 *          the invoice number
	 * @param accountId
	 *          the account id
	 * @return the invoice
	 * @throws Exception
	 *           the exception
	 */
	public Invoice queryInvoiceforPayment(String invoiceNumber, ID accountId) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id,Balance FROM Invoice WHERE " + "InvoiceNumber = '" + invoiceNumber + "' and "
		    + "AccountId = '" + accountId + "' and Balance > 0");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Invoice inv = (Invoice) qResult.getRecords()[0];
		return inv;
	}

	/**
	 * Query adjustment.
	 * 
	 * @param adjustNumber
	 *          the adjust number
	 * @return the invoice adjustment
	 * @throws Exception
	 *           the exception
	 */
	public InvoiceAdjustment queryAdjustment(String adjustNumber) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT id FROM InvoiceAdjustment WHERE AdjustmentNumber = '" + adjustNumber + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		InvoiceAdjustment adjust = (InvoiceAdjustment) qResult.getRecords()[0];
		return adjust;
	}

	/**
	 * Query invoice body.
	 * 
	 * @param invoiceNumber
	 *          the invoice number
	 * @return the invoice
	 * @throws Exception
	 *           the exception
	 */
	public Invoice queryInvoiceBody(String invoiceNumber) throws Exception {
		ZuoraServiceStub.Query query = new ZuoraServiceStub.Query();
		query.setQueryString("SELECT Id, AccountId, Amount, Balance, DueDate, "
		    + "InvoiceDate, InvoiceNumber, Status, TargetDate, Body " + "FROM Invoice where InvoiceNumber= '"
		    + invoiceNumber + "'");
		ZuoraServiceStub.QueryResponse qResponse = stub.query(query, null, this.header);
		ZuoraServiceStub.QueryResult qResult = qResponse.getResult();
		Invoice inv = (Invoice) qResult.getRecords()[0];
		return inv;
	}

}
