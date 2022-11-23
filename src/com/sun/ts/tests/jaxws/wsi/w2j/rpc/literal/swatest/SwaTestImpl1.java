/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

// This class was generated by the JAXWS SI, do not edit.

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest;

import jakarta.activation.*;
import jakarta.jws.WebService;
import jakarta.xml.soap.*;
import jakarta.xml.ws.*;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.soap.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

@WebService(portName = "SwaTestOnePort", serviceName = "WSIRLSwaTestService", targetNamespace = "http://SwaTestService.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JRLSwaTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.SwaTest1")
public class SwaTestImpl1 implements SwaTest1 {
    public void getMultipleAttachments(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequestGet request,
            jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponse> response,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2) {
        try {
            System.out.println("Enter getMultipleAttachments() ......");
            OutputResponse theResponse = new OutputResponse();
            theResponse.setMimeType1(request.getMimeType1());
            theResponse.setMimeType2(request.getMimeType2());
            theResponse.setResult("ok");
            theResponse.setReason("ok");
            DataHandler dh1 = new DataHandler(new URL(request.getUrl1()));
            DataHandler dh2 = new DataHandler(new URL(request.getUrl2()));
            attach1.value = dh1;
            attach2.value = dh2;
            response.value = theResponse;
            System.out.println("Leave getMultipleAttachments() ......");
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public java.lang.String putMultipleAttachments(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequestPut request,
            jakarta.activation.DataHandler attach1,
            jakarta.activation.DataHandler attach2) {
        try {
            String response = "ok";
            System.out.println("Enter putMultipleAttachments() ......");
            if (attach1 == null) {
                System.err.println("attach1 is null (unexpected)");
                response = "not ok";
            }
            if (attach2 == null) {
                System.err.println("attach2 is null (unexpected)");
                response = "not ok";
            }
            System.out.println("Leave putMultipleAttachments() ......");
            return response;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponse echoMultipleAttachments(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequest request,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2) {
        try {
            System.out.println("Enter echoMultipleAttachments() ......");
            OutputResponse theResponse = new OutputResponse();
            theResponse.setMimeType1(request.getMimeType1());
            theResponse.setMimeType2(request.getMimeType2());
            theResponse.setResult("ok");
            theResponse.setReason("ok");
            if (attach1 == null || attach1.value == null) {
                System.err.println("attach1.value is null (unexpected)");
                theResponse.setReason("attach1.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach2 == null || attach2.value == null) {
                System.err.println("attach2.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach2.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach2.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            System.out.println("Leave echoMultipleAttachments() ......");
            return theResponse;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public String echoGifImageType(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.VoidRequest request,
            jakarta.xml.ws.Holder<java.awt.Image> attach1) {
        try {
            String theResponse = "ok";
            System.out.println("Enter echoGifImageType() ......");
            if (attach1 == null || attach1.value == null) {
                System.err.println("attach1 is null (unexpected)");
                theResponse = "not ok";
            }
            System.out.println("Leave echoGifImageType() ......");
            return theResponse;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public java.lang.String echoNoAttachments(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequestString request) {
        try {
            System.out.println("Enter echoNoAttachments() ......");
            String response = request.getMyString();
            System.out.println("Leave echoNoAttachments() ......");
            return response;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponseAll echoAllAttachmentTypes(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.VoidRequest request,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2,
            jakarta.xml.ws.Holder<javax.xml.transform.Source> attach3,
            jakarta.xml.ws.Holder<java.awt.Image> attach4,
            jakarta.xml.ws.Holder<java.awt.Image> attach5,
            jakarta.xml.ws.Holder<javax.xml.transform.Source> attach6) {
        try {
            System.out.println("Enter echoAllAttachmentTypes() ......");
            OutputResponseAll theResponse = new OutputResponseAll();
            theResponse.setResult("ok");
            theResponse.setReason("ok");
            if (attach1 == null || attach1.value == null) {
                System.err.println("attach1.value is null (unexpected)");
                theResponse.setReason("attach1.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach2 == null || attach2.value == null) {
                System.err.println("attach2.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach2.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach2.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach3 == null || attach3.value == null) {
                System.err.println("attach3.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach3.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach3.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach4 == null || attach4.value == null) {
                System.err.println("attach4.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach4.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach4.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach5 == null || attach5.value == null) {
                System.err.println("attach5.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach5.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach5.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            if (attach6 == null || attach6.value == null) {
                System.err.println("attach6.value is null (unexpected)");
                if (theResponse.getReason().equals("ok"))
                    theResponse.setReason("attach6.value is null (unexpected)");
                else
                    theResponse.setReason(theResponse.getReason() + "\nattach6.value is null (unexpected)");
                theResponse.setResult("not ok");
            }
            System.out.println("Leave echoAllAttachmentTypes() ......");
            return theResponse;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public void getAllAttachmentTypes(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequestGetAll request,
            jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponseGetAll> response,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2,
            jakarta.xml.ws.Holder<javax.xml.transform.Source> attach3,
            jakarta.xml.ws.Holder<java.awt.Image> attach4,
            jakarta.xml.ws.Holder<java.awt.Image> attach5,
            jakarta.xml.ws.Holder<javax.xml.transform.Source> attach6) {
        try {
            System.out.println("Enter getAllAttachmentTypes() ......");
            OutputResponseGetAll theResponse = new OutputResponseGetAll();
            theResponse.setMimeType1(request.getMimeType1());
            theResponse.setMimeType2(request.getMimeType2());
            theResponse.setMimeType3(request.getMimeType3());
            theResponse.setMimeType4(request.getMimeType4());
            theResponse.setMimeType5(request.getMimeType5());
            theResponse.setMimeType6(request.getMimeType6());
            theResponse.setResult("ok");
            theResponse.setReason("ok");
            DataHandler dh1 = new DataHandler(new URL(request.getUrl1()));
            DataHandler dh2 = new DataHandler(new URL(request.getUrl2()));
            DataHandler dh3 = new DataHandler(new URL(request.getUrl3()));
            DataHandler dh6 = new DataHandler(new URL(request.getUrl6()));
            attach1.value = dh1;
            attach2.value = dh2;
            attach3.value = new StreamSource(dh3.getInputStream());
            attach4.value = javax.imageio.ImageIO.read(new URL(request.getUrl4()));
            attach5.value = javax.imageio.ImageIO.read(new URL(request.getUrl5()));
            attach6.value = new StreamSource(dh6.getInputStream());
            System.out.println("Leave getAllAttachmentTypes() ......");
            response.value = theResponse;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public java.lang.String putAllAttachmentTypes(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequestPutAll request,
            jakarta.activation.DataHandler attach1,
            jakarta.activation.DataHandler attach2,
            javax.xml.transform.Source attach3,
            java.awt.Image attach4,
            java.awt.Image attach5,
            javax.xml.transform.Source attach6) {
        try {
            String response = "ok";
            System.out.println("Enter putAllAttachmentTypes() ......");
            if (attach1 == null) {
                System.err.println("attach1 is null (unexpected)");
                response = "not ok";
            }
            if (attach2 == null) {
                System.err.println("attach2 is null (unexpected)");
                response = "not ok";
            }
            if (attach3 == null) {
                System.err.println("attach3 is null (unexpected)");
                response = "not ok";
            }
            if (attach4 == null) {
                System.err.println("attach4 is null (unexpected)");
                response = "not ok";
            }
            if (attach5 == null) {
                System.err.println("attach5 is null (unexpected)");
                response = "not ok";
            }
            if (attach6 == null) {
                System.err.println("attach6 is null (unexpected)");
                response = "not ok";
            }
            System.out.println("Leave putAllAttachmentTypes() ......");
            return response;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponse echoAttachmentsAndThrowAFault(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequest request,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2)
            throws com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.MyFault {
        System.out.println("Enter echoAttachmentsAndThrowAFault() ......");
        System.out.println("Throwing back a fault [MyFault] ......");
        throw new MyFault("This is my fault", new MyFaultType());
    }

    public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.OutputResponse echoAttachmentsWithHeader(
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.InputRequest request,
            com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.MyHeader header,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
            jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2)
            throws com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.swatest.MyFault {
        System.out.println("Enter echoAttachmentsWithHeader() ......");
        if (header.getMessage().equals("do throw a fault")) {
            System.out.println("Throwing back a fault [MyFault] ......");
            throw new MyFault("This is my fault", new MyFaultType());
        }
        try {
            OutputResponse theResponse = new OutputResponse();
            theResponse.setMimeType1(request.getMimeType1());
            theResponse.setMimeType2(request.getMimeType2());
            theResponse.setResult("ok");
            theResponse.setReason("ok");
            System.out.println("Leave echoAttachmentsWithHeader() ......");
            return theResponse;
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }
}
