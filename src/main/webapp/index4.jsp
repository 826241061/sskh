<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js" type="text/javascript"></script>
</head>
<body>
<script src="js/echarts.js"></script>




<div id="main" style="width: 1800px;height: 400px"></div>
<br>
<div class="value">1/8</div>
<div class="range">
  <input type="range" min="1" max="1000" step="1" value="125">
  <p class="rang_width"></p>
</div>

<div id="main1" style="width: 1800px;height: 400px"></div>
 <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        var myChartclass = echarts.init(document.getElementById('main1'));
        // 指定图表的配置项和数据



option = {
    title : {
        text: '散点分布',
    },
    grid: {
        left: '3%',
        right: '7%',
        bottom: '10%',
        containLabel: true
    },
    tooltip : {
        trigger: 'axis',
        showDelay : 0,
        formatter : function (params) {
        	var index = params.length-1;
        	if(params[index].value!=null)
	            if (params[index].value.length > 1) {
	                return params[index].seriesName + ' :<br/> x='
	                + params[index].value[0] + ' y='
	                + params[index].value[1] + ' ';
	            }
	            else {
	                return params[index].seriesName + ' :<br/>'
	                + params[index].name + ' : '
	                + params[index].value + 'kg ';
	            }
        },
        axisPointer:{
            show: true,
            type : 'cross',
            lineStyle: {
                type : 'dashed',
                width : 1
            },
            snap:true,
        }
    },
    toolbox: {
        feature: {
            dataZoom: {},
            brush: {
                type: ['rect', 'polygon', 'clear']
            }
        }
    },
    brush: {
    },
    legend: {
        data: ['散点'],
        left: 'center'
    },
    xAxis : [
        {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value} '
            },
            splitLine: {
                show: false
            }
        }
    ],
    yAxis : [
        {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value} 个'
            },
            splitLine: {
                show: false
            }
        }
    ],
    series : [
        {
            name:'散点',
            type:'scatter',
            dimensions: ['x', 'y'],
            encode: {
                x: 0,
                y: 1,
                tooltip: [0, 1]
            },
            data: []


        }
       
    ],
    dataZoom: [{
        type: 'slider',
        height: 8,
        bottom: 20,
        borderColor: 'transparent',
        backgroundColor: '#e2e2e2',
        handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
        handleSize: 20,
        handleStyle: {
            shadowBlur: 6,
            shadowOffsetX: 1,
            shadowOffsetY: 2,
            shadowColor: '#aaa'
        }
    }, {
        type: 'inside'
    },{
        type: 'slider',
        yAxisIndex: 0,
        start: 0,
        end: 100
    }],
};

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

        $.get(
				"getPosition.action",
				{},
				function(data){
					
					myChart.setOption({
						series:[
							{
								data:data.xcount,
								symbolSize: 3
							},
						]
					});
				},
				"json"
			);
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
	
	function renderItem(params, api) {
	    var yValue = api.value(3);
	    var start = api.coord([api.value(1), yValue]);
	    var xValue = api.value(2) - api.value(1);
	    if(xValue==0){
	    	start = api.coord([api.value(1)-0.01,yValue]);
	        xValue=0.02;
	    }
	    var size = api.size([xValue, yValue]);
   		 var style = api.style();

    return {
        type: 'rect',
        shape: {
            x: start[0],
            y: start[1],
            width: size[0],
            height: size[1]
        },
        style: style
    };
}
        
        option1 = {
        	    title: {
        	        text: 'Profit',
        	        left: 'center'
        	    },
        	    tooltip: {
        	    },
        	    xAxis: {
        	        scale: true
        	    },
        	    yAxis: {
        	    },
        	    series: [{
                    type: 'custom',
                    renderItem: renderItem,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    dimensions: ['mid','min', 'max', 'count', 'max-min', 'min_distance', 'max_distance', 'standard deviation'],
                    encode: {
                        x: [0, 2],
                        y: 3,
                        tooltip: [1, 2,3,4,5,6,7],
                        itemName: 0
                    },
                    data: []
                }],
                tooltip : {
                     trigger: 'axis',
                    showDelay : 0,
//                     formatter : function (params) {
//                         if(params.seriesName=='散点'){
//                             if (params.value.length > 1) {
//                                 return params.seriesName + ' :<br/> x='
//                                 + params.value[0] + ' y='
//                                 + params.value[1] + ' ';
//                             }
//                             else {
//                                 return params.seriesName + ' :<br/>'
//                                 + params.name + ' : '
//                                 + params.value + 'kg ';
//                             }
//                         }
//                     },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        },
                        snap:true,
                    }
                },
                
                dataZoom: [{
                    type: 'slider',
                    height: 8,
                    bottom: 20,
                    borderColor: 'transparent',
                    backgroundColor: '#e2e2e2',
                    handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
                    handleSize: 20,
                    handleStyle: {
                        shadowBlur: 6,
                        shadowOffsetX: 1,
                        shadowOffsetY: 2,
                        shadowColor: '#aaa'
                    }
                }, {
                    type: 'inside'
                },{
                    type: 'slider',
                    yAxisIndex: 0,
                    start: 0,
                    end: 100
                }],
        	};
        
        myChartclass.setOption(option1);
        myChartclass.showLoading();
$.get(
		"getClass.action",
		{divide:0.125},
		function(data){
			myChartclass.hideLoading();
			myChartclass.setOption({
				title:{
					subtext:'δ的值为：'+data.division
				},
				series:[{
						data:data.xcount
					}
				]
			});
		},
		"json"
	)
			
			
			
    </script>

<script>
var elem = document.querySelector('input[type="range"]');
 
var rangeValue = function(){
  var newValue = 1/elem.value;
  var target = document.querySelector('.value');
  target.innerHTML = '1\/'+elem.value;
  
  
  myChartclass.showLoading();
  $.get(
  		"getClass.action",
  		{divide:newValue},
  		function(data){
  			myChartclass.hideLoading();
  			myChartclass.setOption({
  				title:{
  					subtext:'δ的值为：'+data.division
  				},
  				series:[{
  						data:data.xcount
  					}
  				]
  			});
  		},
  		"json"
  	)
  
  
};

elem.addEventListener("input", rangeValue);
</script>


</body>
</html>