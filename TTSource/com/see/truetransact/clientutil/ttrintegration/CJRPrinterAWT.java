/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * JRPrintAWT.java
 *
 * Created on January 18, 2012, 1:13 PM
 */

package com.see.truetransact.clientutil.ttrintegration;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
 
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;

import com.see.truetransact.commonutil.ReadXMLForPrint; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
 
/**
 * @author RAJESH
 */

public class CJRPrinterAWT implements Printable
{
    private static final Log log = LogFactory.getLog(CJRPrinterAWT.class);
 
    /**
     *
     */
    private JasperPrint jasperPrint;
    private int pageOffset;
 
 
    /**
     *
     */
    protected CJRPrinterAWT(JasperPrint jrPrint) throws JRException
    {
        JRGraphEnvInitializer.initializeGraphEnv();
         
        jasperPrint = jrPrint;
    }
 
 
    /**
     *
     */
    public static boolean printPages(
        String reportName,
        JasperPrint jrPrint,
        int firstPageIndex,
        int lastPageIndex,
        boolean withPrintDialog
        ) throws JRException
    {
        CJRPrinterAWT printer = new CJRPrinterAWT(jrPrint);
        return printer.printPages(
            reportName,
            firstPageIndex,
            lastPageIndex,
            withPrintDialog
            );
    }
 
 
    /**
     *
     */
    public static Image printPageToImage(
        JasperPrint jrPrint,
        int pageIndex,
        float zoom
        ) throws JRException
    {
        CJRPrinterAWT printer = new CJRPrinterAWT(jrPrint);
        return printer.printPageToImage(pageIndex, zoom);
    }
 
 
    /**
     *
     */
    private boolean printPages(
        String reportName,
        int firstPageIndex,
        int lastPageIndex,
        boolean withPrintDialog
        ) throws JRException
    {
        boolean isOK = true;
 
        if (
            firstPageIndex < 0 ||
            firstPageIndex > lastPageIndex ||
            lastPageIndex >= jasperPrint.getPages().size()
            )
        {
            throw new JRException(
                "Invalid page index range : " +
                firstPageIndex + " - " +
                lastPageIndex + " of " +
                jasperPrint.getPages().size()
                );
        }
 
        pageOffset = firstPageIndex;
 
        PrinterJob printJob = PrinterJob.getPrinterJob();
 
        // fix for bug ID 6255588 from Sun bug database
        initPrinterJobFields(reportName, printJob);
         
        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = pageFormat.getPaper();
 
        printJob.setJobName("CBMS - " + jasperPrint.getName());
         
        switch (jasperPrint.getOrientation())
        {
            case net.sf.jasperreports.engine.JRReport.ORIENTATION_LANDSCAPE :
            {
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
                paper.setSize(jasperPrint.getPageHeight(), jasperPrint.getPageWidth());
                paper.setImageableArea(
                    0,
                    0,
                    jasperPrint.getPageHeight(),
                    jasperPrint.getPageWidth()
                    );
                break;
            }
            case
            net.sf.jasperreports.engine.JRReport.ORIENTATION_PORTRAIT :
            default :
            {
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                paper.setSize(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
                paper.setImageableArea(
                    0,
                    0,
                    jasperPrint.getPageWidth(),
                    jasperPrint.getPageHeight()
                    );
            }
        }
 
        pageFormat.setPaper(paper);
 
        Book book = new Book();
        book.append(this, pageFormat, lastPageIndex - firstPageIndex + 1);
        printJob.setPageable(book);
        try
        {
            if (withPrintDialog)
            {
                if (printJob.printDialog())
                {
                    printJob.print();
                }
                else
                {
                    isOK = false;
                }
            }
            else
            {
                printJob.print();
            }
        }
        catch (Exception ex)
        {
            throw new JRException("Error printing report.", ex);
        }
 
        return isOK;
    }
 
 
    /**
     *
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
    {
        if (Thread.interrupted())
        {
            throw new PrinterException("Current thread interrupted.");
        }
 
        pageIndex += pageOffset;
 
        if ( pageIndex < 0 || pageIndex >= jasperPrint.getPages().size() )
        {
            return Printable.NO_SUCH_PAGE;
        }
 
        try
        {
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
            exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, graphics);
            exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(pageIndex));
            exporter.exportReport();
        }
        catch (JRException e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Print failed.", e);
            }
 
            throw new PrinterException(e.getMessage()); //NOPMD
        }
 
        return Printable.PAGE_EXISTS;
    }
 
 
    /**
     *
     */
    private Image printPageToImage(int pageIndex, float zoom) throws JRException
    {
        Image pageImage = new BufferedImage(
            (int)(jasperPrint.getPageWidth() * zoom) + 1,
            (int)(jasperPrint.getPageHeight() * zoom) + 1,
            BufferedImage.TYPE_INT_RGB
            );
 
        JRGraphics2DExporter exporter = new JRGraphics2DExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
        exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());
        exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(pageIndex));
        exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, new Float(zoom));
        exporter.exportReport();
 
        return pageImage;
    }
 
 
    /**
     * Fix for bug ID 6255588 from Sun bug database
     * @param job print job that the fix applies to
     */
    public static void initPrinterJobFields(String reportName, PrinterJob job)
    {
        try
        {
            /* Create an array of PrintServices */
            javax.print.PrintService[] services = javax.print.PrintServiceLookup.lookupPrintServices(null, null);
            int selectedService = 0;
            /* Scan found services to see if anyone suits our needs */
            //added by rishad for 28/jan/2020
            final String pname = ReadXMLForPrint.readPrintXML(reportName);
            System.out.println("PrinterName" + pname);
            for(int i = 0; i < services.length;i++) {
                     if (services[i].getName().equals(pname)) {
//                if (services[i].getName().equals(ReadXMLForPrint.readPrintXML(reportName))) {
                    /*If the service is named as what we are querying we select it */
                    selectedService = i;
                }
            }
//            job.setPrintService(job.getPrintService());
            job.setPrintService(services[selectedService]);
        }
        catch (PrinterException e)
        {
        }
    }
     
     
    public static long getImageSize(JasperPrint jasperPrint, float zoom)
    {
        int width = (int) (jasperPrint.getPageWidth() * zoom) + 1;
        int height = (int) (jasperPrint.getPageHeight() * zoom) + 1;
        return width * height;
    }
}