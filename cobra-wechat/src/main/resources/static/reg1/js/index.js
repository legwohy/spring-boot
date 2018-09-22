/*
* @Author: Administrator
* @Date:   2018-04-02 16:55:28
* @Last Modified by:   Administrator
* @Last Modified time: 2018-08-31 11:57:52
*/
// JavaScript Document
        var ohtml = document.documentElement;
        getSize();

        window.onresize = function(){
            getSize();
        }
        function getSize(){

            var screenWidth = ohtml.clientWidth;
            if(screenWidth <= 320){
                ohtml.style.fontSize = '17px';
            }else if(screenWidth >= 750){
                ohtml.style.fontSize = '40px';
            }else{
                ohtml.style.fontSize = screenWidth/(750/40)+'px';
            }
            
        }

jQuery(document).ready(function($) {
    // 清空输入值
    $('.input-c input').focus(function() {
      $(this).parents('.item').addClass('focus').siblings('.item').removeClass('focus');
    })
    $('.icon-close').click(function() {
      console.log($(this).prev().val(''));
      $(this).parents('.item').removeClass('focus');
    })

//   // 倒计时
//      var wait=60;
//      var flag = true;
//      var interval = null;
//   
//    function time() {
//        interval = setInterval(function() {
//            if (wait == 0) {
//                clearInterval(interval);
//                $("#btn").text("点击获取");
//                wait = 60;
//                flag = true;
//            } else {
//                $("#btn").text(wait + "s");
//                wait--;
//            }
//        }, 1000);
//
//    }
          
      // 清空输入值
     function clearval(){
      $('#userPhone').val('');
      $('#password').val('');
      $('#captcha').val('');
      $('#smsCode').val('');
     }
    // loading
    function loading(isShow){
     if (isShow) {
       var divElement = $("<div id='loadingWrap' style='position: fixed;left: 0;top: 0;right: 0;bottom: 0;z-index: 1000;background-color:rgba(0,0,0,.5);'><img src='images/loading.gif' style='position: absolute;width:1rem;width:2rem; top: 40%; left: 50%;margin-left:-0.5rem' /></div>");
        $("body").append(divElement);
      }else{
        $('#loadingWrap').remove();
      }
     
    }

//      // 获取短信验证码
//      $('#btn').click(function(event) {
//        var phone_val = $('#userPhone').val();
//        var psd_val = $('#password').val();
//        var img_code_val = $('#captcha').val();
//        var re = /^1\d{10}$/;
//        var reg = new RegExp(/^[a-zA-Z0-9]+$/);
//        if (!flag) {
//            return;
//        };
//        if(!phone_val){
//          showMsg('手机号不能为空',1000)
//          return
//        }else if(!re.test(phone_val)){
//          showMsg('请输入正确的手机号',1000)
//          return 
//        }
//        if(!img_code_val){
//          showMsg('图形验证码不能为空',1000)
//          return
//        }
//        if(!psd_val){
//          showMsg('密码不能为空',1000)
//          return
//        }else if(!reg.test(psd_val) || psd_val > 20){
//          showMsg('密码为20位内字母或数字',1000);
//          return 
//        }
//          flag = false;
//        $.ajax({
//          url: '/sms/getSmsCode?userPhone='+phone_val,
//          type: 'GET',
//          contentType: "application/json; charset=utf-8",
//          dataType: 'json',
//          xhrFields: {
//            withCredentials: true
//          },
//          crossDomain: true,
//          // data: {userPhone: phone_val},
//          success:function(res) {
//            if (res.code === "0") {
//                showMsg('短信发送成功！',1000);
//                time();
//            }else{
//              showMsg(res.message);
//            }
//          },
//          error: function(res) {
//            if (res.msg) {
//              showMsg(res.msg);
//            };
//            showMsg("请求失败，请稍后再试",1000);
//          }
//        })
//
//      });
//
//       // 提交信息
//      $('#submit').click(function(event) {
//        var phone_val = $('#userPhone').val();
//        var sms_code_val = $('#sms_code').val();
//        var re = /^1\d{10}$/;
//        var reg = new RegExp(/^[a-zA-Z0-9]+$/);
//        if(!phone_val){
//          showMsg('手机号不能为空',1000)
//          return
//        }else if(!re.test(phone_val)){
//          showMsg('请输入正确的手机号',1000)
//          return 
//        }
//        if(!img_code_val){
//          showMsg('图形验证码不能为空',1000)
//          return
//        }
//        if(!psd_val){
//          showMsg('密码不能为空',1000)
//          return
//        }else if(!reg.test(psd_val) || psd_val > 20){
//          showMsg('密码为20位内字母或数字',1000);
//          return 
//        }
//
//         $.ajax({
//          url: '/sms/getSmsCode?userPhone='+phone_val,
//          type: 'GET',
//          contentType: "application/json; charset=utf-8",
//          dataType: 'json',
//          xhrFields: {
//            withCredentials: true
//          },
//          crossDomain: true,
//          // data: {userPhone: phone_val},
//          success:function(res) {
//            if (res.code === "0") {
//                showMsg('注册成功！',1000);
//            }else{
//              showMsg(res.message);
//            }
//          },
//          error: function(res) {
//            if (res.msg) {
//              showMsg(res.msg);
//            };
//            showMsg("请求失败，请稍后再试",1000);
//          }
//        })
//      });
})
