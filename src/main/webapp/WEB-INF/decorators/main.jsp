<?xml version="1.0" encoding="UTF-8"?>
<!--
    Document   : main
    Created on : Apr 9, 2016, 3:11:13 AM
    Author     : zhanhb
-->
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:decorator="http://www.opensymphony.com/sitemesh/decorator"
          version="2.0">

    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8" session="false"/>

    <jsp:scriptlet>
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    </jsp:scriptlet>

    <jsp:expression>"&lt;!DOCTYPE html&gt;\r\n"</jsp:expression>
    <html>
        <head>
            <meta charset="utf-8"/>
            <meta name="renderer" content="webkit"/>
            <meta http-equiv="Cache-Control" content="no-siteapp"/>
            <meta http-equiv="X-UA-Compatible" content="chrome=1,IE=Edge"/>
            <title><decorator:title default="Welcome to JudgeOnline"/></title>
            <!--<link href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" rel="stylesheet"/>-->

            <link href="${pageContext.request.contextPath}/css/site.css?_=${applicationScope.startUpDate}" rel="stylesheet"/>
            <link href="${pageContext.request.contextPath}/sample.png" rel="shortcut icon"/>
            <script src="${pageContext.request.contextPath}/webjars/jquery/1.12.4/jquery.min.js"><!----></script>
            <script src="${pageContext.request.contextPath}/js/site.js?_=${applicationScope.startUpDate}"><!----></script>
            <decorator:head/>
        </head>
        <body id="page-home">
            <div id="page">
                <div id="nav">
                    <jsp:scriptlet>
                        if (request.getAttribute("contestId") == null) {
                    </jsp:scriptlet>
                    <div id="outer-header">
                        <div id="branding" align="center">
                            <a href="${pageContext.request.contextPath}/">
                                <img id="logo" border="0"
                                     src="${pageContext.request.contextPath}/images${pageContext.request.contextPath}logo.jpg"
                                     width="100%" height="100"/></a>
                        </div>
                        <!-- end branding -->
                    </div>
                    <!-- end header -->
                    <jsp:include page="/navigation"/>
                    <jsp:scriptlet>
                    } else {
                    </jsp:scriptlet>
                    <table border="0" width="100%" class="table-back">
                        <tr>
                            <td style="width:16.7%"><a href="${pageContext.request.contextPath}/">Home Page</a></td>
                            <td style="width:16.7%"><a href="${pageContext.request.contextPath}/bbs" target="_blank">Web Board</a></td>
                            <td style="width:16.7%"><a href="${pageContext.request.contextPath}/showcontest?contest_id=${contestId}">Problems</a></td>
                            <td style="width:16.7%"><a href="${pageContext.request.contextPath}/conteststanding?contest_id=${contestId}">Standing</a></td>
                            <td style="width:16.7%"><a href="${pageContext.request.contextPath}/status?contest_id=${contestId}">Status</a></td>
                            <jsp:scriptlet>/*<![CDATA[*/
                                {
                                    java.security.Principal userPrincipal = request.getUserPrincipal();
                                    String userId = userPrincipal != null ? userPrincipal.getName() : null;
                                    if (userId != null) {
                                        out.print("<td><b>" + userId + "</b> <a href='" + request.getContextPath() + "/logout'>Logout</a></td>");
                                    } else {
                                        out.print("<td><a href=\"" + request.getContextPath() + "/login?url=" + java.net.URLEncoder.encode((String) request.getAttribute("backUrl"), "UTF-8") + "\">Login</a></td>");
                                    }
                                }
                                /*]]>*/</jsp:scriptlet>
                            </tr>
                        </table>
                    <jsp:scriptlet>
                        }
                    </jsp:scriptlet>
                    <jsp:include page="/notice"/>
                </div>
                <!-- end nav -->
                <div id="content" class="clearfix">
                    <decorator:body/>
                </div>
                <!-- end content -->
                <div id="footer" class="clearfix">
                    <p>
                        <img alt="home" height="29" src="${pageContext.request.contextPath}/images/home.jpg" width="40" border="0"/>
                        <font size="3"><a href="${pageContext.request.contextPath}/">Home Page</a></font>
                        <img alt="goback" height="29" src="${pageContext.request.contextPath}/images/goback.jpg" width="40" border="0" style="margin-left: 40px;"/>
                        <font size="3">
                        <a href="javascript:history.go(-1)">Go Back</a>
                        <img alt="top" height="29" width="40" border="0" src="${pageContext.request.contextPath}/images/top.jpg" style="margin-left: 40px;"/>
                        <a href="#top">To top</a>
                        </font>
                    </p>
                    <hr/>
                    <p align="center">
                        <font size="3">All Copyright Reserved 2006-<jsp:expression>java.time.Year.now()</jsp:expression> ZJNU ACM<br/>Any problem, Please Contact <jsp:scriptlet>
                            /*<![CDATA[*/
                            String email = (String) application.getAttribute("admin.mail");
                            if (email != null && !email.isEmpty()) {
                                /*]]>*/
                        </jsp:scriptlet>
                        <a href="mailto:${applicationScope['admin.mail']}">Administrator</a>
                        <jsp:scriptlet>} else {</jsp:scriptlet>
                        <jsp:text>Administrator</jsp:text>
                        <jsp:scriptlet>}</jsp:scriptlet>
                        </font>
                    </p>
                </div>
                <!-- end footer -->
            </div>
            <!-- end page -->
            <jsp:directive.include file="ga.jsp"/>
        </body>
    </html>
</jsp:root>
