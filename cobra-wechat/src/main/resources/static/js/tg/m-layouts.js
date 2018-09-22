var u = navigator.userAgent;
window.browser = {};
window.browser.iPhone = u.indexOf('iPhone') > -1; //iPhone or QQHD
window.browser.android = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android or uc
window.browser.ipad = u.indexOf('iPad') > -1;
window.browser.isclient = u.indexOf('lyWb') > -1;
window.browser.ios = u.match(/Mac OS/); //ios
window.browser.wx = u.match(/MicroMessenger/);
window.browser.qq = u.match(/QQBrowser/);
window.browser.pc = isPC();
window.browser.app = u.indexOf('xqb') > -1;
window.urlList = {};
window.urlList.homeUrl = location.origin + ((location.host.indexOf('jisuqianbao') != -1) ? '' : '/newh5/web');
getQueryString('source_tag') && window.localStorage.setItem("source_tag", getQueryString('source_tag'));
getQueryString('institution_id') && window.localStorage.setItem("source_tag", getQueryString('institution_id'));
window.source_tag = localStorage.source_tag ? localStorage.source_tag: '';
Initialization();
function Initialization() {
    fontSize();
   
}
function detection(){
    if (!window.browser.app){
        return showExDialog('请先登录APP','确定');
    }else{
        var phone = $('#phone').val();
        var code = $.trim($('#code').val());
        if(!isPhone(phone)){
            return checkPhoneMask(1);
        }
        if(!code){
            return checkCodeMask(1);
        }
        if($('.img img').attr('src') == 'img/agree.png'){
            var url = '/credit-user/verify-code';
            var data_s = {
                cur_phone:phone,
                cur_code:code
            }
            $.post(url, data_s, function(data){
                if (data.code == 0) {
                    location.href = 'register.html'
                } else {
                    console.log(data.message || '请求失败');
                }
            })
        }else{
            return showExDialog('请先同意协议','确定');
        }
    }
}
//页面访问统计
function webVisitStat(remark,sourceUrl,currentUrl) {
    remark = remark || '';
    sourceUrl = sourceUrl || getSourceUrl();
    currentUrl = currentUrl || location.href;
    var url = 'page/visit-stat';
    url = urlToRouteNewH5(url);
    var params = {
        source_url: sourceUrl,
        current_url: currentUrl,
        source_tag: window.source_tag,
        remark: remark
    };
    $.post(url, params, function(data) {
        if (data.code == 0) {
            console.log(data.message);
        } else {
            console.log(data.message || '请求失败');
        }
    });
}

