function table_n_ie(sa, num, tot1, tot2, href01) {
    var i;
    var allvalues = 0;
    for (i = 0; i < num; i++) {
        allvalues += sa[0][i];
    }
    document.write('<br/><p align=center><font size=5 color=blue>Statistics</font></p>');
    document.write('<table class="table-default table-back problem-status" width=80% align=center>');
    document.write('<tr><td width=80%>Total Submissions</td><td align=right><a href=' + href01 + '>' + allvalues + '</a></td></tr>');
    document.write('<tr><td>Users (Submitted)</td><td align=right>' + tot1 + '</td></tr>');
    document.write('<tr><td>Users (Solved)</td><td align=right>' + tot2 + '</td></tr>');
    for (i = 0; i < num; i++) {
        document.write('<tr><td>' + sa[1][i] + '</td><td align=right>' + '<a href=' + sa[2][i] + '>' + sa[0][i] + '</a></td></tr>');
    }
    document.write('</table>');
}
function table(sa, tot1, tot2, href01) {
    var num = Math.max(sa[0].length, sa[1].length);
    table_n_ie(sa, num, tot1, tot2, href01);
}
