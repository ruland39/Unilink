package com.example.unilink.Models.Interests;

public class Interest {
    private String name;
    private Category category;

    public Interest(String name, Category cat) {
        this.name = name;
        this.category = cat;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }
}
