package com.fish.test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @descript:
 * @author: fjjDragon
 * @create: 2021-06-23 10:46
 **/
public class TestItextpdf {

    public static void testPdf(String filePath, String targetFile) {
        PdfReader pdfReader = null;
        PdfStamper ps = null;
        FileOutputStream fos = null;
        try {
            pdfReader = new PdfReader(filePath);
            HashMap<String, String> info = pdfReader.getInfo();
            System.out.println(info);

//            PdfDictionary asDict = pdfReader.getTrailer().getAsDict(PdfName.INFO);
//            PdfString pdfObject = (PdfString) asDict.get(PdfName.TITLE);

            //创建pdf模板，参数reader  bos
            File file = new File(targetFile);
            fos = new FileOutputStream(file);//创建文件输出流
            ps = new PdfStamper(pdfReader, fos);

            ps.setFormFlattening(true);
            PdfDictionary asDict1 = ps.getReader().getTrailer().getAsDict(PdfName.INFO);
            if (null != asDict1) {
                PdfString pdfString = (PdfString) asDict1.get(PdfName.TITLE);
                System.out.println(pdfString.toUnicodeString());
                Class<? extends PdfString> aClass = pdfString.getClass();

                Field bytes = aClass.getSuperclass().getDeclaredField("bytes");
                boolean temp = bytes.canAccess(pdfString);
                if (!temp) {
                    bytes.setAccessible(true);
                    bytes.set(pdfString, PdfEncodings.convertToBytes("test1234", PdfObject.TEXT_UNICODE));
                    bytes.setAccessible(temp);
                }


//                Field field = aClass.getDeclaredField("value");
//                boolean temp = field.canAccess(pdfString);
//                if (!temp) {
//                    field.setAccessible(true);
//                    Object o = field.get(pdfString);
//                    String test1234 = new String(PdfEncodings.convertToBytes("test1234", PdfObject.TEXT_UNICODE));
//                    field.set(pdfString, test1234);
//                    field.setAccessible(temp);
//            }
                pdfString.getBytes();
            }
            HashMap<String, String> info1 = ps.getReader().getInfo();
            System.out.println(info1);

        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();//关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != pdfReader) {
                pdfReader.close();
            }
        }

    }


    public static void main(String[] args) {
        String filePath = "G:\\1234.pdf";
        testPdf(filePath, "G:\\4321.pdf");
    }
}