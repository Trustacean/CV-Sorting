package main.java.models;

import java.io.File;

public class CV {
    private String name;
    private String id;
    private String position1;
    private String position2;
    private File file;

    public CV(String name, String id, String position1, String position2, File file) {
        this.name = name;   
        this.id = id;
        this.position1 = position1;
        this.position2 = position2;
        this.file = file;
    }

    public CV(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPosition1() {
        return position1;
    }

    public String getPosition2() {
        return position2;
    }

    public File getFile() {
        return file;
    }
}
