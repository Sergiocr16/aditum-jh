package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;


@Service
@Transactional
public class IndicadoresEconomicosBccr {


    public ExchangeRateBccr obtenerTipodeCambio(ZonedDateTime fechaInicio, ZonedDateTime fechaFinal) throws IOException, ParserConfigurationException, SAXException {
        DecimalFormat df2 = new DecimalFormat("#.##");
        String FechaInicio = fechaInicio.getDayOfMonth() + "/" + fechaInicio.getMonthValue() + "/" + fechaInicio.getYear();
        String FechaFinal = fechaFinal.getDayOfMonth() + "/" + fechaFinal.getMonthValue() + "/" + fechaFinal.getYear();
        final String baseUrl = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos?Indicador=317&CorreoElectronico=partners@aditumcr.com&Token=0D0TAMMAAC&FechaInicio=" + FechaInicio + "&FechaFinal=" + FechaFinal + "&Nombre=N&SubNiveles=N";
        URL url = new URL(baseUrl);
        URLConnection conn = url.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(conn.getInputStream());
        String compra = df2.format(Double.parseDouble(doc.getElementsByTagName("NUM_VALOR").item(0).getChildNodes().item(0).getNodeValue()))+"";
        final String baseUrl2 = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos?Indicador=318&CorreoElectronico=partners@aditumcr.com&Token=0D0TAMMAAC&FechaInicio=" + FechaInicio + "&FechaFinal=" + FechaFinal + "&Nombre=N&SubNiveles=N";
        URL url2 = new URL(baseUrl2);
        URLConnection conn2 = url2.openConnection();
        DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder2 = factory2.newDocumentBuilder();
        Document doc2 = builder2.parse(conn2.getInputStream());
        String venta = df2.format(Double.parseDouble(doc2.getElementsByTagName("NUM_VALOR").item(0).getChildNodes().item(0).getNodeValue()))+"";
        ExchangeRateBccr ex = new ExchangeRateBccr();
        ex.setCompra(compra);
        ex.setVenta(venta);
        return ex;
    }
}
