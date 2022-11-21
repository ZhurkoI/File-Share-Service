package org.zhurko.fileshare.model;

import java.util.Objects;

public class File {

    private Long id;
    private String filePath;

    public File() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return id.equals(file.id) && filePath.equals(file.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath);
    }
}
