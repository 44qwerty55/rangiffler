package rangiffler.config;

public interface Config {

    String PROJECT_NAME = "rangiffler";

    static Config getConfig() {
        return new LocalConfig();
        /*
        if ("docker".equals(System.getProperty("test.env"))) {
            return new DockerConfig();
        } else if ("local".equals(System.getProperty("test.env"))) {
            return new LocalConfig();
        } else throw new IllegalStateException();

         */
    }

    String authUrl();

    String frontUrl();

    String userdataUrl();

    String photoUrl();

}