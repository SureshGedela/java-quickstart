
/**
 * InvalidValueFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package com.zuora.api.axis2;

public class InvalidValueFault extends java.lang.Exception{
    
    private com.zuora.api.axis2.ZuoraServiceStub.InvalidValueFaultE faultMessage;
    
    public InvalidValueFault() {
        super("InvalidValueFault");
    }
           
    public InvalidValueFault(java.lang.String s) {
       super(s);
    }
    
    public InvalidValueFault(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.zuora.api.axis2.ZuoraServiceStub.InvalidValueFaultE msg){
       faultMessage = msg;
    }
    
    public com.zuora.api.axis2.ZuoraServiceStub.InvalidValueFaultE getFaultMessage(){
       return faultMessage;
    }
}
    