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
package com.zuora.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

import com.zuora.api.axis2.ZuoraServiceStub.Account;
import com.zuora.api.axis2.ZuoraServiceStub.AmendResult;
import com.zuora.api.axis2.ZuoraServiceStub.DeleteResult;
import com.zuora.api.axis2.ZuoraServiceStub.Error;
import com.zuora.api.axis2.ZuoraServiceStub.QueryResult;
import com.zuora.api.axis2.ZuoraServiceStub.SaveResult;
import com.zuora.api.axis2.ZuoraServiceStub.SubscribeResult;
import com.zuora.api.axis2.ZuoraServiceStub.ZObject;

/**
 * The Class ZuoraUtility.
 */
public class ZuoraUtility {

	/** The Constant FILE_PROPERTY_NAME. */
	private static final String FILE_PROPERTY_NAME = "test.properties";

	/** The Constant FILE_PATH. */
	private static final String FILE_PATH = "filepath";

	/** The properties. */
	private Properties properties;

	/**
	 * To string.
	 * 
	 * @param acc
	 *          the account
	 * @return the string
	 */
	public static String toString(Account acc) {
		StringBuilder buff = new StringBuilder();
		buff.append("\t" + acc.getId().getID());
		buff.append("\n\t" + acc.getName());
		buff.append("\n\t" + acc.getAccountNumber());
		return buff.toString();
	}

	/**
	 * Prints the message.
	 * 
	 * @param message
	 *          the message
	 */
	public static void print(String message) {
		System.out.println(message);
	}

	/**
	 * Checks if is not null.
	 * 
	 * @param name
	 *          the name
	 * @return true, if is not null
	 */
	public static boolean isNotNull(String name) {
		return name != null && name.length() > 0;
	}

