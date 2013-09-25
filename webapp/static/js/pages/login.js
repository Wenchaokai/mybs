jQuery(function($){

    var loginForm =    $('#login-form'),
        userName = $('#userName'),
        pwd = $('#pwd'),
        errorBox = $('#label-error-box');

    function cheLogSta(){
	    if ($.cookie("rmbUser")=="true"){
	    	$("#rmbUser").attr("checked", true); 
	    	userName.val($.cookie("userName")); 
	    	pwd.val($.cookie("passWord")); 
	    } 
	}
	
	function saveUserInfo(){
		if ($("#rmbUser").attr("checked")){
			var userNameVal = userName.val(); 
			var passWord = pwd.val(); 
			$.cookie("rmbUser", "true", { expires: 7 }); // 存储一个带7天期限的 cookie 
			$.cookie("userName", userNameVal, { expires: 7 }); // 存储一个带7天期限的 cookie 
			$.cookie("passWord", passWord, { expires: 7 }); // 存储一个带7天期限的 cookie 
		}else{ 
			$.cookie("rmbUser", "false", { expires: -1 }); 
			$.cookie("userName", '', { expires: -1 }); 
			$.cookie("passWord", '', { expires: -1 }); 
		} 
	}

    function validUserName(){
        if($.trim(userName.val())!=""){
        	errorBox.css('display','none');
        	return true;
        }
        errorBox.find('span').first().html('请输入用户名');
        errorBox.removeClass('hide');
        userName.trigger('focus');
        errorBox.css('display','block');
        return false;
    }
    function validPwd(){
        if($.trim(pwd.val())!=""){
        	errorBox.css('display','none');
        	return true;
        }
        errorBox.find('span').first().html('请输入密码');
        errorBox.removeClass('hide');
        pwd.trigger('focus');
        errorBox.css('display','block');
        return false;
    }

    loginForm.on('submit',function(){
	    var flag = validUserName()&&validPwd();
	    if (flag)
	    	saveUserInfo();
	    return flag;
	});
    cheLogSta();
    
    var num = 1;
    var isActive = false;

    var cNodes = $("#J-slide-number a");

    function resetTabs(){
        var node = $("#content  .slide.current");
        node.removeClass('current');

        node.animate({
            opacity: 0
        }, 1000, function() {
            node.hide();
            node.find('.bg').removeClass('bg-1');
        });
        $("#J-slide-number a").removeClass("slide-number-active"); // Activate first tab
    }

    function initTab(){
        var node = $("#content  .slide.current");
        cNodes.on("click",function(e) {
            isActive = true;
            e.preventDefault();
            if ($(this).hasClass("slide-number-active")){
                return;
            }
            else{
                resetTabs();
                $(this).addClass("slide-number-active"); // Activate this
                var targetTab =  $("#content .slide").eq($(this).data('seed')-1);
                num = $(this).data('seed');
                targetTab.addClass('current');
                targetTab.find('.bg').addClass('bg-1');
                targetTab.fadeIn().animate({
                    opacity: 1
                }, 1000, function() {
                    isActive = false;
                });
            }
        });
    }
    initTab();

    setInterval(function(){
        if(!isActive){
            var n = num;
            if(n==3){
                n = 0;
            }
            cNodes.eq(n).trigger('click');
        }
    },4000)
});
