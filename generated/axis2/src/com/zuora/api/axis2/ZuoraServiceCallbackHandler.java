
/**
 * ZuoraServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package com.zuora.api.axis2;

    /**
     *  ZuoraServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ZuoraServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ZuoraServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ZuoraServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for update method
            * override this method for handling normal response from update operation
            */
           public void receiveResultupdate(
                    com.zuora.api.axis2.ZuoraServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for amend method
            * override this method for handling normal response from amend operation
            */
           public void receiveResultamend(
                    com.zuora.api.axis2.ZuoraServiceStub.AmendResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from amend operation
           */
            public void receiveErroramend(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for create method
            * override this method for handling normal response from create operation
            */
           public void receiveResultcreate(
                    com.zuora.api.axis2.ZuoraServiceStub.CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from create operation
           */
            public void receiveErrorcreate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for execute method
            * override this method for handling normal response from execute operation
            */
           public void receiveResultexecute(
                    com.zuora.api.axis2.ZuoraServiceStub.ExecuteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from execute operation
           */
            public void receiveErrorexecute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryMore method
            * override this method for handling normal response from queryMore operation
            */
           public void receiveResultqueryMore(
                    com.zuora.api.axis2.ZuoraServiceStub.QueryMoreResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryMore operation
           */
            public void receiveErrorqueryMore(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for query method
            * override this method for handling normal response from query operation
            */
           public void receiveResultquery(
                    com.zuora.api.axis2.ZuoraServiceStub.QueryResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from query operation
           */
            public void receiveErrorquery(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for generate method
            * override this method for handling normal response from generate operation
            */
           public void receiveResultgenerate(
                    com.zuora.api.axis2.ZuoraServiceStub.GenerateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from generate operation
           */
            public void receiveErrorgenerate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUserInfo method
            * override this method for handling normal response from getUserInfo operation
            */
           public void receiveResultgetUserInfo(
                    com.zuora.api.axis2.ZuoraServiceStub.GetUserInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUserInfo operation
           */
            public void receiveErrorgetUserInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delete method
            * override this method for handling normal response from delete operation
            */
           public void receiveResultdelete(
                    com.zuora.api.axis2.ZuoraServiceStub.DeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delete operation
           */
            public void receiveErrordelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for subscribe method
            * override this method for handling normal response from subscribe operation
            */
           public void receiveResultsubscribe(
                    com.zuora.api.axis2.ZuoraServiceStub.SubscribeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from subscribe operation
           */
            public void receiveErrorsubscribe(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for login method
            * override this method for handling normal response from login operation
            */
           public void receiveResultlogin(
                    com.zuora.api.axis2.ZuoraServiceStub.LoginResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from login operation
           */
            public void receiveErrorlogin(java.lang.Exception e) {
            }
                


    }
    