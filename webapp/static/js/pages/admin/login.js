jQuery(function($){


	var userName = $('#user_name'),
    pwd = $('#pwd');
    
	 function cheLogSta(){
		    if ($.cookie("rmbUser")=="true"){ 
		    	$("#rmbUser").attr("checked", true); 
		    	userName.val($.cookie("user_name")); 
		    	pwd.val($.cookie("pwd")); 
		    } 
	    }
	    
	    function saveUserInfo(){ 
	    	if ($("#rmbUser").attr("checked")){ 
	    		var userNameVal = userName.val(); 
	    		var passWord = pwd.val();
	    		$.cookie("rmbUser", "true", { expires: 7 }); // 存储一个带7天期限的 cookie 
	    		$.cookie("user_name", userNameVal, { expires: 7 }); // 存储一个带7天期限的 cookie 
	    		$.cookie("pwd", passWord, { expires: 7 }); // 存储一个带7天期限的 cookie 
	    	}else{ 
	    		$.cookie("rmbUser", "false", { expires: -1 }); 
	    		$.cookie("user_name", '', { expires: -1 }); 
	    		$.cookie("pwd", '', { expires: -1 }); 
	    	} 
	    }
	    
    if($('#login-form')[0]){
        $('#login-form').validate({
            invalidHandler:function(event, validator){

            },
            submitHandler:function(){
            	saveUserInfo();
            	form.submit();
            },
            errorPlacement:function(error,element){
                var controlGroup =   element.closest(".control-group");
                var errorBox =   controlGroup.find('span.help-inline');
                controlGroup.addClass("error");
                errorBox.html(error).removeClass("hide");
            },
            success:function(element){
                var controlGroup =   element.closest(".control-group");
                controlGroup.removeClass("error");
            },
            rules:{
                user_name:{
                    required: true
                },
                password:{
                    required: true
                }
            },
            messages:{
                user_name:{
                    required:"用户名不能为空!"
                },
                password:{
                    required:"密码不能为空!"
                }
            }

        });

    }
    cheLogSta();
});