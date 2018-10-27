<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/18 0018
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Cache-Control" content="no-transform" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=yes" />
    <!-- 设置页面的编码方式-->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>欢迎您的关注</title>
</head>
<body>
<h1>hello 酷狗!!!!!!欢迎进入狗狗的世界！</h1>
<button id="queryproduct" name="queryproduct" onclick="queryQroduct()">查看我的物流消息</button>
<button id="updateaddress" name="updateaddress" onclick="updateAddress()">修改我的收货地址和时间</button>

<form action="${pageContext.request.contextPath}/addperson" id="personform" name="personform">
    账号:<input type="text" name="name">
    性别:<input type="text" name="sex">
    <input type="submit" value="你点击我，试试看！">
</form>
</body>
<script type="text/javascript">
    //查询我的物流信息
    function queryQroduct() {
        window.location.href = "${pageContext.request.contextPath}/queryproduct";
    }
    //修改我的地址和收货时间
    function updateAddress() {
        window.location.href = "${pageContext.request.contextPath}/updateaddress";
    }



</script>
</html>
