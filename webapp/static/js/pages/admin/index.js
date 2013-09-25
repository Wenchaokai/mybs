jQuery(function($){

    var container = $('#member-filter'),
        delModale =  $("#modal-del"),
        pwdInitModel = $("#modal-pwd-init");

    if(container[0]){
        initDelMedal();
        bindDelEvent();
        bindPasswordInitEvent();
        $('body').on('hidden', '.modal', function () {$(this).removeData('modal');});
    }

    function bindDelEvent(){
        container.on('click','a.action-del',function(e){
            e.preventDefault();
           /*var mid = $(this).data("mid");
            var tr = $(this).closest("tr");
            delModale.modal('show').on('shown',function(){
                delModale.data('mid',mid);
                delModale.data('tr',tr);
            });*/
            var b = window.confirm("确定要删除?");
            var _this = $(this);
            if(b){
                //这里要做个信息的提交
                _this.closest('tr').css('backgroundColor',"#da4f49");
                _this.closest('tr').fadeOut('slow', function() {
                    _this.closest('tr').remove();
                });
                return true;
            }else{
                return false;
            }
        });

        delModale.bind("action.del",function(e,data){
            data.el.css('backgroundColor',"#da4f49");
            data.el.fadeOut('slow', function() {
                data.el.remove();
            });
        });
    }
    function bindPasswordInitEvent(){
        container.on('click','a.action-pwd-init',function(e){
            e.preventDefault();
            /*var mid = $(this).data("mid");
            var tr = $(this).closest("tr");
            pwdInitModel.modal('show').on('shown',function(){
                pwdInitModel.data('mid',mid);
                pwdInitModel.data('tr',tr);
            });*/
            var _this = this;
            var b = window.confirm("确定要初始化密码?");
            if(b){
                //这里要做个信息的提交
               $(_this).html('初始化完成').css("backgroundColor","#5eb95e").css('color',"#fff");
                return true;
            }else{
                return false;
            }
        });

        pwdInitModel.bind("action.pwdinit",function(e,data){
             //data.el.css("backgroundColor","#5eb95e");
            data.el.find('a.action-pwd-init').first().html('初始化完成').css("backgroundColor","#5eb95e").css('color',"#fff");
        });
    }

    function initDelMedal(){

        delModale.on('click','a.btn-cancle',function(){
            delModale.modal('hide');
        });

        delModale.on('click','a.btn-comfirm',function(e){
            var mid =  delModale.data('mid');
            if(mid){
                $.ajax({
                    url:'./ajax-admin-member.html',
                    type:"POST",
                    data:{
                        "mid":mid,
                        "type":"del"
                    },
                    dataType:"json"
                }).done(function(res){
                     if(res&&res.success){
                         delModale.modal('hide');
                         setTimeout(function(){
                             delModale.trigger('action.del',{"mid":mid,el:delModale.data('tr')});
                         },500)
                     }
                });
            }
        });
        pwdInitModel.on('click','a.btn-comfirm',function(e){
            var mid =  pwdInitModel.data('mid');
            if(mid){
                $.ajax({
                    url:'./ajax-admin-member.html',
                    type:"POST",
                    data:{
                        "mid":mid,
                        "type":"password_init"
                    },
                    dataType:"json"
                }).done(function(res){
                        if(res&&res.success){
                            pwdInitModel.modal('hide');
                            setTimeout(function(){
                                pwdInitModel.trigger('action.pwdinit',{"mid":mid,el:pwdInitModel.data('tr')});
                            },500)
                        }
                    });
            }
        });


    }
});