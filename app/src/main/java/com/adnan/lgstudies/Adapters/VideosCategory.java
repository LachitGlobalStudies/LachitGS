package com.adnan.lgstudies.Adapters;

public class VideosCategory {
    String live_class,revision_classes,previous_classes;

    public VideosCategory() {

    }

    public VideosCategory(String live_class, String revision_classes, String previous_classes) {
        this.live_class = live_class;
        this.revision_classes = revision_classes;
        this.previous_classes = previous_classes;
    }

    public String getLive_class() {
        return live_class;
    }

    public void setLive_class(String live_class) {
        this.live_class = live_class;
    }

    public String getRevision_classes() {
        return revision_classes;
    }

    public void setRevision_classes(String revision_classes) {
        this.revision_classes = revision_classes;
    }

    public String getPrevious_classes() {
        return previous_classes;
    }

    public void setPrevious_classes(String previous_classes) {
        this.previous_classes = previous_classes;
    }
}
