jQuery(function($){

    $('.mod-member-show tr').on('mouseenter',function(){
        $(this).addClass('hover');
    }).on('mouseleave',function(){
        $(this).removeClass('hover');
    });

});