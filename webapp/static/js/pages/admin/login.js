jQuery(function($){


    if($('#login-form')[0]){
        $('#login-form').validate({
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

});