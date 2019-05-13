package cn.edu.zjnu.acm.judge.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author zhanhb
 */
public class UnixLineEndingPrintWriter extends PrintWriter {

    public UnixLineEndingPrintWriter(OutputStream out) {
        super(new OutputStreamWriter(out, Platform.getCharset()));
    }

    @Override
    public void println() {
        super.write("\n");
        super.flush();
    }

}
