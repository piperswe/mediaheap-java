package net.mediaheap.namespace;

public class KeySchema {
    private String displayName;
    private String link;

    public String getDisplayName() {
        return displayName;
    }

    public String getLink() {
        return link;
    }

    public String formatLink(String value) {
        if (value == null || link == null) {
            return null;
        }
        return link.replace("{}", value);
    }
}
