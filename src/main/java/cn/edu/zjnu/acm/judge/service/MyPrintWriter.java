package cn.edu.zjnu.acm.judge.service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MyPrintWriter extends PrintWriter {

    public MyPrintWriter(OutputStream out) {
        super(new OutputStreamWriter(out, Platform.getCharset()));
    }

    @Override
    public void println() {
        super.write("\n");
        super.flush();
    }

}
