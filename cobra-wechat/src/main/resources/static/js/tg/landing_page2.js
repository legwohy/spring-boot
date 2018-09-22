var invite_code = getQueryString('invite_code');
$('.agree img').click(function(){
    if($(this).attr('src') == 'img/agree.png'){
        $(this).attr('src','img/agree_1.png')
    }else{
        $(this).attr('src','img/agree.png')
    }
})
var tt = $('.landing_msg').height()/$('li').height();
var h = $('.landing_msg').height()/tt;
var a;
a = 1;
var lunbo = function(){
    a = a + 1;
    $('ul').animate({marginTop:-(h*a+1)},1000,'linear',function(){})
}
var lunbo_1 = function(){
    setTimeout(function(){lunbo()},500)
}
var append = function(){
    if(-h*a < -$('ul').height()/2){
        $('ul').append($('ul').html())
    }
}
var inter = function(){setInterval(function(){lunbo_1();append();},1500)}
inter()

var lunbo1 = function(){
    $('.img_money .img_1 img').animate({paddingTop:"25%"},800,'linear',function(){lunbo1_3();})
}
var lunbo1_3 = function(){
    $('.img_money .img_1 img').animate({paddingTop:"50%",opacity:0},700,'linear',function(){lunbo1_1();lunbo1();})
}
var lunbo1_1 = function(){
    $('.img_money .img_1 img').css("paddingTop","5%")
    $('.img_money .img_1 img').css("opacity","1")
}
lunbo1()
var lunbo2 = function(){
    $('.img_money .img_2 img').animate({paddingTop:"25%"},800,'linear',function(){lunbo2_1();})
}
var lunbo2_1 = function(){
    $('.img_money .img_2 img').animate({paddingTop:"50%",opacity:0},700,'linear',function(){lunbo2_2();lunbo2();})
}
var lunbo2_2 = function(){
    $('.img_money .img_2 img').css("paddingTop","5%")
    $('.img_money .img_2 img').css("opacity","1")
}
setTimeout(function(){lunbo2()},500)
var lunbo3 = function(){
    $('.img_money .img_3 img').animate({paddingTop:"25%"},800,'linear',function(){lunbo3_1();})
}
var lunbo3_1 = function(){
    $('.img_money .img_3 img').animate({paddingTop:"50%",opacity:0},700,'linear',function(){lunbo3_2();lunbo3();})
}
var lunbo3_2 = function(){
    $('.img_money .img_3 img').css("paddingTop","5%")
    $('.img_money .img_3 img').css("opacity","1")
}
setTimeout(function(){lunbo3()},1000)

function register(obj){
    var phone = $("#phone").val();
    var password = $.trim($('#password').val());
    var code = $.trim($('#code').val());
    if(!isPhone(phone)){
        return checkPhoneMask(1);
    }
    if(!isLoginPwd(password)){
        return checkLoginPwdMask(1);
    }
    if(!code){
        return checkCodeMask(1);
    }
    if($('.agree img').attr('src') == 'img/agree.png'){
        var url = '/frontend/web/xqb-user/register?clientType=wap';
        var params = {
            phone:phone,
            password:password,
            code:code,
            source:window.source_tag
        };
        $.post(url, params, function(data){
            if(data && data.code == 0){
                downLoad(obj);
            }else{
                return showExDialog(data.message || '注册失败，请稍后重试','确定');
            }
        },'json');
    }else{
        return showExDialog('请仔细阅读《平台服务协议》并同意','确定');
    }
}