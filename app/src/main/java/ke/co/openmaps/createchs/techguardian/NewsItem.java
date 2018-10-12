package ke.co.openmaps.createchs.techguardian;

public class NewsItem {
    private String title;
    private String section;
    private String publicationDate;
    private String url;
    private String contributor;

    public NewsItem(String title, String section, String url, String publicationDate,
                    String contributor) {
        this.title = title;
        this.section = section;
        this.url = url;
        this.publicationDate = publicationDate;
        this.contributor = contributor;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getUrl() {
        return url;
    }

    public String getContributor() {
        return contributor;
    }
}
