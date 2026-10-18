package com.adnan.lgstudies.Adapters;

public class Notes {
    String name,pdf_link;
    boolean isPremium;

    public Notes() {
    }

    public Notes(String name, String pdf_link, boolean isPremium) {
        this.name = name;
        this.pdf_link = pdf_link;
        this.isPremium = isPremium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
