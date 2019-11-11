<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>





</head>
<body>
<div class="value">0.125</div>
<div class="range">
  <input type="range" min="0" max="1000" step="1" value="125">
  <p class="rang_width"></p>
</div>
<script>
var elem = document.querySelector('input[type="range"]');
 
var rangeValue = function(){
  var newValue = elem.value/1000;
  var target = document.querySelector('.value');
  target.innerHTML = newValue;
};
 
elem.addEventListener("input", rangeValue);
</script>
</body>
</html>