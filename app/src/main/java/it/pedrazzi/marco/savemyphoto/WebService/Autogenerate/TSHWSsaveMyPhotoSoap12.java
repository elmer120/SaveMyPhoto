package it.pedrazzi.marco.savemyphoto.WebService.Autogenerate;




//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 5.0.8.2
//
// Created by Quasar Development at 05/05/2017
//
//---------------------------------------------------




import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.*;
import org.kxml2.kdom.Element;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TSHWSsaveMyPhotoSoap12
{
    interface TSHIWcfMethod
    {
        TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope() throws java.lang.Exception;

        java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object result) throws java.lang.Exception;
    }

    String url="http://savemyphoto.gear.host/WSsaveMyphoto.asmx";
    //String url="http://192.168.1.20:51262/WSsaveMyphoto.asmx";
    int timeOut=60000;
    public List< HeaderProperty> httpHeaders;
    public boolean enableLogging;

    public TSHWSsaveMyPhotoSoap12(){}

    public TSHWSsaveMyPhotoSoap12(String url)
    {
        this.url = url;
    }

    public TSHWSsaveMyPhotoSoap12(String url,int timeOut)
    {
        this.url = url;
        this.timeOut=timeOut;
    }

    protected org.ksoap2.transport.Transport createTransport()
    {
        try
        {
            java.net.URI uri = new java.net.URI(url);
            if(uri.getScheme().equalsIgnoreCase("https"))
            {
                int port=uri.getPort()>0?uri.getPort():443;
                return new HttpsTransportSE(uri.getHost(), port, uri.getPath(), timeOut);
            }
            else
            {
                return new HttpTransportSE(url,timeOut);
            }

        }
        catch (java.net.URISyntaxException e)
        {
        }
        return null;
    }
    
    protected TSHExtendedSoapSerializationEnvelope createEnvelope()
    {
        TSHExtendedSoapSerializationEnvelope envelope= new TSHExtendedSoapSerializationEnvelope(TSHExtendedSoapSerializationEnvelope.VER12);
        return envelope;
    }
    
    protected java.util.List sendRequest(String methodName,TSHExtendedSoapSerializationEnvelope envelope,org.ksoap2.transport.Transport transport  )throws java.lang.Exception
    {
        return transport.call(methodName, envelope, httpHeaders);
    }

    java.lang.Object getResult(java.lang.Class destObj,java.lang.Object source,String resultName,TSHExtendedSoapSerializationEnvelope __envelope) throws java.lang.Exception
    {
        if(source==null)
        {
            return null;
        }
        if(source instanceof SoapPrimitive)
        {
            SoapPrimitive soap =(SoapPrimitive)source;
            if(soap.getName().equals(resultName))
            {
                java.lang.Object instance=__envelope.get(source,destObj,false);
                return instance;
            }
        }
        else
        {
            SoapObject soap = (SoapObject)source;
            if (soap.hasProperty(resultName))
            {
                java.lang.Object j=soap.getProperty(resultName);
                if(j==null)
                {
                    return null;
                }
                java.lang.Object instance=__envelope.get(j,destObj,false);
                return instance;
            }
            else if( soap.getName().equals(resultName)) {
                java.lang.Object instance=__envelope.get(source,destObj,false);
                return instance;
            }
       }

       return null;
    }

        
    public Boolean CredenzialiCheck(final String nomeUtente,final String password ) throws java.lang.Exception
    {
        return (Boolean)execute(new TSHIWcfMethod()
        {
            @Override
            public TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              TSHExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://it.pedrazzi.marco.savemyphoto/", "CredenzialiCheck");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeUtente";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeUtente!=null?nomeUtente:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="password";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(password!=null?password:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("CredenzialiCheckResult");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return new Boolean(j.toString());
                }
                else if (obj!= null && obj instanceof Boolean){
                    return (Boolean)obj;
                }
                return null;
            }
        },"http://it.pedrazzi.marco.savemyphoto/CredenzialiCheck");
    }
    
    public Integer AssociaNuovoDispositivo(final String marca,final String modello,final String versioneAndroid,final Integer spazioLibero,final String nomeUtente ) throws java.lang.Exception
    {
        return (Integer)execute(new TSHIWcfMethod()
        {
            @Override
            public TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              TSHExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://it.pedrazzi.marco.savemyphoto/", "AssociaNuovoDispositivo");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="marca";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(marca!=null?marca:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="modello";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(modello!=null?modello:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="versioneAndroid";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(versioneAndroid!=null?versioneAndroid:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="spazioLibero";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(spazioLibero);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeUtente";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeUtente!=null?nomeUtente:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("AssociaNuovoDispositivoResult");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return Integer.parseInt(j.toString());
                }
                else if (obj!= null && obj instanceof Integer){
                    return (Integer)obj;
                }
                return null;
            }
        },"http://it.pedrazzi.marco.savemyphoto/AssociaNuovoDispositivo");
    }
    
    public Integer RegistrazioneNuovoUtente(final String nomeUtente,final String mail,final String password,final String marca,final String modello,final String versioneAndroid,final Integer spazioLibero ) throws java.lang.Exception
    {
        return (Integer)execute(new TSHIWcfMethod()
        {
            @Override
            public TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              TSHExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://it.pedrazzi.marco.savemyphoto/", "RegistrazioneNuovoUtente");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeUtente";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeUtente!=null?nomeUtente:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="mail";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(mail!=null?mail:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="password";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(password!=null?password:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="marca";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(marca!=null?marca:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="modello";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(modello!=null?modello:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="versioneAndroid";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(versioneAndroid!=null?versioneAndroid:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="spazioLibero";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(spazioLibero);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("RegistrazioneNuovoUtenteResult");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return Integer.parseInt(j.toString());
                }
                else if (obj!= null && obj instanceof Integer){
                    return (Integer)obj;
                }
                return null;
            }
        },"http://it.pedrazzi.marco.savemyphoto/RegistrazioneNuovoUtente");
    }
    
    public Boolean AggiungiMedia(final String nomeFile,final String album,final java.util.Date dataAcquisizione,final Integer dimensione,final String nomeUtente,final Integer altezza,final Integer larghezza,final String formato,final String orientamento,final Integer gpsLat,final Integer gpsLong,final Integer fkDispositivo ) throws java.lang.Exception
    {
        return (Boolean)execute(new TSHIWcfMethod()
        {
            @Override
            public TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              TSHExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://it.pedrazzi.marco.savemyphoto/", "AggiungiMedia");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeFile";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeFile!=null?nomeFile:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="album";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(album!=null?album:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="dataAcquisizione";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(dataAcquisizione!=null?TSHHelper.getDateTimeFormat().format(dataAcquisizione):SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="dimensione";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(dimensione);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeUtente";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeUtente!=null?nomeUtente:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="altezza";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(altezza);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="larghezza";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(larghezza);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="formato";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(formato!=null?formato:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="orientamento";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(orientamento!=null?orientamento:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="gpsLat";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(gpsLat);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="gpsLong";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(gpsLong);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="fkDispositivo";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(fkDispositivo);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("AggiungiMediaResult");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return new Boolean(j.toString());
                }
                else if (obj!= null && obj instanceof Boolean){
                    return (Boolean)obj;
                }
                return null;
            }
        },"http://it.pedrazzi.marco.savemyphoto/AggiungiMedia");
    }
    
    public Boolean AggiornaMedia(final Integer fkDispositivo,final String nomeFile ) throws java.lang.Exception
    {
        return (Boolean)execute(new TSHIWcfMethod()
        {
            @Override
            public TSHExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              TSHExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://it.pedrazzi.marco.savemyphoto/", "AggiornaMedia");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="fkDispositivo";
                __info.type=PropertyInfo.INTEGER_CLASS;
                __info.setValue(fkDispositivo);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://it.pedrazzi.marco.savemyphoto/";
                __info.name="nomeFile";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(nomeFile!=null?nomeFile:SoapPrimitive.NullSkip);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(TSHExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                SoapObject __soap=(SoapObject)__result;
                java.lang.Object obj = __soap.getProperty("AggiornaMediaResult");
                if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return new Boolean(j.toString());
                }
                else if (obj!= null && obj instanceof Boolean){
                    return (Boolean)obj;
                }
                return null;
            }
        },"http://it.pedrazzi.marco.savemyphoto/AggiornaMedia");
    }
    
    public String ResetSistema( ) throws java.lang.Exception
    {
/*This feature is available in Premium account, Check http://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account*/
        return null;    
    }
    
    public String HelloWorld( ) throws java.lang.Exception
    {
/*This feature is available in Premium account, Check http://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account*/
        return null;    
    }
    
    public String HelloWorldP(final String i ) throws java.lang.Exception
    {
/*This feature is available in Premium account, Check http://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account*/
        return null;    
    }
    
    public String HelloWorldTest( ) throws java.lang.Exception
    {
/*This feature is available in Premium account, Check http://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account*/
        return null;    
    }
    
    public String HelloWorldTestTest( ) throws java.lang.Exception
    {
/*This feature is available in Premium account, Check http://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account*/
        return null;    
    }

    
    protected java.lang.Object execute(TSHIWcfMethod wcfMethod,String methodName) throws java.lang.Exception
    {
        org.ksoap2.transport.Transport __httpTransport=createTransport();
        __httpTransport.debug=enableLogging;
        TSHExtendedSoapSerializationEnvelope __envelope=wcfMethod.CreateSoapEnvelope();
        try
        {
            sendRequest(methodName, __envelope, __httpTransport);
            
        }
        finally {
            if (__httpTransport.debug) {
                if (__httpTransport.requestDump != null) {
                    android.util.Log.i("requestDump",__httpTransport.requestDump);    
                    
                }
                if (__httpTransport.responseDump != null) {
                    android.util.Log.i("responseDump",__httpTransport.responseDump);
                }
            }
        }
        java.lang.Object __retObj = __envelope.bodyIn;
        if (__retObj instanceof org.ksoap2.SoapFault){
            org.ksoap2.SoapFault __fault = (org.ksoap2.SoapFault)__retObj;
            throw convertToException(__fault,__envelope);
        }else{
            return wcfMethod.ProcessResult(__envelope,__retObj);
        }
    }
    
        
    java.lang.Exception convertToException(org.ksoap2.SoapFault fault,TSHExtendedSoapSerializationEnvelope envelope)
    {

        return new java.lang.Exception(fault.faultstring);
    }
}


