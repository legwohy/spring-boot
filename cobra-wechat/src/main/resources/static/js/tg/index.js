/*
* @Author: Administrator
* @Date:   2018-04-02 16:55:28
* @Last Modified by:   Administrator
* @Last Modified time: 2018-04-02 17:04:23
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