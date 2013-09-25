jQuery(function($){
	
	if($('#member-form')[0]){

        $('#member-form').validate({
            invalidHandler:function(event, validator){

            },
            submitHandler:function(form){
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
                new_passowrd:{
                    required: true
                },
            },
            messages:{
            	userName:{
                    required:"密码不能为空!"
                },
                userCount:{
                    required:"密码不能为空!"
                },
            }
        });

    }


    if($('#password-form')[0]){

        $('#password-form').validate({
            invalidHandler:function(event, validator){

            },
            submitHandler:function(form){
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
                new_passowrd:{
                    required: true
                },
                confirm_new_password:{
                    required: true,
                    equalTo: "#new_passowrd"
                }
            },
            messages:{
                old_passowrd:{
                    required:"密码不能为空!"
                },
                new_passowrd:{
                    required:"密码不能为空!"
                },
                confirm_new_password:{
                    required:"密码不能为空!",
                    equalTo:"输入的新密码不一致，请重新输入"
                },
            }
        });

    }

});