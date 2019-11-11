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

<div id="main" style="width: 2000px;height: 900px"></div>
 <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
       var data = [[10.2, 16, 3, 1], [16, 18, 15, 'B'], [18, 26, 12, 'C'], [26, 32, 22, 'D'], [32, 56, 7, 'E'], [56, 62, 17, 'F']];
var colorList = ['#4f81bd', '#c0504d', '#9bbb59', '#604a7b', '#948a54', '#e46c0b'];

data = echarts.util.map(data, function (item, index) {
    return {
        value: item,
        itemStyle: {
            normal: {
                color: colorList[index%6]
            }
        }
    };
});

function renderItem(params, api) {
    var yValue = api.value(2);
    var start = api.coord([api.value(0), yValue]);
    var size = api.size([api.value(1) - api.value(0), yValue]);
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

option = {
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
        dimensions: ['from', 'to', 'profit'],
        encode: {
            x: [0, 1],
            y: 2,
            tooltip: [0, 1, 2],
            itemName: 3
        },
        data: data
    }]
};

        // 使用刚指定的配置项和数据显示图表。
         myChart.setOption(option);
        
        $.get(
				"getInformation.action",
				{},
				function(data){
					myChart.setOption({
						series:[
							{
								data:data.xcount
							},
						]
					});
				},
				"json"
			)
    </script>




</body>
</html>