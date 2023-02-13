package net.mediaheap.namespace;

public record KeySchema(String displayName, String link) {
    public String formatLink(String value) {
        if (value == null || link == null) {
            return null;
        }
        return link.replace("{}", value);
    }
}
