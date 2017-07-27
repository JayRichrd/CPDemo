package com.jy.cpdemo;
/*
 *   author RichardJay
 *    email jiangfengfn12@163.com
 *  created 2017/7/27 17:29
 *  version v1.0
 * modified 2017/7/27
 *     note xxx
 */

public class Person {
    private String name;
    private int id;
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
