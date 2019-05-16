package cn.edu.zjnu.acm.judge.sandbox;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author zhanhb
 */
public interface Executor extends Closeable {

    int _O_RDONLY = 0;
    int _O_WRONLY = 1;
    int _O_RDWR = 2;
    int _O_TEMPORARY = 0x0040;
    int _O_CREAT = 0x0100;
    /* Create the file if it does not exist. */
    int _O_TRUNC = 0x0200;
    /* Truncate the file if it does exist. */
    int O_RDONLY = _O_RDONLY;
    int O_WRONLY = _O_WRONLY;
    int O_RDWR = _O_RDWR;
    int O_CREAT = _O_CREAT;
    int O_TRUNC = _O_TRUNC;
    int O_TEMPORARY = _O_TEMPORARY;
    int O_SYNC = 0x0800;
    int O_DSYNC = 0x2000;

    ExecuteResult execute(Options options) throws IOException;

    @Override
    void close();

}
