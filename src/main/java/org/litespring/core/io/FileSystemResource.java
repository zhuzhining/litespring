package org.litespring.core.io;

import org.litespring.util.Assert;

import java.io.*;

public class FileSystemResource implements Resource {

    private String path;

    private File file;

    public FileSystemResource(String path) {
        Assert.notNull(path, "文件路径不能为空");
        this.path = path;
        this.file = new File(path);
    }

    public FileSystemResource(File file) {
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public String getDescription() {
        return this.path;
    }
}
