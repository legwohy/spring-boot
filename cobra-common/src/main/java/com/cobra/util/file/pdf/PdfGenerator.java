package com.cobra.util.file.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PdfGenerator {
    private static Logger logger = LoggerFactory.getLogger(PdfGenerator.class);

    public final static String CHARSET_NAME = "UTF-8";


    private Document document;

    /**
     * 设置字体
     */
    private Font font;

    public PdfGenerator() {
        document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);

        // 添加附件
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            logger.error("字体不存在,errorMsg={}",e);
        }
        font = new Font(bfChinese, 10, Font.NORMAL);

    }

    public void htmlToPdf(String htmlStr, OutputStream out) throws IOException, DocumentException {

        PdfWriter writer = PdfWriter.getInstance(document, out);

        // 设置页眉
        PDFBuilder builder = new PDFBuilder();
        writer.setPageEvent(builder);
        document.open();

        // html内容解析
        HtmlPipelineContext htmlContext = new HtmlPipelineContext
                (
                    new CssAppliersImpl(new XMLWorkerFontProvider() {
                        @Override
                        public Font getFont(String fontName, String encoding, float size, final int style)
                        {
                            if (fontName == null)
                            {
                                // 加载系统配置文件
                                font = getSystemConfigFont(size, style);
                            }
                            
                            return font;
                        }
                    })
                );

        // 图片解析
        htmlContext.setImageProvider(new AbstractImageProvider() {

            String rootPath = PdfGenerator.class.getResource("/").getPath();

            @Override
            public String getImageRootPath() {
                return rootPath;
            }

            @Override
            public Image retrieve(String src) {
                if (StringUtils.isEmpty(src)) {
                    return null;
                }
                try {
                    Image image = Image.getInstance(new File(rootPath, src).toURI().toString());
                    // 图片显示位置
                    image.setAbsolutePosition(400, 400);
                    store(src, image);
                    return image;
                } catch (Exception e) {
                    logger.error("pdf文件解析图片错误,errorMsg={}",e);
                }
                return super.retrieve(src);
            }
        });
        htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());

        // css解析
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        cssResolver.setFileRetrieve(new FileRetrieve() {
            @Override
            public void processFromStream(InputStream in,
                                          ReadingProcessor processor) throws IOException {
                try (
                        InputStreamReader reader = new InputStreamReader(in, Charset.forName(CHARSET_NAME))) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Exception e) {
                    logger.error("css解析错误,errorMsg={}",e);
                }
            }

            // 解析href
            @Override
            public void processFromHref(String href, ReadingProcessor processor) throws IOException {
                InputStream is = PdfGenerator.class.getResourceAsStream("/" + href);
                try (InputStreamReader reader = new InputStreamReader(is, CHARSET_NAME)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Exception e) {
                    logger.error("解析href错误,errorMsg={}",e);
                }
            }
        });

        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = null;
        worker = new XMLWorker(pipeline, true);
        XMLParser parser = new XMLParser(true, worker, Charset.forName(CHARSET_NAME));
        try (InputStream inputStream = new ByteArrayInputStream(htmlStr.getBytes())) {
            parser.parse(inputStream, Charset.forName(CHARSET_NAME));
        }


    }

    public void closeDocument() {
        document.close();
    }


    /**
     * 添加文档属性
     */
    public  void addAttach() {
        // 文档附加信息
        document.addTitle("合同.pdf");
        document.addAuthor("lei sir");
    }

    /**
     * 添加新章节 另起一页
     *
     * @param title
     * @throws DocumentException
     */
    public void addNextPage(String title) throws DocumentException {
        // 新章节从编号1开始
        Chapter chapter = new Chapter(new Paragraph(title), 1);
        // 不显示上面的编号1
        chapter.setNumberDepth(0);
        document.add(chapter);
    }


    public void addOthers() throws DocumentException {
        // 添加属性
        this.addAttach();

        document.add(new Paragraph("附件 见下页 ",font));

        // 另起一页
        this.addNextPage("合同");

        // 居中
        Paragraph paragraph = new Paragraph("附 订单信息 1-1",font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

    }


    /**
     * 添加列表
     */
    public void addTable(LinkedHashMap<String,String> titleMap, List<Map<String,Object>> contentMapList) throws DocumentException {
        // 3X7
        int size = titleMap.size();
        PdfPTable table = new PdfPTable(size);
        // 宽度100%填充
        table.setWidthPercentage(100);
        // 前间距
        table.setSpacingBefore(10f);
        // 后间距
        table.setSpacingAfter(10f);
        ArrayList<PdfPRow> rowList = table.getRows();



        PdfPCell[] titleCells = new PdfPCell[size];

        int i = 0;
        for(Map.Entry<String,String> en: titleMap.entrySet()){
            titleCells[i] = new PdfPCell(new Paragraph(en.getValue(), font));
            i++;
        }


        PdfPRow titleRow = new PdfPRow(titleCells);

        rowList.add(titleRow);


        for (int j = 0; j < contentMapList.size(); j++) {
            PdfPCell[] contentCells = new PdfPCell[size];
            Map<String,Object> contentMap = contentMapList.get(j);
            int k = 0;

            // 顺序
            for(Map.Entry<String,String> en: titleMap.entrySet()){
                if(contentMap.containsKey(en.getKey())){
                    contentCells[k] = new PdfPCell(new Paragraph(contentMap.get(en.getKey()).toString(), font));
                    k++;
                }
            }


            PdfPRow contentRow = new PdfPRow(contentCells);
            rowList.add(contentRow);
        }

        document.add(table);

    }

    private Font getSystemConfigFont(float size, int style){
        Font configFont = null;
        String fontFile = this.getClass().getResource("/").getPath() + "fonts/simsun.ttc";
        BaseFont bf=null;
        try {
            bf = BaseFont.createFont(fontFile+",1",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            configFont = new Font(bf, size, style);
        } catch (Exception e) {
            log.error("html 字体转换失败fontFile:{},font:{}",fontFile,font);
        }

        return configFont;
    }

}
