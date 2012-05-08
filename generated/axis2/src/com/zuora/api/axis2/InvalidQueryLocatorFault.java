
/**
 * InvalidQueryLocatorFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package com.zuora.api.axis2;

public class InvalidQueryLocatorFault extends java.lang.Exception{
    
    private com.zuora.api.axis2.ZuoraServiceStub.InvalidQueryLocatorFaultE faultMessage;
    
    public InvalidQueryLocatorFault() {
        super("InvalidQueryLocatorFault");
    }
           
    public InvalidQueryLocatorFault(java.lang.String s) {
       super(s);
    }
    
    public InvalidQueryLocatorFault(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.zuora.api.axis2.ZuoraServiceStub.InvalidQueryLocatorFaultE msg){
       faultMessage = msg;
    }
    
    public com.zuora.api.axis2.ZuoraServiceStub.InvalidQueryLocatorFaultE getFaultMessage(){
       return faultMessage;
    }
}
    