var onit = true;
var num = 0;
var Stop;
function moveup(iteam, top, txt, rec) {
    var temp = eval(iteam);
    var tempat = eval(top);
    var temptxt = eval(txt);
    var temprec = eval(rec);
    var at = parseInt(temp.style.top);
    temprec.style.display = "";
    if (num > 27) {
        temptxt.style.display = "";
    }
    if (at > (tempat - 28) && onit) {
        num++;
        temp.style.top = at - 1;
        Stop = setTimeout(function () {
            moveup(temp, tempat, temptxt, temprec);
        }, 10);
    } else {
        return;
    }
}
function movedown(iteam, top, txt, rec) {
    var temp = eval(iteam);
    var temptxt = eval(txt);
    var temprec = eval(rec);
    clearTimeout(Stop);
    temp.style.top = top;
    num = 0;
    temptxt.style.display = "none";
    temprec.style.display = "none";
}
function ontxt(iteam, top, txt, rec) {
    var temp = eval(iteam);
    var temptxt = eval(txt);
    var temprec = eval(rec);
    if (onit) {
        temp.style.top = top - 28;
        temptxt.style.display = "";
        temprec.style.display = "";
    }
}
function movereset(over) {
    if (over == 1) {
        onit = false;
    } else {
        onit = true;
    }
}
function table_n_ie(sa, num, tot1, tot2, href01) {
    var i;
    var allvalues = 0;
    for (i = 0; i < num; i++) {
        allvalues += sa[0][i];
    }
    document.write("<br/><p align=center><font size=5 color=blue>Statistics</font></p>");
    document.write("<table class='table-default table-back problem-status' width=80% align=center>");
    document.write("<tr><td width=80%>Total Submissions</td><td align=right><a href=" + href01 + ">" + allvalues + "</a></td></tr>");
    document.write("<tr><td>Users (Submitted)</td><td align=right>" + tot1 + "</td></tr>");
    document.write("<tr><td>Users (Solved)</td><td align=right>" + tot2 + "</td></tr>");
    for (i = 0; i < num; i++) {
        document.write("<tr><td>" + sa[1][i] + "</td><td align=right>" + "<a href=" + sa[2][i] + ">" + sa[0][i] + "</a></td></tr>");
    }
    document.write("</table>");
}
function table(sa, table_left, table_top, all_width, all_height, table_title, unit, radius, l_width, tot1, tot2, href01) {
    var num = Math.max(sa[0].length, sa[1].length);
    if (!(window.navigator.userAgent.indexOf("MSIE 6.0") >= 1 || window.navigator.userAgent.indexOf("MSIE 7.0") >= 1)) {
        table_n_ie(sa, num, tot1, tot2, href01);
        return;
    }
    var allvalues = 0, i, j;
    var color = ["#19ff19", "#ff8c19", "#ff1919", "#ffff19", "#1919ff", "#fc0000", "#3cc000", "#ff19ff", "#993300", "#f60000"];
    var bg_color = new Array(num);
    var pie = new Array(num);
    for (i = 0, j = 0; i < num; i++, j++) {
        bg_color[i] = color[j];
        if (j == color.length) {
            j = -1;
        }
    }
    for (i = 0; i < num; i++) {
        allvalues += sa[0][i];
    }
    var k = 0;
    for (i = 0; i < num - 1; i++) {
        pie[i] = parseInt((sa[0][i]) / allvalues * 10000) / 10000;
        k += pie[i];
    }
    pie[num - 1] = 1 - k;
    document.writeln("<v:shapetype id='Cake_3D' coordsize='21600,21600' o:spt='95' adj='11796480,5400' path='al10800,10800@0@0@2@14,10800,10800,10800,10800@3@15xe'></v:shapetype>");
    document.writeln("<v:shapetype id='3dtxt' coordsize='21600,21600' o:spt='136' adj='10800' path='m@7,l@8,m@5,21600l@6,21600e'>");
    document.writeln("<v:path textpathok='t' o:connecttype='custom' o:connectlocs='@9,0;@10,10800;@11,21600;@12,10800' o:connectangles='270,180,90,0'/>");
    document.writeln("<v:textpath on='t' fitshape='t'/>");
    document.writeln("<o:lock v:ext='edit' text='t' shapetype='t'/>");
    document.writeln("</v:shapetype>");
    document.writeln("<v:group ID='table' style='position:absolute;left:" + table_left + "px;top:" + table_top + "px;WIDTH:" + l_width + "px;HEIGHT:" + all_height + "px;' coordsize = '21000,11500'>");
    document.writeln("<v:Rect style='position:relative;left:500;top:200;width:20000;height:800'filled='false' stroked='false'>");
    document.writeln("<v:TextBox inset='0pt,0pt,0pt,0pt'>");
    document.writeln("<table width='100%' border='0' align='center' cellspacing='0'>");
    document.writeln("<tr>");
    document.writeln("<td align='center' valign='middle'><div style='font-size:15pt; font-family:Arial, Helvetica, sans-serif;'><B>" + table_title + "</B></div></td>");
    document.writeln("</tr>");
    document.writeln("</table>");
    document.writeln("</v:TextBox>");
    document.writeln("</v:Rect> ");
    var height0 = 7000 / 11;
    document.writeln("<v:rect id='back' style='position:relative;left:500;top:1000;width:20000; height:" + ((num + 3) * height0 + 3400) + ";' onmouseover='movereset(1)' onmouseout='movereset(0)' fillcolor='#9cf' strokecolor='#888888'>");
    document.writeln("<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>");
    document.writeln("</v:rect>");
    document.writeln("<v:rect id='back' style='position:relative;left:800;top:4100;width:18000; height:" + ((num + 3) * height0 + 0) + ";' fillcolor='#9cf' stroked='t' strokecolor='#0099ff'>");
    document.writeln("<v:fill rotate='t' angle='-175' focus='100%' type='gradient'/>");
    document.writeln("<v:shadow on='t' type='single' color='silver' offset='3pt,3pt'/>");
    document.writeln("</v:rect>");
    document.writeln("<a class=sssss0 style='cursor:hand;' href='" + href01 + "'>");
    document.writeln("<v:Rect id='recrec11' style='position:relative;left:1300;top:4200;width:17000;height:500' fillcolor='#000000' stroked='f' strokecolor='#000000'>");
    document.writeln("<v:TextBox inset='8pt,4pt,3pt,3pt' style='font-size:11pt; font-family:Arial, Helvetica, sans-serif;'><div><div class=sd1>Total Submissions:</div><div class=sd2>" + allvalues + unit + "</div></div></v:TextBox>");
    document.writeln("</v:Rect></a>");
    document.writeln("<v:Rect style='position:relative;left:1300;top:4700;width:17000;height:500' fillcolor='#000000' stroked='f' strokecolor='#000000'>");
    document.writeln("<v:TextBox inset='8pt,4pt,3pt,3pt' style='font-size:11pt; font-family:Arial, Helvetica, sans-serif;'><div align='left'><font color='#FFF'><B><div class=sd1>Users (Submitted):</div><div class=sd2>" + tot1 + unit + "</div></B></font></div></v:TextBox>");
    document.writeln("</v:Rect> ");
    document.writeln("<v:Rect style='position:relative;left:1300;top:5200;width:17000;height:500' fillcolor='#000000' stroked='f' strokecolor='#000000'>");
    document.writeln("<v:TextBox inset='8pt,4pt,3pt,3pt' style='font-size:11pt; font-family:Arial, Helvetica, sans-serif;'><div align='left'><font color='#FFF'><B><div class=sd1>Users (Solved):</div><div class=sd2>" + tot2 + unit + "</div></B></font></div></v:TextBox>");
    document.writeln("</v:Rect> ");
    for (i = 0; i < num; i++) {
        document.writeln("<a class=\"sssss\" style='cursor:hand;' onmouseover='moveup(cake" + i + "," + (table_top + radius / 14) + ",txt" + i + ",rec" + i + ")'; onmouseout='movedown(cake" + i + "," + (table_top + radius / 14) + ",txt" + i + ",rec" + i + ");' href=" + sa[2][i] + ">");
        document.writeln("<v:Rect id='rec" + i + "' style='position:relative;left:1100;top:" + Math.round((i + 3) * height0 + 3850) + ";width:17000;height:600;display:none' fillcolor='#efefef' strokecolor='" + bg_color[i] + "'>");
        document.writeln("<v:fill opacity='.6' color2='fill darken(118)' o:opacity2='.6' rotate='t' method='linear sigma' focus='100%' type='gradient'/>");
        document.writeln("</v:Rect>");
        document.writeln("<v:Rect style='position:relative;left:1300;top:" + Math.round((i + 3) * height0 + 3900) + ";width:1300;height:500' fillcolor='" + bg_color[i] + "' stroked='f'/>");
        document.writeln("<v:Rect style='position:relative;left:3100;top:" + Math.round((i + 3) * height0 + 3900) + ";width:14400;height:500' filled='f' stroked='f'>");
        document.writeln("<v:TextBox inset='0pt,5pt,0pt,0pt' style='font-size:10pt; font-family:Arial, Helvetica, sans-serif; cursor:hand;'><div><div style='position:absolute; left:0px'>" + sa[1][i] + ":</div><div style='position:absolute; right:0px'><b>" + sa[0][i] + unit + "</b></div></div></v:TextBox>");
        document.writeln("</v:Rect>");
        document.writeln("</a>");
    }
    document.writeln("</v:group>");
    var k1 = 180;
    var k4 = 10;
    for (i = 0; i < num; i++) {
        var k2 = 360 * pie[i] / 2;
        var k3 = k1 + k2;
        if (k3 >= 360) {
            k3 = k3 - 360;
        }
        var kkk = (-11796480 * pie[i] + 5898240);
        var k5 = 3.1414926 * 2 * (180 - (k3 - 180)) / 360;
        var R = radius / 2;
        var txt_x = table_left + radius / 8 - 30 + R + R * Math.sin(k5) * 0.7;
        var txt_y = table_top + radius / 14 - 39 + R + R * Math.cos(k5) * 0.7 * 0.5;
        var titlestr = "Result     : " + sa[1][i] + "&#13;&#10;Number     : " + sa[0][i] + unit + "&#13;&#10;Percentage : " + Math.round(pie[i] * 10000) / 100 + "%&nbsp;&nbsp;";
        document.writeln("<div style='cursor:hand;'>");
        document.writeln("<v:shape id='cake" + i + "' type='#Cake_3D' title='" + titlestr + "'");
        document.writeln("style='position:absolute;left:" + (table_left + radius / 8) + "px;top:" + (table_top + radius / 14) + "px;WIDTH:" + radius + "px;HEIGHT:" + radius + "px;rotation:" + k3 + ";z-index:" + k4 + "'");
        document.writeln("adj='" + kkk + ",0' fillcolor='" + bg_color[i] + "' onmouseover='moveup(cake" + i + "," + (table_top + radius / 14) + ",txt" + i + ",rec" + i + ")'; onmouseout='movedown(cake" + i + "," + (table_top + radius / 14) + ",txt" + i + ",rec" + i + ");'>");
        document.writeln("<v:fill opacity='60293f' color2='fill lighten(120)' o:opacity2='60293f' rotate='t' angle='-135' method='linear sigma' focus='100%' type='gradient'/>");
        document.writeln("<o:extrusion v:ext='view' on='t' backdepth='25' rotationangle='60' viewpoint='0,0'viewpointorigin='0,0' skewamt='0' lightposition='-50000,-50000' lightposition2='50000'/>");
        document.writeln("</v:shape>");
        document.writeln("<v:shape id='txt" + i + "' type='#3dtxt' style='position:absolute;left:" + txt_x + "px;top:" + txt_y + "px;z-index:20;display:none;width:50; height:10;' fillcolor='black'");
        document.writeln("onmouseover='ontxt(cake" + i + "," + (table_top + radius / 14) + ",txt" + i + ",rec" + i + ")'>");
        document.writeln("<v:textpath style=\"font-family:Arial; v-text-kern:t\" trim='true' string='" + Math.round(pie[i] * 10000) / 100 + "%'/>");
        document.writeln("</v:shape>");
        document.writeln("</div>");
        k1 = k1 + k2 * 2;
        if (k1 >= 360) {
            k1 = k1 - 360;
        }
        if (k1 > 180) {
            k4 = k4 + 1;
        } else {
            k4 = k4 - 1;
        }
    }
}
