package com.percussion.forum;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.percussion.rx.delivery.IPSDeliveryResult;
import com.percussion.rx.delivery.data.PSDeliveryResult;
import com.percussion.rx.delivery.impl.PSBaseDeliveryHandler;

public class DeliveryExample extends PSBaseDeliveryHandler {

	private static Log de_log = LogFactory.getLog(DeliveryExample.class);
	
	protected IPSDeliveryResult doDelivery(PSBaseDeliveryHandler.Item arg0, long arg1, String arg2)
			{
			try {
				Object localObject1 = null;
				if(arg0.getFile()!=null)
					localObject1 = new FileInputStream(arg0.getFile());
				else
					localObject1 = arg0.getResultStream();
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				DefaultHandler handler = new DefaultHandler() {
					boolean bfname = false;
					boolean blname = false;
					boolean bnname = false;
					boolean bsalary = false;
					
					public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
						System.out.println("Start Element :" + qName);
						if (qName.equalsIgnoreCase("FIRSTNAME")) {
							bfname = true;
						}
						if (qName.equalsIgnoreCase("LASTNAME")) {
							blname = true;
						}
						if (qName.equalsIgnoreCase("NICKNAME")) {
							bnname = true;
						}
						if (qName.equalsIgnoreCase("SALARY")) {
							bsalary = true;
						}
					}
				 
					public void endElement(String uri, String localName,
						String qName) throws SAXException {
						System.out.println("End Element :" + qName);
					}
					public void characters(char ch[], int start, int length) throws SAXException {
						if (bfname) {
							System.out.println("First Name : " + new String(ch, start, length));
							bfname = false;
						}
				 
						if (blname) {
							System.out.println("Last Name : " + new String(ch, start, length));
							blname = false;
						}
				 
						if (bnname) {
							System.out.println("Nick Name : " + new String(ch, start, length));
							bnname = false;
						}
				 
						if (bsalary) {
							System.out.println("Salary : " + new String(ch, start, length));
							bsalary = false;
						}
				 
					}
				 
				 };
				 saxParser.parse((InputStream)localObject1,handler);
				System.out.println(localObject1.toString());
				System.out.println(arg1);
				System.out.println(arg2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			StringBuilder str = new StringBuilder();

		    try
		    {
		      return new PSDeliveryResult(IPSDeliveryResult.Outcome.DELIVERED, null, arg0.getId(), arg1, arg0.getReferenceId(), str.toString().getBytes("UTF8"));
		    }
		    catch (UnsupportedEncodingException localUnsupportedEncodingException)
		    {
		      de_log.error("Problem delivering item", localUnsupportedEncodingException);
		      return (IPSDeliveryResult)new PSDeliveryResult(IPSDeliveryResult.Outcome.FAILED, localUnsupportedEncodingException.getLocalizedMessage(), arg0.getId(), arg1, arg0.getReferenceId(), null);
		    }
	}

	@Override
	protected IPSDeliveryResult doRemoval(PSBaseDeliveryHandler.Item arg0, long arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
