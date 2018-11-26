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
      $(this).prev().val('');
      $(this).parents('.item').removeClass('focus');
    });
    // 消息提示
      function showMsg(info,time){
        var e_time = 2000;
        if (time) {
           e_time = time
        }
        var divElement = $("<div id='msg' style='position: fixed;left: 0; top: 0;right: 0;bottom: 0;z-index: 1000;text-align: center;z-index: 100;background-color: rgba(0, 0, 0, .1);'><div style='width: 100%; display: inline-block;text-align: center;position: absolute;top: 30%;left:0;'><div style='display: inline-block;padding: 0.4rem 0.5rem;font-size: 0.75rem;line-height: 1rem;color: #fff;border-radius: 0.2rem;text-align: center;background-color: rgba(0, 0, 0, .5);'>"+info+"</div></div></div>");
        $("body").append(divElement);
          setTimeout(function(){
            $("#msg").remove();
          }, e_time);
        }
    /*
        $('#submit').click(function () {
            if (!$("#phone").val()) {
                showMsg('请输入手机号')
            }
        })
      */
})
