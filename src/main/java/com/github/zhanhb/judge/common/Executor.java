package com.github.zhanhb.judge.common;

import java.io.IOException;

public interface Executor {

    int _O_RDONLY = 0;
    int _O_WRONLY = 1;
    int _O_RDWR = 2;

    /* Mask for access mode bits in the _open flags. */
    int _O_ACCMODE = _O_WRONLY | _O_RDWR;

    int _O_APPEND = 0x0008;
    /* Writes will add to the end of the file. */

    int _O_RANDOM = 0x0010;
    int _O_SEQUENTIAL = 0x0020;
    int _O_TEMPORARY = 0x0040;
    /* Make the file dissappear after closing.
     * WARNING: Even if not created by _open! */
    int _O_NOINHERIT = 0x0080;

    int _O_CREAT = 0x0100;
    /* Create the file if it does not exist. */
    int _O_TRUNC = 0x0200;
    /* Truncate the file if it does exist. */
    int _O_EXCL = 0x0400;
    /* Open only if the file does not exist. */

    int _O_SHORT_LIVED = 0x1000;

    /* NOTE: Text is the default even if the given _O_TEXT bit is not on. */
    int _O_TEXT = 0x4000;
    /* CR-LF in file becomes LF in memory. */
    int _O_BINARY = 0x8000;
    /* Input and output is not translated. */
    int _O_RAW = _O_BINARY;

    int _O_WTEXT = 0x10000;
    int _O_U16TEXT = 0x20000;
    int _O_U8TEXT = 0x40000;

    /* POSIX/Non-ANSI names for increased portability */
    int O_RDONLY = _O_RDONLY;
    int O_WRONLY = _O_WRONLY;
    int O_RDWR = _O_RDWR;
    int O_ACCMODE = _O_ACCMODE;
    int O_APPEND = _O_APPEND;
    int O_CREAT = _O_CREAT;
    int O_TRUNC = _O_TRUNC;
    int O_EXCL = _O_EXCL;
    int O_TEXT = _O_TEXT;
    int O_BINARY = _O_BINARY;
    int O_TEMPORARY = _O_TEMPORARY;
    int O_NOINHERIT = _O_NOINHERIT;
    int O_SEQUENTIAL = _O_SEQUENTIAL;
    int O_RANDOM = _O_RANDOM;
    int O_SYNC = 0x0800;
    int O_DSYNC = 0x2000;

    ExecuteResult execute(Options options) throws IOException;

}
