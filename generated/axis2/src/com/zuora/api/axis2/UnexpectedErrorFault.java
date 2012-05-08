
/**
 * UnexpectedErrorFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package com.zuora.api.axis2;

public class UnexpectedErrorFault extends java.lang.Exception{
    
    private com.zuora.api.axis2.ZuoraServiceStub.UnexpectedErrorFaultE faultMessage;
    
    public UnexpectedErrorFault() {
        super("UnexpectedErrorFault");
    }
           
    public UnexpectedErrorFault(java.lang.String s) {
       super(s);
    }
    
    public UnexpectedErrorFault(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.zuora.api.axis2.ZuoraServiceStub.UnexpectedErrorFaultE msg){
       faultMessage = msg;
    }
    
    public com.zuora.api.axis2.ZuoraServiceStub.UnexpectedErrorFaultE getFaultMessage(){
       return faultMessage;
    }
}
    