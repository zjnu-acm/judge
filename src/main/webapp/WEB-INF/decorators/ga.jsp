<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8" session="false"/>

    <script>(function(i,s,o,g,r,a,m){i.GoogleAnalyticsObject=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=+new Date;a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');<jsp:scriptlet>
      out.write("ga('create','");
      final String ga = "UA-60789828-1";
      out.write(ga);
      java.security.Principal userPrincipal = request.getUserPrincipal();
      String userId = userPrincipal != null ? userPrincipal.getName() : null;
      if (userId != null) {
          out.write("','auto',{userId:'");
          out.write(userId);
          out.write("'});");
      } else {
          out.write("','auto');");
      }
    </jsp:scriptlet>ga('send','pageview');</script>
</jsp:root>