//今日头条代码安装
function jrttCode(convertIdKey) {
    var convert_id_list = ['59426864829','62530673138'];
    var convertId = convert_id_list[convertIdKey] || convert_id_list[0];
    console.log("jrtt:" + convertId);
    (function(root) {
        root._tt_config = true;
        var ta = document.createElement('script'); ta.type = 'text/javascript'; ta.async = true;
        ta.src = document.location.protocol + '//' + 's3.pstatp.com/bytecom/resource/track_log/src/toutiao-track-log.js';
        ta.onerror = function () {
            var request = new XMLHttpRequest();
            var web_url = window.encodeURIComponent(window.location.href);
            var js_url  = ta.src;
            var url = '//ad.toutiao.com/link_monitor/cdn_failed?web_url=' + web_url + '&js_url=' + js_url + '&convert_id=' + convertId;
            request.open('GET', url, true);
            request.send(null);
        }
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ta, s);
    })(window);
}
//今日头条事件跟踪统计
function jrttClickStat(convertIdKey) {
    var convert_id_list = ['59426864829','62530673138'];
    var convertId = convert_id_list[convertIdKey] || convert_id_list[0];
    window._taq && _taq.push({convert_id:convertId, event_type:"form"});
}
//神马代码安装
function smCode() {
    window._fxcmd = window._fxcmd || [];
    window._fxcmd.sid = '55cfe4561030e8cfecffec162575f634';
    window._fxcmd.trackAll = false;
    (function () {
        var _pzfx=document['createElement']('script');
        _pzfx.type='text/javascript';
        _pzfx.async=true;
        _pzfx.src='//static.w3t.cn/fx/1/1/fx.js';
        var sc=document.getElementsByTagName('script')[0];
        sc.parentNode.insertBefore(_pzfx,sc);
    })();
}
//神马注册量跟踪统计
function smRegStat() {
    window._fxcmd && _fxcmd.push(['trackEvent','event','reg','注册','1']);
}
//神马下载量跟踪统计
function smDownloadStat() {
    window._fxcmd && _fxcmd.push(['trackEvent','event','download','下载','1']);
}
//下载地址
function downUrl(obj){
	debugger;
	location.href=obj.href;
}
//APP下载
function downLoad(obj, tag) {
    baiduClickStat(obj);
    smDownloadStat();
    if (window.browser.app) return;
    if (window.browser.iPhone || window.browser.ipad || window.browser.ios) {
        iosDownload();
    } else {
        androidDownload(tag);
    }
}
function iosDownload() {
    if (window.browser.wx) {
        wxDownload();
    } else {
        jumpTo("https://itunes.apple.com/app/id1214140934?mt=8");
    }
}
function androidDownload(tag) {
    tag = tag || window.source_tag;
    if (!window.browser.wx) {
        var apk_list = [21,22,31,39,97,119,'pc','wap','jinritoutiao1','jinritoutiao2','jinritoutiao3','jinritoutiao4','jinritoutiao5'];
        if (tag && inArray(tag,apk_list)) {
            jumpTo("http://p.kdqugou.com/apk/jsqb-" + tag + ".apk");
        } else {
            jumpTo("http://p.kdqugou.com/apk/jsqb-21.apk");
        }
    } else {
        wxDownload();
    }
}
function wxDownload() {
    var href = location.origin;
    if(location.href.indexOf('register/download-app.html') == -1){
        href = href + '/h5/web/register/download-app.html?source='+window.source_tag+'';
        jumpTo(href)
    }else{
        hideExDialog();
        hideExDialog('wxDownload');
        showMask('wxDownload', '#000', '60');
        var src = window.urlList.homeUrl + '/image/page/wx_download.png?v=2017032001';
        $(".wxDownload").attr("onclick", "hideExDialog('wxDownload');");
        $(".wxDownload").css({
            'background': '#000 url(' + src + ') no-repeat top center',
            'background-size': '100%'
        });
    }
}
function showSoft() {
    hideExDialog();
    hideExDialog('showSoft');
    showMask('showSoft', '#000', '100');
    var src = window.urlList.homeUrl + '/image/common/Soft.jpg?v=2017032001';
    $(".showSoft").attr("onclick", "hideExDialog('showSoft');");
    $(".showSoft").css({
        'background': '#000 url(' + src + ') no-repeat center center',
        'background-size': '100%'
    });
}
function urlToRouteH5Mobile(route, params) {
    var url = window.urlList.homeUrl + '/';
    url += route;
    if (params) url = createUrl(url, params);
    url = createUrl(url, 'clientType=wap');
    return strReplace(url, ['newh5/'], ['h5/mobile/']);
}
function urlToRouteNewH5(route, params) {
    var url = window.urlList.homeUrl + '/';
    url += route;
    if (params) url = createUrl(url, params);
    url = createUrl(url, 'clientType=wap');
    return url;
}
function urlToRouteFrontend(route, params) {
    var url = window.urlList.homeUrl + '/';
    url += route;
    if (params) url = createUrl(url, params);
    url = createUrl(url, 'clientType=wap');
    return strReplace(url, ['newh5/'], ['frontend/']);
}
function toCompletePersonalData(){
    return jumpTo(urlToRouteH5Mobile('mobile/#/my/certification'));
}
function checkPhoneMask(type){
    switch ( intval(type) ){
        case 1:
            return showExDialog('手机格式不正确','确定','取消');
            break;
        case 2:
            return showExDialog('账号已注册，是否继续下载APP','下载','downLoad(this)','取消');
            break;
        default:
            return showExDialog('手机格式不正确','确定');
    }
}
function checkLoginPwdMask(type){
    switch ( intval(type) ){
        case 1:
            return showExDialog('密码仅支持6-16位数字和字母，请重新设置','确定');
            break;
        default:
            return showExDialog('密码不能为空','确定');
    }
}
function checkPayPwdMask(type){
    switch ( intval(type) ){
        case 1:
            return showExDialog('交易密码由6位数字组成','确定');
            break;
        default:
            return showExDialog('交易密码不能为空','确定');
    }
}
function checkCodeMask(type){
    switch ( intval(type) ){
        case 1:
            return showExDialog('验证码不正确，是否继续下载','下载','downLoad(this)','取消');
            break;
        default:
            return showExDialog('请输入验证码','确定');
    }
}
function appJumpTo(skipCode){
    if (!window.browser.app){
        return showExDialog('请先登录APP','确定');
    };
    return nativeMethod.returnNativeMethod('{"skip_code":"'+skipCode+'"}');
}
function appJumpToWechat(){
    if (!window.browser.app){
        return showExDialog('请先登录APP','确定');
    };
    copyText('jisuqb888');
    return nativeMethod.returnNativeMethod('{"type":"10","url":"weixin://"}');
}
/**
 * 调用原生客户端-复制信息
 * @param text
 * @returns
 */
