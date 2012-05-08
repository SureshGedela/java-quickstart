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
package com.zuora.api.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zuora.api.axis2.ZuoraServiceStub.Account;
import com.zuora.api.axis2.ZuoraServiceStub.Amendment;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.Invoice;
import com.zuora.api.axis2.ZuoraServiceStub.Payment;
import com.zuora.api.axis2.ZuoraServiceStub.Product;
import com.zuora.api.axis2.ZuoraServiceStub.Refund;
import com.zuora.api.axis2.ZuoraServiceStub.Subscription;
import com.zuora.api.axis2.ZuoraServiceStub.Usage;
import com.zuora.api.sample.AccountSample;
import com.zuora.api.sample.AmendmentSample;
import com.zuora.api.sample.InvoiceSample;
import com.zuora.api.sample.ProductSample;
import com.zuora.api.sample.SubscribeSample;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class SampleTester.
 */
public class SampleTester {

	/**
	 * Sets up the environment.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	@Before
	public void setUp() throws Exception {
		ZuoraUtility.print("Test case starts.");
	}

	/**
	 * Clean up the environment.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	@After
	public void tearDown() throws Exception {
		ZuoraUtility.print("Test case ends.\n");
	}

	/**
	 * Test create account.
	 */
	@Test
	public void testCreateAccount() {
		AccountSample sample = new AccountSample();
		try {
			ID id = sample.createAccount();
			Account account = sample.queryAccount(id);
			assertNotNull(account);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update account.
	 */
	@Test
	public void testUpdateAccount() {
		AccountSample sample = new AccountSample();
		try {
			ID id = sample.updateAccount();
			Account account = sample.queryAccount(id);
			assertNotNull(account);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test delete account.
	 */
	@Test
	public void testDeleteAccount() {
		AccountSample sample = new AccountSample();
		try {
			boolean isDeleted = sample.deleteAccount();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create product.
	 */
	@Test
	public void testCreateProduct() {
		ProductSample sample = new ProductSample();
		try {
			ID productId = sample.createProduct();
			Product product = sample.queryProduct(productId);
			assertNotNull(product);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update product.
	 */
	@Test
	public void testUpdateProduct() {
		ProductSample sample = new ProductSample();
		try {
			ID productId = sample.updateProduct();
			Product product = sample.queryProduct(productId);
			assertNotNull(product);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test delete product.
	 */
	@Test
	public void testDeleteProduct() {
		ProductSample sample = new ProductSample();
		try {
			boolean isDeleted = sample.deleteProduct();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create subscribe.
	 */
	@Test
	public void testCreateSubscribe() {
		SubscribeSample sample = new SubscribeSample();
		try {
			ID subscribeId = sample.createSubscribe();
			Subscription subscription = sample.querySubscription(subscribeId);
			assertNotNull(subscription);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create subscribe with account.
	 */
	@Test
	public void testCreateSubscribeWithAccount() {
		SubscribeSample sample = new SubscribeSample();
		try {
			ID subscribeId = sample.createSubscribeWithExistingAccount();
			Subscription subscription = sample.querySubscription(subscribeId);
			assertNotNull(subscription);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update subscription.
	 */
	@Test
	public void testUpdateSubscription() {
		SubscribeSample sample = new SubscribeSample();
		try {
			ID subscriptionId = sample.updateSubscribe();
			Subscription subscription = sample.querySubscription(subscriptionId);
			assertNotNull(subscription);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test delete subscription.
	 */
	@Test
	public void testDeleteSubscription() {
		SubscribeSample sample = new SubscribeSample();
		try {
			boolean isDeleted = sample.deleteSubscribe();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create amendment.
	 */
	@Test
	public void testCreateAmendment() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID amendmentId = sample.createAmendment();
			Amendment amendment = sample.queryAmendment(amendmentId);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create amend.
	 */
	@Test
	public void testCreateAmend() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID[] amendmentId = sample.amend();
			Amendment amendment = sample.queryAmendment(amendmentId[0]);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update amend.
	 */
	@Test
	public void testUpdateAmend() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID[] amendmentId = sample.amendUpdateProduct();
			Amendment amendment = sample.queryAmendment(amendmentId[0]);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test remove amend.
	 */
	@Test
	public void testRemoveAmend() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID[] amendmentId = sample.amendRemoveProduct();
			Amendment amendment = sample.queryAmendment(amendmentId[0]);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test renew amend.
	 */
	@Test
	public void testRenewAmend() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID[] amendmentId = sample.amendRenewProduct();
			Amendment amendment = sample.queryAmendment(amendmentId[0]);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test cancel amend.
	 */
	@Test
	public void testCancelAmend() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID[] amendmentId = sample.amendCancel();
			Amendment amendment = sample.queryAmendment(amendmentId[0]);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update amendment.
	 */
	@Test
	public void testUpdateAmendment() {
		AmendmentSample sample = new AmendmentSample();
		try {
			ID amendmentId = sample.updateAmendmentForTnC();
			Amendment amendment = sample.queryAmendment(amendmentId);
			assertNotNull(amendment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test delete amendment.
	 */
	@Test
	public void testDeleteAmendment() {
		AmendmentSample sample = new AmendmentSample();
		try {
			boolean isDeleted = sample.deleteAmendment();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create invoice.
	 */
	@Test
	public void testCreateInvoice() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID invoiceId = sample.createInvoice();
			Invoice invoice = sample.queryInvoice(invoiceId);
			assertNotNull(invoice);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update Invoice.
	 */
	@Test
	public void testUpdateInvoice() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID invoiceId = sample.updateInvoice();
			Invoice invoice = sample.queryInvoice(invoiceId);
			assertNotNull(invoice);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test generate invoice.
	 */
	@Test
	public void testGenerateInvoice() {
		InvoiceSample sample = new InvoiceSample();
		try {
			boolean isGenerated = sample.generateInvoicePDF();
			assertTrue(isGenerated);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create payment.
	 */
	@Test
	public void testCreatePayment() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID paymentId = sample.createPayment();
			Payment payment = sample.queryPaymentById(paymentId);
			assertNotNull(payment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update payment.
	 */
	@Test
	public void testUpdatePayment() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID paymentId = sample.updatePayment();
			Payment payment = sample.queryPaymentById(paymentId);
			assertNotNull(payment);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Testdelete payment.
	 */
	@Test
	public void testdeletePayment() {
		InvoiceSample sample = new InvoiceSample();
		try {
			boolean isDeleted = sample.deletePayment();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create refund.
	 */
	@Test
	public void testCreateRefund() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID refundId = sample.createRefund();
			Refund refund = sample.queryRefundById(refundId);
			assertNotNull(refund);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update refund.
	 */
	@Test
	public void testUpdateRefund() {
		InvoiceSample sample = new InvoiceSample();
		try {
			ID refundId = sample.updateRefund();
			Refund refund = sample.queryRefundById(refundId);
			assertNotNull(refund);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test create usage.
	 */
	@Test
	public void testCreateUsage() {
		AccountSample sample = new AccountSample();
		try {
			ID usageId = sample.createUsage();
			Usage usage = sample.queryUsage(usageId);
			assertNotNull(usage);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test update usage.
	 */
	@Test
	public void testUpdateUsage() {
		AccountSample sample = new AccountSample();
		try {
			ID usageId = sample.updateUsage();
			Usage usage = sample.queryUsage(usageId);
			assertNotNull(usage);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test delete usage.
	 */
	@Test
	public void testDeleteUsage() {
		AccountSample sample = new AccountSample();
		try {
			boolean isDeleted = sample.deleteUsage();
			assertTrue(isDeleted);
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
	}
}
