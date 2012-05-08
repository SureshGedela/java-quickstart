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
package com.zuora.api;

import com.zuora.api.sample.AccountSample;
import com.zuora.api.sample.AmendmentSample;
import com.zuora.api.sample.InvoiceSample;
import com.zuora.api.sample.ProductSample;
import com.zuora.api.sample.SubscribeSample;
import com.zuora.api.util.ZuoraUtility;

/**
 * The Class ApiTest.
 */
public class ApiTest {

	/**
	 * The main method.
	 * 
	 * @param arg
	 *          the arguments
	 */
	public static void main(String[] arg) {

		// Get test operation from command arguments. Default is
		// "account-create"
		// "crud" is used to run the test using ant
		String operation = "account-create";
		if (arg != null && arg.length == 1 && arg[0].equalsIgnoreCase("crud")) {
			operation = "account-create";
		}
		else if (arg != null && arg.length == 1) {
			operation = arg[0];
		}

		try {
			ZuoraUtility.print("Process start for -: " + operation);

			AccountSample account = new AccountSample();
			SubscribeSample subscribe = new SubscribeSample();
			ProductSample product = new ProductSample();
			AmendmentSample amendment = new AmendmentSample();
			InvoiceSample invoice = new InvoiceSample();

			if (operation.equalsIgnoreCase("account-create")) {
				account.createAccount();
			}
			else if (operation.equalsIgnoreCase("account-update")) {
				account.updateAccount();
			}
			else if (operation.equalsIgnoreCase("account-delete")) {
				account.deleteAccount();
			}
			else if (operation.equalsIgnoreCase("paymentmethod-update")) {
				account.updatePaymentMethod();
			}
			else if (operation.equalsIgnoreCase("paymentmethod-delete")) {
				account.deletePaymentMethod();
			}
			else if (operation.equalsIgnoreCase("subscribe-create")) {
				subscribe.createSubscribe();
			}
			else if (operation.equalsIgnoreCase("subscribe-withaccount")) {
				subscribe.createSubscribeWithExistingAccount();
			}
			else if (operation.equalsIgnoreCase("subscribe-activate")) {
				subscribe.updateSubscribe();// Activate subscription
			}
			else if (operation.equalsIgnoreCase("subscribe-delete")) {
				subscribe.deleteSubscribe();
			}
			else if (operation.equalsIgnoreCase("product")) {
				product.createProduct();
			}
			else if (operation.equalsIgnoreCase("product-update")) {
				product.updateProduct();
			}
			else if (operation.equalsIgnoreCase("product-delete")) {
				product.deleteProduct();
			}
			else if (operation.equalsIgnoreCase("addRatePlanCharge")) {
				product.addNewRatePlanCharge();
			}
			else if (operation.equalsIgnoreCase("amendment")) {
				amendment.createAmendment();// Create Amendment for any type
			}
			else if (operation.equalsIgnoreCase("amendment-tnc-update")) {
				amendment.updateAmendmentForTnC();
			}
			else if (operation.equalsIgnoreCase("amendment-newproduct")) {
				amendment.createAmendmentForNewProduct();
			}
			else if (operation.equalsIgnoreCase("amendment-newproduct-update")) {
				amendment.updateAmendmentForNewProduct();
			}
			else if (operation.equalsIgnoreCase("amendment-updateproduct")) {
				amendment.createAmendmentForUpdateProduct();
			}
			else if (operation.equalsIgnoreCase("amendment-updateproduct-update")) {
				amendment.updateAmendmentForUpdateProduct();
			}
			else if (operation.equalsIgnoreCase("amendment-activate")) {
				amendment.updateAmendment();
			}
			else if (operation.equalsIgnoreCase("amendment-delete")) {
				amendment.deleteAmendment();
			}
			else if (operation.equalsIgnoreCase("amend")) {
				amendment.amend();
			}
			else if (operation.equalsIgnoreCase("amend-updateproduct")) {
				amendment.amendUpdateProduct();
			}
			else if (operation.equalsIgnoreCase("amend-removeproduct")) {
				amendment.amendRemoveProduct();
			}
			else if (operation.equalsIgnoreCase("amend-renewproduct")) {
				amendment.amendRenewProduct();
			}
			else if (operation.equalsIgnoreCase("amend-cancel")) {
				amendment.amendCancel();
			}
			else if (operation.equalsIgnoreCase("invoice-create")) {
				invoice.createInvoice();
			}
			else if (operation.equalsIgnoreCase("invoice-update")) {
				invoice.updateInvoice();
			}
			else if (operation.equalsIgnoreCase("invoice-pdf")) {
				invoice.generateInvoicePDF();
			}
			else if (operation.equalsIgnoreCase("adjustment-create")) {
				invoice.createAdjustment();
			}
			else if (operation.equalsIgnoreCase("adjustment-cancel")) {
				invoice.cancelAdjustment();
			}
			else if (operation.equalsIgnoreCase("adjustment-delete")) {
				invoice.deleteAdjustment();
			}
			else if (operation.equalsIgnoreCase("itemadjustment-create")) {
				invoice.createItemAdjustment();
			}
			else if (operation.equalsIgnoreCase("itemadjustment-cancel")) {
				invoice.cancelItemAdjustment();
			}
			else if (operation.equalsIgnoreCase("itemadjustment-delete")) {
				invoice.deleteItemAdjustment();
			}
			else if (operation.equalsIgnoreCase("invoicepayment")) {
				invoice.createPayment();
			}
			else if (operation.equalsIgnoreCase("payment")) {
				invoice.createPayment();
			}
			else if (operation.equalsIgnoreCase("payment-update")) {
				invoice.updatePayment();
			}
			else if (operation.equalsIgnoreCase("payment-delete")) {
				invoice.deletePayment();
			}
			else if (operation.equalsIgnoreCase("refund")) {
				invoice.createRefund();
			}
			else if (operation.equalsIgnoreCase("refund-update")) {
				invoice.updateRefund();
			}
			else if (operation.equalsIgnoreCase("usage")) {
				account.createUsage();
			}
			else if (operation.equalsIgnoreCase("usage-update")) {
				account.updateUsage();
			}
			else if (operation.equalsIgnoreCase("usage-delete")) {
				account.deleteUsage();
			}
			else if (operation.equalsIgnoreCase("querymore")) {
				account.queryMore();
			}
		}
		catch (com.zuora.api.axis2.LoginFault e) {
			ZuoraUtility.print("Login Failure : " + e.getFaultMessage().getLoginFault().getFaultMessage());
		}
		catch (com.zuora.api.axis2.InvalidTypeFault ie) {
			ZuoraUtility.print("InvalidTypeFault Code : " + ie.getFaultMessage().getInvalidTypeFault().getFaultCode());
			ZuoraUtility.print("Message: " + ie.getFaultMessage().getInvalidTypeFault().getFaultMessage());
		}
		catch (Exception e) {
			ZuoraUtility.print(e.getMessage());
			e.printStackTrace();
		}
		ZuoraUtility.print("Process Completed.");
	}

}
