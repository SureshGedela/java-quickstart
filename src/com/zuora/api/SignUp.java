/*    Copyright (c) 2010 Zuora, Inc.
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

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zuora.api.axis2.ZuoraServiceStub.*;

public class SignUp {

   public static final String Name = "Name";
   public static final String FirstName = "FirstName";
   public static final String LastName = "LastName";
   public static final String WorkEmail = "WorkEmail";
   public static final String WorkPhone = "WorkPhone";
   public static final String Address1 = "Address1";
   public static final String Address2 = "Address2";
   public static final String City = "City";
   public static final String State = "State";
   public static final String Country = "Country";
   public static final String PostalCode = "PostalCode";
   public static final String CreditCardHolderName = "CreditCardHolderName";
   public static final String CreditCardNumber = "CreditCardNumber";
   public static final String CreditCardExpirationMonth = "CreditCardExpirationMonth";
   public static final String CreditCardExpirationYear = "CreditCardExpirationYear";
   public static final String CreditCardType = "CreditCardType";
   public static final String CreditCardPostalCode = "CreditCardPostalCode";

   private HttpServletRequest req;
   private HttpServletResponse resp;

   String _pId = null;
   String _prpId = null;
   String[] _chargeIds = null;

   private List<SelectItem> productsSelect = new ArrayList<SelectItem>();
   private List<SelectItem> ratePlansSelect = new ArrayList<SelectItem>();
   private List<SelectItem> chargesSelect = new ArrayList<SelectItem>();

   private ZuoraInterface zuora;
   private String status = "";

   public SignUp(HttpServletRequest req, HttpServletResponse resp) throws Exception {

      this.req = req;
      this.resp = resp;
      zuora = new ZuoraInterface();

      init();

      boolean valid = validate();
      if (valid) {
         subscribe();
      }

   }

   private void init() throws Exception {

      _pId = getValue("Products");
      _prpId = getValue("RatePlans");
      _chargeIds = getValues("Charges");

      SelectItem item = null;

      Product[] products = zuora.queryProducts();
      if (products != null && products.length > 0) {
         for (int i = 0; i < products.length; i++) {
            Product obj = products[i];
            item = new SelectItem(obj.getName(), obj.getId().getID());
            if (obj.getId() != null & obj.getId().getID().equals(_pId)) {
               item.selected = true;
            }
            productsSelect.add(item);
         }

         // if only one item, select it.
         if (products.length == 1) {
            _pId = products[0].getId().getID();
         }
      }

      if (ZuoraInterface.isValidId(_pId)) {
         ProductRatePlan[] prp = zuora.queryRatePlansByProduct(_pId);
         if (prp != null && prp.length > 0) {
            for (int i = 0; i < prp.length; i++) {
               ProductRatePlan obj = prp[i];
               item = new SelectItem(obj.getName(), obj.getId().getID());
               if (obj.getId() != null & obj.getId().getID().equals(_prpId)) {
                  item.selected = true;
               }
               ratePlansSelect.add(item);
            }

            // if only one item, select it.
            if (prp.length == 1) {
               _prpId = prp[0].getId().getID();
            }
         }
      }
      else {
         item = new SelectItem("-- SELECT A PRODUCT ABOVE -- ", null);
         ratePlansSelect.add(item);
      }

      if (ZuoraInterface.isValidId(_pId) && ZuoraInterface.isValidId(_prpId)) {
         ProductRatePlanCharge[] charges = zuora.queryChargesByProductRatePlan(_prpId);
         if (charges != null && charges.length > 0) {
            for (int i = 0; charges.length > 0 && i < charges.length; i++) {
               ProductRatePlanCharge obj = charges[i];

               String name = obj.getName() + (obj.getAccountingCode() != null ? " (" + obj.getAccountingCode() + ")" : "");
               item = new SelectItem(name, obj.getId().getID());
               item.selected = true;

               chargesSelect.add(item);
            }

            // if only one item, select it.
            if (charges.length == 1) {
               _chargeIds = new String[1];
               _chargeIds[0] = charges[0].getId().getID();
            }
         }
      }
      else {
         item = new SelectItem("-- SELECT A RATE PLAN ABOVE -- ", null);
         chargesSelect.add(item);
      }
   }

   private boolean validate() throws Exception {
      
      boolean valid = true;
      boolean isSubmit = req.getParameter("Submit") != null;
      if (!isSubmit) {
         return false;
      }
      else {
         
         if (_pId == null) {
            status = "Please select a Product.";
            return false;
         }
         if (_prpId == null) {
            status = "Please select a Rate Plan.";
            return false;
         }
         if (_chargeIds == null) {
            status = "Please select Charges.";
            return false;
         }

         valid = valid && validateValue(Name);
         valid = valid && validateValue(FirstName);
         valid = valid && validateValue(LastName);
         valid = valid && validateValue(WorkEmail);
         valid = valid && validateValue(WorkPhone);
         valid = valid && validateValue(Address1);
         // valid = valid && validateValue(Address2);
         valid = valid && validateValue(City);
         valid = valid && validateValue(State);
         valid = valid && validateValue(Country);
         valid = valid && validateValue(PostalCode);
         valid = valid && validateValue(CreditCardHolderName);
         valid = valid && validateValue(CreditCardNumber);
         valid = valid && validateValue(CreditCardExpirationMonth);
         valid = valid && validateValue(CreditCardExpirationYear);
         valid = valid && validateValue(CreditCardType);

      }
      return valid;
   }

   private void subscribe() throws Exception {

      int month = Integer.parseInt(getValue(CreditCardExpirationMonth));
      int year = Integer.parseInt(getValue(CreditCardExpirationYear));

      ProductRatePlanCharge[] charges = new ProductRatePlanCharge[_chargeIds.length];
      for (int i = 0; i < _chargeIds.length; i++) {
         ProductRatePlanCharge charge = new ProductRatePlanCharge();
         charge.setId(ZuoraInterface.makeID(_chargeIds[i]));
         charge.setProductRatePlanId(ZuoraInterface.makeID(_prpId));
         charges[i] = charge;
      }

      String subscriptionName = getValue(Name) + " New Signup (" + new Date().getTime() + ")";

      SubscribeResult result = zuora.subscribe(subscriptionName, charges, getValue(Name), getValue(FirstName), getValue(LastName), getValue(WorkEmail), getValue(WorkPhone), getValue(Address1),
         getValue(Address2), getValue(City), getValue(State), getValue(Country), getValue(PostalCode), getValue(CreditCardType), getValue(CreditCardNumber), getValue(CreditCardHolderName), month,
         year);

      String message = ZuoraInterface.createMessage(result);
      status = message;

   }

   private boolean validateValue(String param) {
      boolean empty = isEmpty(req.getParameter(param));
      if (empty) {
         status = status + param + " is a required value.<br>";
      }
      return !empty;
   }

   private boolean isEmpty(String val) {
      return val == null || val.trim().equals("");
   }

   private String[] getValues(String string) {
      return req.getParameterValues(string);
   }

   private String getValue(String string) {
      return getValue(string, null);
   }

   public String getValue(String string, String defaultValue) {
      String val = req.getParameter(string);
      if (val == null) val = defaultValue;
      return val;
   }
   
   public String getStatus() {
      return status;
   }

   public List<SelectItem> getProductsSelect() {
      return productsSelect;
   }

   public List<SelectItem> getRatePlansSelect() {
      return ratePlansSelect;
   }

   public List<SelectItem> getChargesSelect() {
      return chargesSelect;
   }

   public class SelectItem {

      String name;
      String value;
      boolean selected;

      public SelectItem(String n, String v) {
         this(n, v, false);
      }

      public SelectItem(String n, String v, boolean s) {
         this.name = n;
         this.value = v;
         this.selected = s;
      }

      public String getName() {
         return name;
      }

      public String getValue() {
         return value;
      }

      public boolean isSelected() {
         return selected;
      }

      public String getSelected() {
         return (selected ? " selected " : "");
      }

   }

}
