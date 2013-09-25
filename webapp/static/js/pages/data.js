jQuery(function($){

    //看数据首页-数据详情

    if($("#mod-detail-table-container")[0]){


        $('button',"#mod-detail").click(function(){
            getDataDetailDataByAjax($(this).data('url'));

        });

        jQuery('select',"#mod-detail").change(function(){

            getDataDetailDataByAjax($(this).val());

        });

        getDataDetailDataByAjax($('#mod-detail-table-container').data('url'));

    }

    function getDataDetailDataByAjax(url){

        $('#mod-detail-table-container').html('').addClass("loading");

        $.ajax({
            url:url,
            type:'GET',
            dataType:'json',
            success:function(){

            }
        }).done(function(res){

            if(res&&res.success){
                renderDataDetailTable(res,$('#mod-detail-table-container'));
            }
        });

    }


    function renderDataDetailTable(res,el){
        var html = [],item;

        html.push('<table class="ui-table">');

        html.push('<thead><tr><th class="t1">&nbsp;</th>');

        for(var i= 0,len = res.label.length;i<len;i++){

            html.push('<th class="t'+(i+2)+'">'+res.label[i]+'</th>');
        }

        html.push('</tr></thead>');

        html.push('<tbody>');

        for(var j = 0,len = res.data.length;j<len;j++){

            item = res.data[j];

            html.push('<tr>');

            for(var k = 0,len2 = item.length;k<len2;k++){

                if($.type(item[k])=='object'){
                    if(item[k].state=='up'){

                        html.push('<td><span class="">'+item[k].num+'</span></td>');

                    }else{

                        html.push('<td><span class="decline">'+item[k].num+'</span></td>');
                    }
                }else{
                    if(k==0){
                        html.push('<td>'+item[k]+'</td>');
                    }else{

                        html.push('<td><em>'+item[k]+'</em></td>');

                    }
                }
            }

            html.push('</tr>');

        }

        html.push('</tbody>');
        html.push('</table>');

        el.html(html.join(''));
        $('#mod-detail-table-container').removeClass("loading");

    }

    //趋势图

    if($('#trend-chart')[0]){
        $.ajax({
            url:$('#trend-chart').data('url'),
            type:'GET',
            dataType:'json',
            success:function(){

            }
        }).done(function(res){

            if(res&&res.success){
                showTrendCharts(res,$('#trend-chart'));
            }
        });

    }


    if($('#chart')[0]){
        $.ajax({
            url:$('#chart').data('url'),
            type:'GET',
            dataType:'json',
            success:function(){

            }
        }).done(function(res){

            if(res&&res.success){
                showCharts(res);
            }
        });
    }
    //趋势图
    function showTrendCharts(data,el){
        el.highcharts({
            exporting:{
                enabled:false
            },
            credits: {
                enabled: false
            },
            chart: {
                type: data.type
            },
            title: {
                text: data.title,
                x: -20 //center
            },
            subtitle: {
                text: '',
                x: -20
            },
            xAxis: {
                categories: data.categories,
                tickmarkPlacement: 'on',
                title: {
                    enabled: false
                }
            },

            yAxis: {
                min: 0,
                title: {
                    text: data.yAxis
                }
            },
            colors: [
                '#0088cc',
                '#f5002f',
                '#8bbc21',
                '#910000',
                '#1aadce',
                '#492970',
                '#f28f43',
                '#77a1e5',
                '#c42525',
                '#a6c96a'
            ],
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.f}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            } ,
            plotOptions: {
                area: {
                    stacking: 'normal',
                        lineColor: '#666666',
                        lineWidth: 1,
                        marker: {
                        lineWidth: 1,
                            lineColor: '#666666'
                    }
                }
            },
            series: data.series
        });
    }

    function showCharts(data){
        $('#chart').highcharts({
            exporting:{
                enabled:false
            },
            credits: {
                enabled: false
            },
            chart: {
                type: data.type
            },
            title: {
                text: data.title,
                x: -20 //center
            },
            subtitle: {
                text: '',
                x: -20
            },
            xAxis: {
                categories: data.categories
            },

            yAxis: {
                min: 0,
                title: {
                    text: data.yAxis
                }
            },
            colors: [
                '#0088cc',
                '#f5002f',
                '#8bbc21',
                '#910000',
                '#1aadce',
                '#492970',
                '#f28f43',
                '#77a1e5',
                '#c42525',
                '#a6c96a'
            ],
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.f}%</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: data.series
        });
    }
});