	/**
	 * Creates the print format of the subscribe result value.
	 * 
	 * @param resultArray
	 *          the result array
	 * @return the string
	 */
	public String createMessage(SubscribeResult[] resultArray) {
		StringBuilder resultString = new StringBuilder("SusbscribeResult :\n");
		if (resultArray != null) {
			SubscribeResult result = resultArray[0];
			if (result.getSuccess()) {
				resultString.append("\nSubscribe Result: \n").append("\n\tAccount Id: ").append(result.getAccountId())
				    .append("\n\tAccount Number: ").append(result.getAccountNumber()).append("\n\tSubscription Id: ")
				    .append(result.getSubscriptionId()).append("\n\tSubscription Number: ")
				    .append(result.getSubscriptionNumber()).append("\n\tInvoice Number: ").append(result.getInvoiceNumber())
				    .append("\n\tPayment Transaction: ").append(result.getPaymentTransactionNumber());
			}
			else {
				resultString.append("\nSubscribe Failure Result: \n");
				Error[] errors = result.getErrors();
				if (errors != null) {
					for (Error error : errors) {
						resultString.append("\n\tError Code: ").append(error.getCode().toString()).append("\n\tError Message: ")
						    .append(error.getMessage());
					}
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * Creates the print format of the Error message.
	 * 
	 * @param result
	 *          the result
	 * @return String
	 */
	public static String createErrorMessage(SaveResult result) {
		StringBuilder resultString = new StringBuilder("Error Message :\n");
		if (result != null && !result.getSuccess()) {
			Error[] errors = result.getErrors();
			if (errors != null) {
				for (Error error : errors) {
					resultString.append("\n\tError Code: ").append(error.getCode().toString()).append("\n\tError Message: ")
					    .append(error.getMessage());
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * Creates the print format of the Error message.
	 * 
	 * @param result
	 *          the AmendResult
	 * @return String
	 */
	public static String createErrorMessage(AmendResult result) {
		StringBuilder resultString = new StringBuilder("Error Message :\n");
		if (result != null && !result.getSuccess()) {
			Error[] errors = result.getErrors();
			if (errors != null) {
				for (Error error : errors) {
					resultString.append("\n\tError Code: ").append(error.getCode().toString()).append("\n\tError Message: ")
					    .append(error.getMessage());
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * Creates the error message.
	 * 
	 * @param result
	 *          the result
	 * @return the string
	 */
	public static String createErrorMessage(DeleteResult result) {
		StringBuilder resultString = new StringBuilder("Error Message :\n");
		if (result != null && !result.getSuccess()) {
			Error[] errors = result.getErrors();
			if (errors != null) {
				for (Error error : errors) {
					resultString.append("\n\tError Code: ").append(error.getCode().toString()).append("\n\tError Message: ")
					    .append(error.getMessage());
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * Creates the print format of the Error message.
	 * 
	 * @param result
	 *          the SubscribeResult
	 * @return String
	 */
	public static String createErrorMessage(SubscribeResult result) {
		StringBuilder resultString = new StringBuilder("Error Message :\n");
		if (result != null && !result.getSuccess()) {
			Error[] errors = result.getErrors();
			if (errors != null) {
				for (Error error : errors) {
					resultString.append("\n\tError Code: ").append(error.getCode().toString()).append("\n\tError Message: ")
					    .append(error.getMessage());
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * Prints the query result.
	 * 
	 * @param qResult
	 *          the q result
	 */
	public static void printQueryResult(QueryResult qResult) {
		StringBuilder resultString = new StringBuilder("Query Result : ");
		ZObject[] obj = qResult.getRecords();
		resultString.append("\n\tResults count:- " + obj.length);
		for (ZObject object : obj) {
			resultString.append("\n\t" + object.getId());
		}
		System.out.println(resultString + "\n");
	}

	/**
	 * Load properties.
	 */
	public void loadProperties() {
		String subscribeDataFileName = System.getProperty(FILE_PROPERTY_NAME);
		try {
			properties = new Properties();
			if (subscribeDataFileName != null) {
				properties.load(new FileInputStream(subscribeDataFileName));
			}
		}
		catch (IOException e) {
			print("Error while reading input data file: " + subscribeDataFileName);
			print(e.getMessage());
		}
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public Properties getProperties() {
		if (properties == null) {
			loadProperties();
		}
		return properties;
	}

	/**
	 * Gets the property value.
	 * 
	 * @param propertyName
	 *          the property name
	 * @return the property value
	 */
	public String getPropertyValue(String propertyName) {
		return getProperties().getProperty(propertyName);
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public static Calendar getCurrentDate() {
		return Calendar.getInstance();
	}

	/**
	 * Gets the end date.
	 * 
	 * @return the end date
	 */
	public static Calendar getEndDate() {
		Calendar calendarEndDate = Calendar.getInstance();
		calendarEndDate.add(Calendar.YEAR, 1);
		return calendarEndDate;
	}

	/**
	 * Gets the end date.
	 * 
	 * @return the end date
	 */
	public static Calendar getNextMonth() {
		Calendar nextMonthDate = Calendar.getInstance();
		nextMonthDate.add(Calendar.MONTH, 1);
		return nextMonthDate;
	}

	/**
	 * Decode base64.
	 * 
	 * @param strBody
	 *          the String body
	 * @return the byte[]
	 */
	public static byte[] decodeBase64(String strBody) {
		return Base64.decodeBase64(strBody.getBytes());
	}

	/**
	 * Write file.
	 * 
	 * @param data
	 *          the data
	 */
	public static void writeFile(byte[] data) {
		ZuoraUtility utility = new ZuoraUtility();
		new File(utility.getPropertyValue(FILE_PATH)).mkdirs();
		String strFilePath = utility.getPropertyValue(FILE_PATH) + "INV-" + System.currentTimeMillis() + ".pdf";
		try {
			FileOutputStream fos = new FileOutputStream(strFilePath);
			fos.write(data);
			fos.close();
			System.out.println("Invoice PDF generated @ " + strFilePath);
		}
		catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException : " + ex);
		}
		catch (IOException ioe) {
			System.out.println("IOException : " + ioe);
		}
	}

}
