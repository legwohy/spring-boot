package com.cobra.util.file.office;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.HtmlUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OfficeCommonUtils {

    /**
     * .doc 转为 .html
     */
    public static String docToHtml() throws Exception {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String imagePathStr = path.getAbsolutePath() + "\\static\\image\\";
        String sourceFileName = path.getAbsolutePath() + "\\static\\test.doc";
        String targetFileName = path.getAbsolutePath() + "\\static\\test2.html";
        File file = new File(imagePathStr);
        if (!file.exists()) {
            file.mkdirs();
        }
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
        org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);

        //保存图片，并返回图片的相对路径
        wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
            try (FileOutputStream out = new FileOutputStream(imagePathStr + name)) {
                out.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "image/" + name;
        });
        wordToHtmlConverter.processDocument(wordDocument);
        org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(targetFileName));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);

        return targetFileName;
    }

    /**
     * .docx 转为 .html
     */
    public static String docxToHtml() throws Exception {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String imagePath = path.getAbsolutePath() + "\\static\\image";
        String sourceFileName = path.getAbsolutePath() + "\\static\\testX.docx";
        String targetFileName = path.getAbsolutePath() + "\\static\\testX.html";
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();

            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(imagePath)));

            // html中图片的路径
            options.URIResolver(new BasicURIResolver("image"));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
        return targetFileName;
    }


    public static String readFromFilePath(String filePath) {
        File file = new File(filePath);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        byte[] bytes = new byte[1024];
        try {
            for (int n; (n = input.read(bytes)) != -1; ) {
                buffer.append(new String(bytes, 0, n, "utf8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 1、图片不能使用绝对路径
     */
    public static String htmlToDocx(String content) {
        String path = "D:/wordFile";
        Map<String, Object> param = new HashMap<String, Object>();
        if (!"".equals(path)) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            //抽出图片 替换
            content = HtmlUtils.htmlUnescape(content);

            //
            List<HashMap<String, String>> imgs = getImgStr(content);
            int count = 0;
            for (HashMap<String, String> img : imgs) {
                count++;

                //处理替换以“/>”结尾的img标签
                content = content.replace(img.get("img"), "${imgReplace" + count + "}");

                //处理替换以“>”结尾的img标签
                content = content.replace(img.get("img1"), "${imgReplace" + count + "}");
                Map<String, Object> header = new HashMap<String, Object>();
                try {
                    File filePath = new File(ResourceUtils.getURL("classpath:").getPath());
                    String imagePath = filePath.getAbsolutePath() + "\\static\\";
                    imagePath += img.get("src").replaceAll("/", "\\\\");

                    //如果没有宽高属性，默认设置为400*300
                    if (img.get("width") == null || img.get("height") == null) {
                        header.put("width", 400);
                        header.put("height", 300);
                    } else {
                        header.put("width", (int) (Double.parseDouble(img.get("width"))));
                        header.put("height", (int) (Double.parseDouble(img.get("height"))));
                    }
                    header.put("type", "jpg");
                    header.put("content", OfficeUtil.inputStream2ByteArray(new FileInputStream(imagePath), true));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                param.put("${imgReplace" + count + "}", header);
            }


            try {

                // 生成doc格式的word文档，需要手动改为docx
                byte buffer[] = content.getBytes("UTF-8");
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

                POIFSFileSystem poifs = new POIFSFileSystem();
                DirectoryEntry directory = poifs.getRoot();
                directory.createDocument("WordDocument", bais);

                // 临时纯文档
                FileOutputStream ostream = new FileOutputStream("D:\\wordFile\\temp.doc");
                poifs.writeFilesystem(ostream);
                bais.close();
                ostream.close();

                if(param.size() == 0){
                    return "D:\\wordFile\\temp.doc";
                }


                // 临时文件(手动改好的 .docx文件)
                CustomXWPFDocument doc = OfficeUtil.generateWord(param, "D:\\wordFile\\temp.docx");

                //最终生成的带图片的word文件
                FileOutputStream fopts = new FileOutputStream("D:\\wordFile\\final.docx");
                doc.write(fopts);
                fopts.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "D:/wordFile/final.docx";
    }


    /**
     * 获取html中的图片元素信息
     * @param htmlStr
     * @return
     */
    public static List<HashMap<String, String>> getImgStr(String htmlStr) {
        List<HashMap<String, String>> pics = new ArrayList<HashMap<String, String>>();
        Document doc = Jsoup.parse(htmlStr);
        org.jsoup.select.Elements imgs = doc.select("img");
        for (Element img : imgs) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (!"".equals(img.attr("width"))) {
                map.put("width", img.attr("width").substring(0, img.attr("width").length() - 2));
            }
            if (!"".equals(img.attr("height"))) {
                map.put("height", img.attr("height").substring(0, img.attr("height").length() - 2));
            }
            map.put("img", img.toString().substring(0, img.toString().length() - 1) + "/>");
            map.put("img1", img.toString());
            map.put("src", img.attr("src"));
            pics.add(map);
        }
        return pics;
    }

    public static void main(String[] args) throws Exception {
       // String htmlPath =  docToHtml();// String htmlXPath =  docxToHtml();


        String htmlXPath = "D:\\code_repository\\cobra\\cobra-common\\target\\classes\\static\\testX.html";
        // html-->docx
        String strHtml = readFromFilePath(htmlXPath);
        String docXPath = htmlToDocx(strHtml);

        System.out.println("file:"+docXPath);
    }

}
