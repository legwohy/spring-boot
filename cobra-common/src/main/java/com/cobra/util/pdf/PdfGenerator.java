package com.cobra.util.pdf;

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
        String htmlStr = "<p style=\"text-align: center;\"><b>天翼电子商务有限公司上海分公司</b></p><p><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b><b>服务协议&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b></p><p><b>&nbsp;</b></p><p>&nbsp;</p><p><b>甲方：【 &nbsp;</b>中国新<b> &nbsp;</b><b>】</b></p><p><b>住所：【&nbsp; </b>中南海<b>&nbsp; </b><b>】</b></p><p><b>法定代表人/负责人：【 </b>小明<b>&nbsp;&nbsp; </b><b>】</b></p><p>联系人：【&nbsp;&nbsp; baby 】</p><p>联系地址：【&nbsp;&nbsp; 开票地址 】</p><p>邮编：【&nbsp; 100001&nbsp;】</p><p>电话：【&nbsp; 12345678901&nbsp; 】</p><p>电子邮件：【&nbsp; &nbsp;】</p><p><b>&nbsp;</b></p><p><b>乙方：【天翼电子商务有限公司上海分公司】</b></p><p><b>住所：【上海市虹口区塘沽路463号2楼】</b></p><p><b>法定代表人/负责人：【陈建立】</b></p><p><b>联系人：【&nbsp;&nbsp;&nbsp; 】</b></p><p><b>联系地址：[上海市虹口区塘沽路463号虹口科技金融大厦2楼 ]</b></p><p><b>邮编：[200085 ]</b></p><p><b>电话：【&nbsp;&nbsp;&nbsp; 】</b></p><p><b>电子邮件：【&nbsp;&nbsp;&nbsp; 】 </b></p><p>&nbsp;</p><p>&nbsp;</p><p>上述主体单方称为“一方”，合称“双方”。</p><p>甲乙双方本着自愿、平等、互利、诚实信用原则，就乙方向甲方提供产品服务相关事宜达成一致意见，现依据《中华人民共和国合同法》等法律法规之规定，在充分友好协商的基础上签订本协议，共同信守。</p><p><b>1&nbsp;&nbsp;&nbsp;</b><b>定义</b></p><p>除非在本协议中另有特别解释或说明，下列词语应具有如下涵义：</p><p>信息主体。本协议中所称信息主体是指包括法人、自然人和非法人组织在内的拥有其本身信息的民事主体。</p><p>信息主体的授权：是指对信息主体信息进行采集、使用、保存、加工、向他人提供等活动时依法所取得的信息主体的授权文本，包括纸质授权书、授权条款及与纸质授权同等效力的能够识别授权主体身份的电子授权。</p><p><b>2&nbsp;&nbsp;&nbsp;</b><b>产品服务内容</b></p><p>双方就产品服务的服务内容达成以下共识，并将依照法律法规及本协议的约定进行本协议项下的合作:</p><p>2.1 本协议约定，乙方向甲方提供以下第 [ A、B] 项服务。</p><p>A.欺诈盾服务；</p><p>B.甜橙镜鉴标准服务；</p><p>C.甜橙镜鉴定制服务；</p><p>D.行业关注名单服务；</p><p>E.其他业务（项目咨询、方案设计、软件开发、系统维护等根据项目情况补充约定）。</p><p>2.2 产品服务类型的定义</p><p>A.欺诈盾服务。根据甲方提供的字段信息，返回相关欺诈评估结果及其他相关数据。</p><p>B．甜橙镜鉴标准服务。根据甲方提供的同一信息主体的相关数据，返回相关信息的一致性验证的评价结果以及其他相关结果。</p><p>C. 甜橙镜鉴定制服务。根据甲方业务诉求构建模型深度定制，并根据甲方提供的同一信息主体的相关数据，返回相关信息的一致性验证的评价结果以及其他相关结果。</p><p>D．行业关注名单服务。根据甲方提供的同一信息主体的相关数据，返回相关信息是否为行业黑名单的评价结果以及其他相关结果。</p><p>E. 其他业务。根据具体服务需求，提供项目咨询、方案设计、软件开发、系统维护等服务内容。</p><p>具体服务内容以协议附件为准。乙方对其服务类型及对应内容享有定义和解释权。</p><p>2.3 合作期限：本协议有效期为本协议签署之日始至 [ 2020 ]年[ 10 ]月[ 30 ]日止。</p><p><b>3&nbsp;&nbsp;&nbsp;</b><b>信息获取条件</b></p><p>3.1甲方获取乙方服务必须同时满足下述前提条件：</p><p>条件一：提交有效的信息匹配字段。具体字段以甲方使用服务类型为准。</p><p>条件二：信息主体授权。甲方向乙方传输信息主体非公开信息的以及所使用的乙方服务内容涉及信息主体非公开信息的，需要按协议约定获得信息主体的有效充分授权。</p><p>3.2 对于甲方使用乙方本协议约定服务的，乙方在本合同约定下提供的每笔服务，甲方均已按协议约定获得信息主体的授权。</p><p><b>4&nbsp;&nbsp;&nbsp;</b><b>信息获取方式</b></p><p>本合同约定，乙方向甲方提供以下第 <b>[ 4.2 ]</b> 种信息获取方式。</p><p><b>4.1</b><b>网页端查询</b></p><p>甲方登陆乙方提供的网页客户端，实现查询服务功能。乙方根据被服务方的需求，分配<u>1</u>个用户名及密码。甲方只能在一台固定的电脑上登录查询。甲方需按照乙方相关要求和流程填写客户端查询申请材料、办理客户端查询申请事宜。</p><p><b>4.2</b><b>系统接口调用</b></p><p>甲方按照乙方提供的“Https接口”开发程序，通过甲方业务程序调用乙方“Https接口”提交服务请求，乙方系统按照“Https接口”定义的内容和格式自动完成服务、返回结果。甲方须按照乙方提出的接口技术要求，提供服务操作所需的软硬件环境。</p><p><b>5&nbsp;&nbsp;&nbsp;</b><b>服务费用及结算方式</b></p><p><b>5.1 </b><b>服务价格</b></p><p>本条款下价格仅适用于本协议合作期限内，详细服务及价格参见附件。</p><p><b>5.2 </b><b>服务费用</b></p><p>乙方为甲方的服务提供商，有权因其提供上述服务而向接受服务的相关方收取相应的服务费用，乙方在收到甲方预付款后开始提供服务。服务费用计算方式、支付方式及支付时间如下：</p><p>1.甲乙双方约定，甲方在合同签署起5个工作日内向乙方支付预付费[ 24.0000] 万元（大写：人民币 [ ] 万元整），乙方在收到甲方预付费后开通服务。</p><p>2.预付费用于实时支付甲方使用乙方服务的费用，当剩余预付费低于[ 24.0000] 万元时，乙方将以书面方式通知甲方，甲方应在[ 7 ]个工作日内补充[[ 24.0000]万元预付款，并将对应预付款项划入乙方账户，如甲方未按时补足预付款，导致预付款余额不足的，乙方有权停止服务，直至甲方补足预付款，因此造成甲方的损失由甲方自行承担。</p><p>3.甲方按照自然月与乙方对账，每自然月 [ 8 ] 号前，乙方将上一月服务费用账单发送至甲方核对，甲方应当自收到乙方服务账单7个工作日内进行核对，服务费用金额为甲方上一月实际发生的服务费用（即：实际产品调用条数*单价，双方按照各自平台记录的实际信息调用条数进行核对，对账误差率1%以内的，以乙方的对账数据为准。如对账误差率超过1%的，由甲乙双方拉取调用流水号核对调用明细，协商一致后以乙方发出的账单为准，甲方在收到乙方账单后须在三个工作日内确认账单并以书面方式回复，如甲方在收到乙方账单后三个工作日内未回复乙方，视为甲方确认账单金额无误），双方确认账单后，乙方在5个工作日内开具增值税发票，开票内容为 [*信息技术服务*信息服务费]。</p><p>4.本协议第2.3条约定的有效期届满,且甲乙双方结算完最后一笔款项后，乙方将甲方预付费剩余部分无息退还至甲方账户，如甲方与乙方另行续签协议，可将剩余预付款结转至新合同继续使用。</p><p><span style=\"color: red;\">&nbsp;</span></p><p><b>5.3 </b><b>甲方银行账户及开票信息</b></p><p>开户名称：[ 王天成 ]</p><p>开户银行： [ 中国农业银行|(德国地区)中国农业银行法兰克福分行 ]</p><p>银行账号：[ 622221234523435454445 ]</p><p>纳税人识别号：[ 33333333xxxxxx ]</p><p>地址：[ 开票地址 ]</p><p>电话：[ 12345678901 <b>&nbsp;]</b></p><p>开票类型：【增值税专用发票】 &nbsp;</p><p><b>5.3 </b><b>乙方银行账户及开票信息</b></p><p><span style=\"color: black;\">开户名称：【天翼电子商务有限公司上海分公司】</span></p><p><span style=\"color: black;\">开户银行：【中国建设银行上海六里支行】</span></p><p><span style=\"color: black;\">银行账号：【31001522917050025664】</span></p><p><span style=\"color: black;\">纳税人识别号：<b>[</b>913101095741074807<b>]</b></span></p><p><span style=\"color: black;\">地址：<b>[</b>上海市虹口区四川北路61号12楼1203室<b>]</b></span></p><p><span style=\"color: black;\">电话：<b>[</b>021-56298823<b>]</b></span></p><p><span style=\"color: black;\">开票类型：【增值税专用发票】 &nbsp;</span></p><p><b>6&nbsp;&nbsp;&nbsp;</b><b>服务流程</b></p><p>6.1确认服务开通：在本协议生效及甲方支付预付费后，甲方通过电子邮件通知乙方服务正式开通时间，乙方需及时开启服务，并向甲方发送服务开通的电子邮件，甲方确认服务正常并邮件回复则表明正式开通。甲方应自乙方开通服务通知投递至甲方电子邮件服务器时24小时内确认服务正常。甲方超过24小时未确认的，视为乙方正常开通服务。</p><p>6.2乙方系统维护：乙方不定期地为乙方设备进行维修维护。如果乙方的维修维护工作将会影响到其为甲方提供本协议项下的服务，乙方须在维护开始的两个工作日之前以电子邮件方式通知甲方经办人。若甲方在乙方通知的系统维护时间段有特殊服务要求，需自收到乙方通知之日起一个工作日内跟乙方进行磋商。</p><p><b>7&nbsp;&nbsp;&nbsp;</b><b>双方的权利义务</b></p><p><b>7.1 </b><b>甲方的权利义务</b></p><p>甲方通过本合同约定的方式使用乙方产品服务。</p><p>7.1.1甲方使用乙方服务时应向乙方提供正确、真实、完整的待分析关键字段，如因甲方提供的关键字段不准确、不及时等瑕疵所引起的后果由甲方承担。</p><p>7.1.2在客户端和系统接口调用的信息获取方式下，每月第一个工作日甲方可以得到乙方提供的上一计费统计周期内甲方使用乙方信息的计费或统计账单（以下简称“账单”）。甲方应当按约向乙方支付服务费用，甲方未依约支付服务费用的，每延迟一日，应按照应付未付金额千分之五的标准向乙方支付迟延履行违约金。甲方累计延迟支付服务费达到30日或上一结算月产生服务费20%的，乙方在要求甲方承担违约金的同时有权中止或终止本协议的履行。</p><p>7.1.3 在甲方收到乙方的服务结果文档后，应及时确认。如对该文档存有异议，甲方应在十个工作日内以书面形式通知乙方，并由甲乙双方约定整改内容及期限。如甲方未能在十个工作日内以书面形式对文档提出异议，视为甲方对乙方该文档完全认可。</p><p>7.1.4在客户端和系统接口调用的信息获取方式下，通过甲方用户名及密码登陆进行的一切操作，均视为甲方的行为，甲方应承担由此造成的一切后果。甲方应定期修改登录密码，并妥善保管其用户名和密码。</p><p>7.1.5甲方在使用乙方服务时，应遵守法律法规、规章及规范性文件等要求,且已取得被查询主体就甲方使用本协议所涉及产品服务的具体信息字段的有效充分授权（包括但不限于信息主体授权甲方向乙方提供个人信息、授权甲方可以向乙方采集信息、甲方委托乙方向第三方数据源提供、采集及保存个人信息等，授权条款或内容中涉及本协议约定服务的信息字段应采用符合监管要求的形式及方式采取逐项列举的方式，且明确告知信息主体使用目的、方式和范围等，该授权应当向被查询信息主体以显著方式列明等）。</p><p>甲方需配合乙方对甲方使用本服务时所获得的授权情况进行核实，乙方向甲方提出核实需求的，甲方须在三个工作日内提供用户授权协议/条款等符合监管要求及约定的使用本服务相关的文件资料，如甲方就本合作所获取信息主体授权等文件不符合约定及监管要求的情形，乙方有权暂停提供服务或终止协议，甲方应向乙方承担违约责任，如导致乙方遭受监管处罚、用户投诉、诉讼等损失的，相应监管处罚、用户安抚费用、诉讼费、律师费、差旅费等由甲方承担。上述费用乙方可从甲方在乙方处的任一笔款项中扣除。</p><p>7.1.6&nbsp;甲方承诺，对于乙方提供的产品服务只限在甲方合法业务及被查询信息主体授权范围内使用，除前述规定外，未经乙方及信息主体授权不做其他任何使用或提供泄露，但配合行政、司法机关调查及法律法规规定必须提供的除外，但应于受到行政、司法等调查配合的监管要求时起一个工作日内以书面形式通知乙方。</p><p>7.1.7甲方不得以乙方所提供的服务内容作为其行为依据或提供给他人作为他人的行为依据。</p><p>7.1.8对甲方已提交但因乙方系统故障导致无法返回结果的查询，乙方不计入计费查询量中，不收取费用。</p><p>7.1.9未经乙方书面同意，甲方不得以任何方式使用乙方的商标、品牌和企业名称，包括但不限于“天翼电子商务有限公司”、“翼支付”、“甜橙信用”、“tycredit”、“中国电信”、“甜橙金融”等标志。甲方不得向任何第三方直接或间接透露、暗示与（或曾经与）乙方的合作事项、合作关系，但因法律法规、监管机构、司法机关要求的除外。</p><p>&nbsp;</p><p><b>7.2 </b><b>乙方权利义务</b></p><p><span style=\"color: black;\">乙方通过本协议约定的方式向甲方提供本协议约定的产品服务。</span></p><p>7.2.1乙方在本合同约定下提供的每项产品服务，均视为甲方已按约定及监管要求获得了信息主体的有效充分授权。因甲方就本合作所获取信息主体授权不符合约定及监管要求的情形，乙方有权暂停提供服务或终止协议。</p><p>7.2.2乙方在客户端、系统接口调用的信息获取方式下，负责甲方、乙方各项核查比对日志的管理，不向任何其他方提供甲方的日志信息，对于甲方提供的信息只限在本协议下为甲方提供所需服务使用，不向第三方提供。<span style=\"color: black;\">但配合行政、司法机关调查及根据政府要求、命令或法律法规规定必须提供的除外。</span></p><p>7.2.3因乙方系统升级改造等原因不能提供服务，或者乙方可提供服务的数据范围发生变化时，乙方应及时以网站公告、传真或邮件的方式通知甲方。</p><p>7.2.4 乙方必须遵守国家法律、行政法规、部门性规章、规范性文件以及与国家相关产业政策。</p><p>7.2.5 乙方有权利对提供的服务收取相应的服务费用，甲方未按约定支付服务费用的，乙方有权停止服务并按7.1.2约定收取支付迟延履行违约金。</p><p>&nbsp;</p><p><b>7.3</b><b>陈述和保证</b></p><p>甲、乙双方均承诺和保证：</p><p>其是依据中华人民共和国公司法及相关法律成立并存续的，具备法律、法规、规章及规范性文件所要求的签署和履行本协议之全部资质。本协议履行过程中涉及协议外第三方主体的权利的，双方均已获得第三方合法有效授权。双方在缔约过程及协议履行中向对方提供资料均真实、完整、有效。</p><p>双方未经对方事先书面授权同意，一方不得在任何地方（包括但不限于广告、宣传）以任何方式使用对方的商标、品牌和企业名称。一方不得向任何第三方直接、间接披露、暗示与对方的合作事项，或向任何第三方直接、间接透露或暗示与对方（可能或曾）与对方有合作关系，但因法律法规、监管机构、司法机关要求除外。</p><p><b>8&nbsp;&nbsp;&nbsp;</b><b>保密条款</b></p><p><span style=\"color: black;\">8.1 </span><span style=\"color: black;\">甲乙双方及其雇员应对合作过程中对方提供的所有文件（包括但不限于纸制文件及其它介质文件）以及甲乙双方在本协议履行过程中所获知的双方各项技术、客户信息、数据资料以及商业秘密（以下统称“保密信息”）承担严格的保密义务。除非一方事先书面许可，另一方及其雇员、外部顾问等在任何时间不得向任何协议以外的第三方透露保密信息的任何部分。除履行本合同确有必要外，双方不得对保密信息进行拷贝或抄写。</span></p><p><span style=\"color: black;\">8.2 </span><span style=\"color: black;\">非经一方事先书面许可，另一方不得将保密信息用于本协议外的其他目的。</span></p><p><span style=\"color: black;\">8.3 </span><span style=\"color: black;\">如保密信息提供方有要求或本协议终止后，除本协议另有约定之外，接受保密信息一方应根据对方的要求返还或销毁所取得的资料及其他复印件（或其他任何形式的复制品）、删除计算机中所存储的保密信息以及利用保密信息制作的文件。</span></p><p><span style=\"color: black;\">8.4 </span><span style=\"color: black;\">任何一方因违反保密义务而给对方造成经济损失的，应承担全额赔偿责任。任何一方的雇员、外部顾问违反本协议项下保密义务，视为该方违约。</span></p><p><span style=\"color: black;\">8.5 </span><span style=\"color: black;\">本条所述保密义务对双方持续有效，不因本合同的终止而终止。</span></p><p><b>9&nbsp;&nbsp;&nbsp;</b><b>违约责任</b></p><p>9.1 甲乙双方应当严格遵守本协议之约定。任何一方违反约定而导致合作无法继续进行，则另一方有权立即终止本协议项下合作，并有权要求违约方承担违约责任。</p><p>9.2 在协议履行期间所发生的一切差错、事故和案件，造成损失的，应由违约方承担赔偿责任。对于不可归因于甲乙两方的责任，双方应依照公平原则分担；因第三方原因造成损失的，甲乙双方应互相协助向该第三方追偿。</p><p>9.3 若遇国家行政管理部门、司法机关政策调整对本协议的履行造成严重影响的，调整方应及时通知对方，由双方协商议定，如协商不能达成一致的，任何一方有权终止本协议。</p><p>9.4 由于国家法律法规和监管政策的变化导致乙方无法继续为甲方提供服务时，不视为乙方违约。</p><p>9.5 由于不可抗力而不能履行合同时，不构成违约，由双方共同协商议定，协商不成的，按《中华人民共和国合同法》有关规定执行。</p><p><b>10&nbsp; </b><b>反贿赂廉洁条款</b></p><p>10.1、双方保证并承诺：在本服务提供过程中，双方严格遵守反贿赂、反行贿及反不正当竞争法的相关规定，不得从事违反相关法律法规的行为。</p><p>10.2、任一方工作人员不得有下列行为：</p><p>（1）向对方或对方经办人或其他相关人员索要、收受协议约定外的任何利益，包括但不限于现金、购物卡、实物、有价证券、干股、红利、旅游或其他非物质性利益等；</p><p>（2）在合作方报销与本项目无关的费用及向合作方提出个人利益要求或参加任何可能对履约有影响的宴请或娱乐活动；</p><p>（3）私自与合作方就合作项目进行私下商谈或达成默契，透露有关招投标内控信息及企业商业秘密，或采用任何方式在项目中为家属和亲友谋利益；</p><p>（4）以洽谈工作等为名，邀请合作方工作人员或其亲友外出旅游和进行高消费娱乐活动。</p><p>10.3、任一方如发现其他方工作人员有违反廉洁条款约定行为，应向该方上级领导者或监察部门举报，被举报的一方不得以任何借口进行打击报复。</p><p>受理举报电话/邮箱：</p><p>翼支付：<u>jubao@bestpay.com.cn</u><span style=\"\">&nbsp;</span>； <span style=\"\">&nbsp;</span></p><p>合作方：<u><span style=\"\">&nbsp;</span></u><u> </u><u><span style=\"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></u><u>&nbsp;</u><u><span style=\"\">&nbsp;</span></u><u> </u><u><span style=\"\">&nbsp;&nbsp;</span></u></p><p><b>11&nbsp; </b><b>适用法律和争议解决</b></p><p>11.1 本协议的签订、效力、执行和解释均遵循中华人民共和国法律。</p><p>11.2 因本协议引起的或与本协议有关的任何争议，双方应友好协商解决。如协商不成，则任何一方均有权将该争议提交至上海市虹口区人民法院诉讼解决。</p><p>11.3 诉讼进行过程中，双方将继续履行本协议未涉诉讼的其他部分。</p><p><b>12&nbsp; </b><b>免责条款</b></p><p>&nbsp;在合同履行过程中，由于不可抗力致使双方无法按约履行合同约定的相关义务，根据不可抗力对合同履行造成的影响，双方全部或部分免除责任。</p><p>本协议所称不可抗力是指双方不能预见、不能避免并不能克服的客观情况（包括政府禁止性行为）。</p><p><b>13&nbsp; </b><b>合同的终止</b></p><p>除因不可抗力致使合同无法再继续履行、本协议约定及法律、行政法规规定的情形外，任何一方不得擅自解除或终止本合同。</p><p><b>14&nbsp; </b><b>其他规定</b></p><p>14.1本协议履行过程中，由双方指定项目经办人确认本协议履行相关事宜。凡采用以下联系方式发出的通知经过约定期限视为已送达对方：</p><p>（1）采用电子邮件、传真方式通知的，成功发出即视为送达；</p><p>（2）除本协议约定外采用纸质信函方式通知的，自发出之日起满三日视为送达。</p><p>一方变更本协议通知联系人、地址、电话、邮件或传真等信息的，应提前七个工作日将拟变更信息通知另一方，否则另一方有权继续按原联系人和联系方式通知对方，变更方应对此造成的一切后果承担责任。</p><p>14.2本协议项下的全部或部分权利、义务均不得向任何第三方转让。</p><p>14.3非经合同签订双方书面确认，任何一方不得擅自变更或修改本合同，国家法律法规另有规定的除外。</p><p>14.4本协议经双方加盖各自公章或合同专用章之日起生效。</p><p>14.5 本协议一方没有行使、或延迟行使本协议项下的权利，不构成对这种权利或补救措施的放弃，也不构成对任何其他权利的放弃，但是本协议中其他条款对此有特别约定的情况除外。</p><p>14.6 若有未尽事宜，双方应另行协商确定，并签署书面补充协议或其他法律文件。补充合同作为本合同不可分割的一部分，与本合同具有同等法律效力。补充合同与本合同条款如有冲突，以补充合同为准。</p><p>14.7 本合同附件作为本合同不可分割的组成部分，与本合同正文具有同等法律效力。</p><p>14.8 本协议及附件正本一式肆份，双方各持贰份，各份正本具有同等法律效力。</p><p>&nbsp;</p><p style=\"text-align: center;\">（以下无正文）</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p style=\"text-align: center;\">[签署页无正文]</p><p>&nbsp;</p><p>&nbsp;</p><p>甲&nbsp; 方：【&nbsp;&nbsp; 中国新&nbsp;&nbsp;&nbsp; 】 &nbsp;&nbsp;</p><p>法定代表人/签字代表：</p><p>日&nbsp; 期：&nbsp; &nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>乙&nbsp; 方：天翼电子商务有限公司上海分公司</p><p>法定代表人/签字代表：</p><p>日&nbsp; 期：&nbsp; &nbsp;</p><p>&nbsp;</p>\n" ;

        OutputStream os = new FileOutputStream(new File("D:\\temp.pdf"));
        htmlToPdf(htmlStr,os);
    }
}
