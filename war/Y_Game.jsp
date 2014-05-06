<!doctype html>
<html>
<head>
<meta name="gwt:property" content="locale=<%= request.getLocale() %>">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=1.0, minimum-scale=1.0, maximum-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Y-Game</title>
<script type="text/javascript" language="javascript"
  src="org.Y_Game/org.Y_Game.nocache.js"></script>
<script>
function scaleBody() {
	  var myGameWidth = 830;
	  var myGameHeight = 730;
	  var scaleX = window.innerWidth / myGameWidth;
	  var scaleY = window.innerHeight / myGameHeight;
	  var scale = Math.min(scaleX, scaleY);
	  var transformString = "scale(" + scale + "," + scale + ")";
	  document.body.style.transform = transformString;
	  document.body.style['-o-transform'] = transformString;
	  document.body.style['-webkit-transform'] = transformString;
	  document.body.style['-moz-transform'] = transformString;
	  document.body.style['-ms-transform'] = transformString;
	  var transformOriginString = "0 0 0";
	  document.body.style['transform-origin'] = transformOriginString;
	  document.body.style['-o-transform-origin'] = transformOriginString;
	  document.body.style['-webkit-transform-origin'] = transformOriginString;
	  document.body.style['-moz-transform-origin'] = transformOriginString;
	  document.body.style['-ms-transform-origin'] = transformOriginString;
	}
	window.onresize = scaleBody;
	window.onorientationchange = scaleBody;
	window.onload = scaleBody;
	document.addEventListener("orientationchange", scaleBody);
</script>
<style>
.imgContainer {
  overflow: hidden;
  width: 73px;
  height: 97px;
}

.imgShortContainer {
  overflow: hidden;
  width: 15px;
  height: 97px;
}

.color-icon {
  background-image: none !important;
  background-color: #ff0000;
}

.color-icon2 {
  background-image: none !important;
  background-color: #00ff00;
}

.color-try {
  background-image: none !important;
  background-color: #aaaaaa;
}

.center {
  text-align:center;
  margin-left:auto; 
  margin-right:auto;
}

.margin {
	margin-top: 20px;
	margin-botton: 20px;
}
</style>
</head>
<body>
  <div id="mainDiv" width="600" height="600"></div>
</body>
</html>