function copyText(text) {
    if (!window.browser.app){
        return showExDialog('请先登录APP','确定');
    };
    return nativeMethod.copyTextMethod('{"text":"' + text + '","tip":"复制成功!"}');
}


/**
 * 启动APP
 */
function startApp(skip_code,h5_url){
    skip_code = skip_code || 101; //默认APP借款首页
    h5_url = h5_url || '';
    var app_url = 'schemejsqb://';
    var params = {skip_code:skip_code};
    if(h5_url){
        params.skip_code = 108; //H5
        params.url = h5_url;
    }
    return location.href = createUrl(app_url, params);
}
/**
 * 调用原生分享
 * @param title 分享标题
 * @param body 分享内容
 * @param url 分享url
 * @param logo 分享logo
 * @param platform QQ,QZONE,WEIXIN,WEIXIN_CIRCLE,SINA,SMS_INVITE
 * @param type 0|1，0直接分享，1右上角出现分享按钮
 * @returns
 */
function nativeShare(title,body,url,logo,type,platform){
    if (!window.browser.app){
        return showExDialog('请登录APP进行分享','确定');
    };
    url = url || window.location.href;
    logo = logo || window.urlList.homeUrl + '/image/common/share_logo.png';
    if(type != 1) type = 0;
    platform = platform || '';
    return nativeMethod.shareMethod('{"share_title":"'+title+'","share_body":"'+body+'","share_url":"'+url+'","share_logo":"'+logo+'","type":"'+type+'","platform":"'+platform+'"}');
}
function toVoucherList(){
    if (!window.browser.app){
        return showExDialog('请登录APP进行查看','确定');
    };
    return appJumpTo(104);
}
function toPacketList(){
    if (!window.browser.app){
        return showExDialog('请登录APP进行查看','确定');
    };
    return appJumpTo(105);
}
function getNewCaptcha(obj){
    var url = urlToRouteFrontend('xqb-user/captcha?refresh');
    $.post(url, {}, function(data){
        if(data && data.url){
            $(obj).attr("src",data.url);
        }
    },'json');
}
function sendCaptcha(captcha,downloadPage,isCheckCaptcha,isNextPage){
    $('#captcha').val(captcha);
    getCode(downloadPage,isCheckCaptcha,isNextPage);
}
function getCode(downloadPage,isCheckCaptcha,isNextPage){
    downloadPage = downloadPage || 0;
    isCheckCaptcha = isCheckCaptcha || 0;
    isNextPage = isNextPage || 0;
    var phone = $("#phone").val();
    if(!isPhone(phone)){
        return checkPhoneMask(0);
    }
    var captcha = $.trim($('#captcha').val());
    if(!captcha && isCheckCaptcha){
        var rand_num = intval(Math.random()*1000000);
        var img_url = urlToRouteFrontend('xqb-user/captcha',{v:rand_num});
        var html = '';
        html += '<div class="captcha_temp_wraper m_center _b_radius" style="padding:.3em 0;background:#fff;width:100%;margin-bottom:.8em;border:1px solid #dcd3a0;">';
        html += '<span class="f_left"><img id="captcha_img" onclick="getNewCaptcha(this);" src="'+img_url+'"/></span>';
        html += '<input class="f_right em_1" style="background:#fff;width:50%;height:21px;line-height:21px;padding:.3em 0 .3em 1em;border-left:1px solid #c8c7c4;margin-right:.5em;" type="text" id="captcha_temp" maxlength="4" placeholder="请输入图片验证码" />';
        html += '<div class="clear"></div>';
        html += '</div>';
        return showExDialog(html,'确定',"sendCaptcha($('#captcha_temp').val(),"+downloadPage+",0,"+isNextPage+")");
    }
    var url = urlToRouteFrontend('xqb-user/h5-reg-get-code');
    $.post(url, {captcha:captcha,phone:phone,source:window.source_tag}, function(data){
        $('#captcha').val('');
        if(data && data.code == 0){
            if(isNextPage == 1){
                setCookie('getCodeCountDown', 'true', 's60');
                location.href = 'landing-page2.html?id=2&source_tag=10001&code=2&phone='+phone+'';
            }else{
                $('#password').parent().animate({opacity:1},1000,'linear',function(){});
                hideExDialog();
                getCodeCountDown('获取验证码','倒计时num秒');
            }
        }else if(data && data.code == 1000){
            return checkPhoneMask(2);
        }else if(data && data.code == 10001){
            getCode(downloadPage,data.is_check_captcha,isNextPage);
        }else{
            getNewCaptcha();
            return showExDialog(data.message || '获取验证码失败，请稍后重试','确定');
        }
    },'json');
}
(function(window, document){
    function createHttpRequest()
    {
        if(window.ActiveXObject){
            return new ActiveXObject("Microsoft.XMLHTTP");  
        }
        else if(window.XMLHttpRequest){
            return new XMLHttpRequest();  
        }  
    }
    function AliLogTracker(host,project,logstore){
        this.uri_ = '//' + project + '.' + host + '/logstores/' + logstore + '/track?APIVersion=0.6.0';
        this.params_=new Array();
        this.httpRequest_ = createHttpRequest();
    }
    AliLogTracker.prototype = {
        push: function(key,value) {
            if(!key || !value) {
                return;
            }
            this.params_.push(key);
            this.params_.push(value);
        },
        logger: function()
        {
            var url = this.uri_;
            var k = 0;
            while(this.params_.length > 0)
            {
                if(k % 2 == 0)
                {
                    url += '&' + encodeURIComponent(this.params_.shift());
                }
                else
                {
                    url += '=' + encodeURIComponent(this.params_.shift());
                }
                ++k;
            }
            try
            {
                this.httpRequest_.open("GET",url,true);
                this.httpRequest_.send(null);
            }
            catch (ex) 
            {
                if (window && window.console && typeof window.console.log === 'function') 
                {
                    console.log("Failed to log to ali log service because of this exception:\n" + ex);
                    console.log("Failed log data:", url);
                }
            }
            
        }
    };
    window.Tracker = AliLogTracker;
})(window, document);
//页面访问统计
function webTrackingVisitStat(sourceUrl,currentUrl,action) {
    sourceUrl = sourceUrl || getSourceUrl();
    currentUrl = currentUrl || location.href;
    action = action || 'visit';
    var logger = new window.Tracker('cn-shanghai.log.aliyuncs.com','jsqb','newh5-stats');
    var userAgent = navigator.userAgent.toLowerCase();
    logger.push('source_url', sourceUrl);
    logger.push('current_url', currentUrl);
    logger.push('action', action);
    logger.push('source_tag', window.source_tag);
    logger.push('user_agent', userAgent);
    logger.logger();
}
webTrackingVisitStat();
