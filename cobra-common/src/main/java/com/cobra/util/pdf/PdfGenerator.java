package com.cobra.util.pdf;

import com.cobra.util.StringCommonUtils;
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
import com.itextpdf.tool.xml.pipeline.html.*;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class PdfGenerator {

	/**
	 * Output a pdf to the specified outputstream
	 * 
	 * @param htmlStr
	 *            the htmlstr
	 * @param out
	 *            the specified outputstream
	 * @throws Exception
	 */

	public static void htmlToPdf(String htmlStr, OutputStream out) throws IOException, DocumentException {
		final String charsetName = "UTF-8";
		
		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        // 设置页眉
        PDFBuilder builder = new PDFBuilder();
        writer.setPageEvent(builder);

        document.open();
        
        // html内容解析
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(
                new CssAppliersImpl(new XMLWorkerFontProvider() {
                    @Override
                    public Font getFont(String fontname, String encoding, float size, final int style) {
                        if (fontname == null) {
                        	// 操作系统需要有该字体, 没有则需要安装; 当然也可以将字体放到项目中， 再从项目中读取
                            fontname = "SimSun";        
                        }
                        return super.getFont(fontname, encoding, size, style);
                    }
                })) {
            @Override
            public HtmlPipelineContext clone() throws CloneNotSupportedException {
                HtmlPipelineContext context = super.clone();
                try {
                    ImageProvider imageProvider = this.getImageProvider();
                    context.setImageProvider(imageProvider);
                } catch (NoImageProviderException e) {
                }
                return context;
            }
        };
        
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
                    if (image != null) {
                        store(src, image);
                        return image;
                    }
                } catch (Throwable e) {
                	e.printStackTrace();
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
                        InputStreamReader reader = new InputStreamReader(in, charsetName)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                }
            }

            // 解析href
            @Override
            public void processFromHref(String href, ReadingProcessor processor) throws IOException {
            	InputStream is = PdfGenerator.class.getResourceAsStream("/" + href);
                try (InputStreamReader reader = new InputStreamReader(is,charsetName)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                	e.printStackTrace();
                }
            }
        });

        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = null;
        worker = new XMLWorker(pipeline, true);
        XMLParser parser = new XMLParser(true, worker, Charset.forName(charsetName));
        try (InputStream inputStream = new ByteArrayInputStream(htmlStr.getBytes())) {
            parser.parse(inputStream, Charset.forName(charsetName));
        }

        // 添加属性
        addAttach(document);

        document.add(new Paragraph("附件 见下页 ",font));

        // 另起一页
        addNextPage(document,"订单信息");

        // 居中
        Paragraph paragraph = new Paragraph("附 订单信息 1-1",font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);


        // 添加表单
        addTable(document);



        document.close();
	}

	/** 设置字体*/
	private static Font font;
	static {
        // 添加附件
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            // TODO 解决中文不显示问题
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        font = new Font(bfChinese, 10,Font.NORMAL);
    }



	/** 添加文档属性*/
	private static void addAttach(Document document){
        // 文档附加信息
        document.addTitle("协议.pdf");
        document.addAuthor("lei sir");
    }

    /**
     * 添加新章节 另起一页
     * @param document
     * @param title
     * @throws DocumentException
     */
    private static void addNextPage(Document document,String title) throws DocumentException{
        // 新章节从编号1开始
        Chapter chapter = new Chapter(new Paragraph(title),1);
        // 不显示上面的编号1
        chapter.setNumberDepth(0);
        document.add(chapter);
    }


    /**
     * 添加列表
     */
	private static void  addTable(Document document) throws DocumentException{
	    // 3X7
        PdfPTable table = new PdfPTable(7);
        // 宽度100%填充
        table.setWidthPercentage(100);
        // 前间距
        table.setSpacingBefore(10f);
        // 后间距
        table.setSpacingAfter(10f);
        ArrayList<PdfPRow> rowList = table.getRows();


        PdfPCell[] titleCells = new PdfPCell[7];
        titleCells[0] = new PdfPCell(new Paragraph("订单号",font));
        titleCells[1] = new PdfPCell(new Paragraph("商品信息",font));
        titleCells[2] = new PdfPCell(new Paragraph("规格",font));
        titleCells[3] = new PdfPCell(new Paragraph("购买数量",font));
        titleCells[4] = new PdfPCell(new Paragraph("购买时长",font));
        titleCells[5] = new PdfPCell(new Paragraph("创建时间",font));
        titleCells[6] = new PdfPCell(new Paragraph("金额",font));
        PdfPRow titleRow = new PdfPRow(titleCells);

        rowList.add(titleRow);

        for (int i = 0;i<3;i++){
            PdfPCell[] contentCells = new PdfPCell[7];
            contentCells[0] = new PdfPCell(new Paragraph("10001",font));
            contentCells[1] = new PdfPCell(new Paragraph("治疗库",font));
            contentCells[2] = new PdfPCell(new Paragraph("S 粉色",font));
            contentCells[3] = new PdfPCell(new Paragraph("100",font));
            contentCells[4] = new PdfPCell(new Paragraph("10年",font));
            contentCells[5] = new PdfPCell(new Paragraph("2046-10-10 02:34:34",font));
            contentCells[6] = new PdfPCell(new Paragraph("￥38.00",font));

            PdfPRow contentRow = new PdfPRow(contentCells);
            rowList.add(contentRow);
        }

        document.add(table);

    }

    public static void main(String[] args) throws Exception{
	    Long start = System.currentTimeMillis();
	    File file = new File(PathUtils.getRootClassPath("testPdf.html"));
	    FileInputStream fis = new FileInputStream(file);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];
        int n;
        while ((n=fis.read(bytes)) != -1){
            baos.write(bytes,0,n);
        }
        baos.flush();
	    baos.close();

        String htmlStr = baos.toString() ;

        OutputStream os = new FileOutputStream(new File("D:\\temp.pdf"));
        htmlToPdf(htmlStr,os);

        Long end = System.currentTimeMillis();

        System.out.println("耗时:"+ StringCommonUtils.longToStringPoint(end-start,3));
    }
}
