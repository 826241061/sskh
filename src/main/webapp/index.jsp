<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"
	type="text/javascript"></script>
<script>
	function myClear() {
		document.getElementsByClassName("scope1")[0].value = 0;
		document.getElementsByClassName("scope1")[1].value = 0;
		myChart.setOption({
			dataZoom : [ {
				start : 0,
				end : 100
			} ]
		});
	}
	function myLocate() {
		if (parseFloat(document.getElementsByClassName("scope1")[0].value) >= parseFloat(document
				.getElementsByClassName("scope1")[1].value))
			document.getElementsByClassName("scope1")[1].value = parseFloat(document
					.getElementsByClassName("scope1")[0].value) + 1;
		myChart
				.setOption({
					dataZoom : [ {
						startValue : document.getElementsByClassName("scope1")[0].value,
						endValue : document.getElementsByClassName("scope1")[1].value
					} ]
				});
	}
</script>
</head>
<body>
	<script src="js/echarts.js"></script>

	<div class="value">0.250</div>
	<div class="range">
		<input type="range" min="0" max="500" step="1" value="250">
		<p class="rang_width"></p>
	</div>


	<div id="main" style="width: 1800px; height: 850px"></div>

	<div style="width: 500px; height: 30px">
		<input class="scope1" type="number" value="0"> <input
			class="scope1" type="number" value="0">
		<button style="width: 53px; height: 20px" onclick="myLocate()">Locate</button>
		<button style="width: 47px; height: 20px" onclick="myClear()">Clear</button>
	</div>



	<script type="text/javascript">
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'));
		// 指定图表的配置项和数据

		function renderItem(params, api) {
			var yValue = api.value(3);
			var start = api.coord([ api.value(1), yValue ]);
			var xValue = api.value(2) - api.value(1);
			if (xValue == 0) {
				start = api.coord([ api.value(1) - 0.0025, yValue ]);
				xValue = 0.005;
			}
			var size = api.size([ xValue, yValue ]);
			var style = api.style();

			return {
				type : 'rect',
				shape : {
					x : start[0],
					y : start[1],
					width : size[0],
					height : size[1]
				},
				style : style
			};
		};

		option = {
			color : [ '#c23531', '#4f81bd99' ],
			title : {
				text : '散点分布',
			},
			grid : {
				left : '3%',
				right : '7%',
				bottom : '10%',
				containLabel : true
			},
			tooltip : {

			},
			toolbox : {
				feature : {
					dataZoom : {},
					brush : {
						type : [ 'rect', 'polygon', 'clear' ]
					}
				}
			},
			brush : {},
			legend : {
				data : [ 'Point', 'Cluster' ],
				left : 'center'
			},
			xAxis : [ {
				type : 'value',
				scale : true,
				axisLabel : {
					formatter : '{value} '
				},
				splitLine : {
					show : false
				}
			} ],
			yAxis : [ {
				type : 'value',
				scale : true,
				axisLabel : {
					formatter : '{value} 个'
				},
				splitLine : {
					show : false
				}
			} ],
			series : [
					{
						name : 'Point',
						type : 'scatter',
						dimensions : [ 'x', 'y', 'y' ],
						encode : {
							x : 0,
							y : 1,
							tooltip : [ 0, 2 ]
						},
						data : []

					},
					{
						name : 'Cluster',
						type : 'custom',
						renderItem : renderItem,
						label : {
							normal : {
								show : true,
								position : 'top'
							}
						},
						dimensions : [ 'mid', 'min', 'max', 'count', 'max-min',
								'min_distance', 'max_distance',
								'standard deviation' ],
						encode : {
							x : [ 0, 2 ],
							y : 3,
							tooltip : [ 1, 2, 3, 4, 5, 6, 7 ],
							itemName : 0
						},
						data : []
					}

			],
			dataZoom : [
					{
						type : 'slider',
						height : 8,
						bottom : 20,
						borderColor : 'transparent',
						backgroundColor : '#e2e2e2',
						handleIcon : 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
						handleSize : 20,
						handleStyle : {
							shadowBlur : 6,
							shadowOffsetX : 1,
							shadowOffsetY : 2,
							shadowColor : '#aaa'
						}
					}, {
						type : 'inside'
					}, {
						type : 'slider',
						yAxisIndex : 0,
						start : 0,
						end : 100
					} ],
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);

		$.get("getPosition.action", {}, function(data) {
			var info = new Array();
			var size = data.xcount.length;
			var mark = 0;
			for (var i = 0; i < size; i++) {

				var count = data.xcount[i][1];
				var num = data.xcount[i][0];
				for (var j = 1; j <= count; j++) {
					var current = new Array(3);
					current[0] = num;
					current[1] = j;
					current[2] = count;
					info[mark++] = current;
				}

			}
			myChart.setOption({
				series : [ {
					data : info,
					symbolSize : 3
				}, ]
			});
		}, "json");

		myChart.showLoading();
		$.get("getClass.action", {
			divide : 250,
			isDistinct : 0
		}, function(data) {
			myChart.hideLoading();
			var info = new Array();
			var size = data.mid.length;
			for (var i = 0; i < size; i++) {
				var current = new Array(8);
				current[0] = data.mid[i];
				current[1] = data.min[i];
				current[2] = data.max[i];
				current[3] = data.count[i];
				current[4] = data.difference_MaxMin[i];
				current[5] = data.min_distance[i];
				current[6] = data.max_distance[i];
				current[7] = data.standard_Deviation[i];
				info[i] = current;
			}
			myChart.setOption({
				title : {
					subtext : 'δ的值为：' + data.numOfK
				},
				series : [ {}, {
					data : info
				} ]
			});
		}, "json")
	</script>

	<script>
		var elem = document.querySelector('input[type="range"]');

		var rangeValue = function() {
			//var newValue = 1/elem.value;
			var target = document.querySelector('.value');
			target.innerHTML = '0.'
					+ (Array(3).join('0') + elem.value).slice(-3);

			myChart.showLoading();
			$.get("getClass.action", {
				divide : elem.value,
				isDistinct : 0
			}, function(data) {
				myChart.hideLoading();

				var info = new Array();
				var size = data.mid.length;
				for (var i = 0; i < size; i++) {
					var current = new Array(8);
					current[0] = data.mid[i];
					current[1] = data.min[i];
					current[2] = data.max[i];
					current[3] = data.count[i];
					current[4] = data.difference_MaxMin[i];
					current[5] = data.min_distance[i];
					current[6] = data.max_distance[i];
					current[7] = data.standard_Deviation[i];
					info[i] = current;
				}

				myChart.setOption({
					title : {
						subtext : 'δ的值为：' + data.numOfK
					},
					series : [ {}, {
						data : info
					} ]
				});
			}, "json")

		};

		elem.addEventListener("input", rangeValue);
	</script>


</body>
</html>