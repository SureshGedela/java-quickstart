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

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;

import com.zuora.api.axis2.ZuoraServiceStub;
import com.zuora.api.axis2.ZuoraServiceStub.Amend;
import com.zuora.api.axis2.ZuoraServiceStub.AmendResponse;
import com.zuora.api.axis2.ZuoraServiceStub.AmendResult;
import com.zuora.api.axis2.ZuoraServiceStub.CallOptions;
import com.zuora.api.axis2.ZuoraServiceStub.Create;
import com.zuora.api.axis2.ZuoraServiceStub.CreateResponse;
import com.zuora.api.axis2.ZuoraServiceStub.Delete;
import com.zuora.api.axis2.ZuoraServiceStub.DeleteResponse;
import com.zuora.api.axis2.ZuoraServiceStub.ID;
import com.zuora.api.axis2.ZuoraServiceStub.Login;
import com.zuora.api.axis2.ZuoraServiceStub.LoginResponse;
import com.zuora.api.axis2.ZuoraServiceStub.LoginResult;
import com.zuora.api.axis2.ZuoraServiceStub.SessionHeader;
import com.zuora.api.axis2.ZuoraServiceStub.Update;
import com.zuora.api.axis2.ZuoraServiceStub.UpdateResponse;
import com.zuora.api.axis2.ZuoraServiceStub.ZObject;

/**
 * The Class ZuoraAPIHelper.
 */
public class ZuoraAPIHelper {

	/** The Constant PROPERTY_ENDPOINT. */
	private static final String PROPERTY_ENDPOINT = "endpoint";

	/** The Constant PROPERTY_USERNAME. */
	private static final String PROPERTY_USERNAME = "username";

	/** The Constant PROPERTY_PASSWORD. */
	private static final String PROPERTY_PASSWORD = "password";

	/** The stub. */
	private ZuoraServiceStub stub;

	/** The header. */
	private SessionHeader header;

	/** The call options. */
	private CallOptions callOptions;

	/**
	 * Creates the ZObject.
	 * 
	 * @param obj
	 *          the ZObject
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID create(ZObject obj) throws Exception {

		Create create = new Create();
		create.setZObjects(new ZObject[] { obj });
		CreateResponse cResponse = stub.create(create, this.callOptions, this.header);
		ZuoraServiceStub.SaveResult result = cResponse.getResult()[0];
		if (!result.getSuccess()) {
			ZuoraUtility.print("Create Status : Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(result));
		}
		else {
			ZuoraUtility.print("Create Status : Success");
		}
		ID id = result.getId();
		return id;
	}

	/**
	 * Updates the ZObject.
	 * 
	 * @param obj
	 *          the ZObject
	 * @return the iD
	 * @throws Exception
	 *           the exception
	 */
	public ID update(ZObject obj) throws Exception {

		Update update = new Update();
		update.setZObjects(new ZObject[] { obj });

		UpdateResponse cResponse = stub.update(update, this.header);
		ZuoraServiceStub.SaveResult result = cResponse.getResult()[0];
		if (!result.getSuccess()) {
			ZuoraUtility.print("Update Status : Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(result));
		}
		else {
			ZuoraUtility.print("Update Status : Success");
		}
		ID id = result.getId();
		return id;
	}

	/**
	 * Delete Zuora object for Type and ID.
	 * 
	 * @param type
	 *          the type
	 * @param id
	 *          the ID
	 * @return true, if successful
	 * @throws Exception
	 *           the exception
	 */
	public boolean delete(String type, ID id) throws Exception {

		Delete delete = new Delete();
		delete.setType(type);
		delete.setIds(new ID[] { id });
		DeleteResponse cResponse = stub.delete(delete, this.header);
		ZuoraServiceStub.DeleteResult result = cResponse.getResult()[0];
		if (result.getSuccess()) {
			ZuoraUtility.print("Delete Status : Success");
		}
		else {
			ZuoraUtility.print("Delete Status : Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(result));
		}
		return result.getSuccess();
	}

	/**
	 * Amend.
	 * 
	 * @param amend
	 *          the amend
	 * @return the amend response
	 * @throws Exception
	 *           the exception
	 */
	public AmendResponse amend(Amend amend) throws Exception {

		AmendResponse resp = stub.amend(amend, this.header);
		AmendResult amendResult = resp.getResults()[0];
		if (amendResult.getErrors() == null) {
			ZuoraUtility.print("Amend status: Success");
		}
		else {
			ZuoraUtility.print("Amend Status : Fail");
			ZuoraUtility.print(ZuoraUtility.createErrorMessage(amendResult));
		}
		return resp;
	}

	/**
	 * Login.
	 * 
	 * @throws Exception
	 *           the exception
	 */
	public void login() throws Exception {

		Login login = new Login();
		ZuoraUtility utility = new ZuoraUtility();
		login.setUsername(utility.getPropertyValue(PROPERTY_USERNAME));
		login.setPassword(utility.getPropertyValue(PROPERTY_PASSWORD));
		LoginResponse resp = stub.login(login);
		LoginResult result = resp.getResult();
		// create session for all subsequent calls
		this.header = new SessionHeader();
		this.header.setSession(result.getSession());
	}

	/**
	 * Instantiates a new zuora api helper.
	 * 
	 * @throws AxisFault
	 *           the axis fault
	 */
	public ZuoraAPIHelper() throws AxisFault {
		try {
			this.stub = new ZuoraServiceStub();
			// set new ENDPOINT
			ZuoraUtility utility = new ZuoraUtility();
			String endpoint = utility.getPropertyValue(PROPERTY_ENDPOINT);
			// ZuoraUtility.print("PROPERTY_ENDPOINT :"+ endpoint);
			if (endpoint != null && endpoint.trim().length() > 0) {
				ServiceClient client = stub._getServiceClient();
				client.getOptions().getTo().setAddress(endpoint);
			}

		}
		catch (AxisFault e) {
			ZuoraUtility.print(e.getMessage());
			throw e;
		}
	}

	/**
	 * Change endpoint.
	 * 
	 * @return true, if successful
	 */
	public boolean changeEndpoint() {
		return false;
	}

	/**
	 * Gets the stub.
	 * 
	 * @return the stub
	 */
	public ZuoraServiceStub getStub() {
		return stub;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public SessionHeader getHeader() {
		return header;
	}
}
