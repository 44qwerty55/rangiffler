package rangiffler.data;

import org.apache.commons.lang3.StringUtils;

public enum DataBase {
    USERDATA("jdbc:postgresql://127.0.0.1:5432/rangiffler-userdata"),
    AUTH("jdbc:postgresql://127.0.0.1:5432/rangiffler-auth"),
    GEO("jdbc:postgresql://127.0.0.1:5432/rangiffler-geo");

    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlForP6Spy() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(url, "jdbc:");
    }
}
