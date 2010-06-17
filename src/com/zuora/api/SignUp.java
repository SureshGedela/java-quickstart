package com.zuora.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zuora.api.axis2.ZuoraServiceStub.*;

public class SignUp {

   private HttpServletRequest req;
   private HttpServletResponse resp;
   
   String _pId = null;
   String _prpId = null;
   String[] _chargeIds = null;

   private List<SelectItem> productsSelect = new ArrayList<SelectItem>();
   private List<SelectItem> ratePlansSelect = new ArrayList<SelectItem>();
   private List<SelectItem> chargesSelect = new ArrayList<SelectItem>();

   private ZuoraInterface zuora;
   private String status = "Nothing";
   
   public SignUp(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      
      this.req = req;
      this.resp = resp;
      zuora = new ZuoraInterface();
      
      _pId = getValue("Products");
      _prpId = getValue("RatePlans");
      _chargeIds = getValues("Charges");
      
      SelectItem item = null;
      
      Product[] products = zuora.queryProducts();
      if (products != null && products.length > 0)
      {
          for (int i = 0; i < products.length; i++)
          {
              Product obj = products[i];
              item = new SelectItem(obj.getName(), obj.getId().getID());
              if (obj.getId() != null & obj.getId().equals(_pId))
              {
                  item.selected = true;
              }
              productsSelect.add(item);
          }

          // if only one item, select it.
          if (products.length == 1)
          {
              _pId = products[0].getId().getID();
          }
      }

      if (ZuoraInterface.isValidId(_pId))
      {
          ProductRatePlan[] prp = zuora.queryRatePlansByProduct(_pId);
          if (prp != null && prp.length > 0)
          {
              for (int i = 0; prp.length > 0 && i < prp.length; i++)
              {
                  ProductRatePlan obj = prp[i];
                  item = new SelectItem(obj.getName(), obj.getId().getID());
                  if (obj.getId() != null & obj.getId().equals(_prpId))
                  {
                      item.selected = true;
                  }
                  ratePlansSelect.add(item);
              }

              // if only one item, select it.
              if (prp.length == 1){
                  _prpId = prp[0].getId().getID();
              }
          }
      } 
      else
      {
          item = new SelectItem("-- SELECT A PRODUCT ABOVE -- ", null);
          ratePlansSelect.add(item);
      }

      if (ZuoraInterface.isValidId(_pId) && ZuoraInterface.isValidId(_prpId)) 
      {
          ProductRatePlanCharge[] charges = zuora.queryChargesByProductRatePlan(_prpId);
          if (charges != null && charges.length > 0)
          {
              for (int i = 0; charges.length > 0 && i < charges.length; i++)
              {
                  ProductRatePlanCharge obj = charges[i];
                  
                  String name = obj.getName() + (obj.getAccountingCode() != null ? " (" + obj.getAccountingCode() + ")" : "");
                  item = new SelectItem(name, obj.getId().getID());
                  item.selected = true;
                  
                  chargesSelect.add(item);
              }

              // if only one item, select it.
              if (charges.length == 1)
              {
                  _chargeIds = new String[1];
                  _chargeIds[0] = charges[0].getId().getID();
              }
          }
      }
      else
      {
          item = new SelectItem("-- SELECT A RATE PLAN ABOVE -- ", null);
          chargesSelect.add(item);
      }
      
   }
   
   private String[] getValues(String string) {
      return req.getParameterValues(string);
   }

   private String getValue(String string) {
      return req.getParameter(string);
   }

   public String getStatus(){
      return status;
   }
   
   public List<SelectItem> getProductsSelect() {
      return productsSelect;
   }

   public void setProductsSelect(List productsSelect) {
      this.productsSelect = productsSelect;
   }

   public List<SelectItem> getRatePlansSelect() {
      return ratePlansSelect;
   }

   public void setRatePlansSelect(List ratePlansSelect) {
      this.ratePlansSelect = ratePlansSelect;
   }

   public List<SelectItem> getChargesSelect() {
      return chargesSelect;
   }

   public void setChargesSelect(List chargesSelect) {
      this.chargesSelect = chargesSelect;
   }
   
   public class SelectItem {
      
      String name;
      String value;
      boolean selected;
      
      public SelectItem(String n, String v){
         this(n, v, false);
      }
      
      public SelectItem(String n, String v, boolean s){
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
