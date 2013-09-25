jQuery(function($){

    $('.mod-add-member .field-value').on('mouseenter',function(){
        $(this).addClass('hover');
    }).on('mouseleave',function(){
        $(this).removeClass('hover');
    });
    
    var userRole = $("[name='userRole']"),
	roleGroup = $("[name='role-group']");

userRole.change(function(){
	var selectedValue = $("input[name='userRole']:checked").val();
	if (selectedValue==1){
		roleGroup.addClass('hide');
	}else{
		roleGroup.removeClass('hide');
	}
});

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
                required:"用户名不能为空!"
            },
            userCount:{
                required:"邮箱不能为空!"
            },
        }
    });

}


if($('#bill-form')[0]){

    $('#bill-form').validate({
        invalidHandler:function(event, validator){

        },
        submitHandler:function(form){
        	form.submit();
        },
        errorPlacement:function(error,element){
            var controlGroup =   element.closest(".control-group");
            var errorBox =   controlGroup.find('span.help-inline');
            errorBox.html(error).removeClass("hide");
        },
        success:function(element){
            var controlGroup =   element.closest(".control-group");
            controlGroup.removeClass("error");
        },
        rules:{
        	billAccount:{
                required: true
            },
        },
        messages:{
        	billAccount:{
                required:"金额不能为空!"
            }
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