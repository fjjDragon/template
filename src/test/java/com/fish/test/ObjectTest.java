//package com.fish.test;
//
//
//import com.fish.model.bean.GameInfo;
//import org.openjdk.jol.info.ClassLayout;
//
///**
// * @descript: 测试查看对象padding
// * @author: fjjDragon
// * @create: 2021-06-04 16:40
// **/
//public class ObjectTest {
//
//    public static void test(Object obj) {
//        ClassLayout layout = ClassLayout.parseInstance(obj);
//        System.out.println(layout.toPrintable());
//    }
//
//    public static void main(String[] args) {
//        int[] ints = {1};
//        test(ints);
////        UserData userData = new UserData();
////        userData.setUid(1234);
////        userData.setOpenid("AAAAA");
////        test(userData);
//        GameInfo gameInfo = new GameInfo();
//        test(gameInfo);
//    }
//
////    //创建一个pdf读取对象
////		PdfReader reader = new PdfReader(templatePdfPath);
////		//创建一个输出流
////		ByteArrayOutputStream bos = new ByteArrayOutputStream();
////		//创建pdf模板，参数reader  bos
////		PdfStamper ps = new PdfStamper(reader, bos);
////		//封装数据，取出模板中的所有字段数据，读取文本域
////		AcroFields s = ps.getAcroFields();
////		s.setField("id", juanBook.getId());
////		s.setField("name", juanBook.getName());
////		s.setField("shoolname", juanBook.getShoolName());
////		s.setField("rmb", juanBook.getRmb());
////		s.setField("data", juanBook.getData());
////		ps.setFormFlattening(true);//必须要调用这个，否则文档不会生成的
////		ps.close();//关闭PdfStamper
////		FileOutputStream fos = new FileOutputStream(file);//创建文件输出流
////		fos.write(bos.toByteArray());//写入数据
////		fos.close();//关闭输出流
//
//}