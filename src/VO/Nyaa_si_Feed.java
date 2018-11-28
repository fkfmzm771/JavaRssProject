package VO;

import java.util.ArrayList;
import java.util.List;

public class Nyaa_si_Feed {
	private String title;   //타이틀
	private String description;
	private String link;    //해당 페이지 링크
	final List<Nyaa_si_FeedMessage> entries = new ArrayList<>();


	public Nyaa_si_Feed() {
	}

	public Nyaa_si_Feed(String title, String description, String link) {
		this.title = title;
		this.description = description;
		this.link = link;
	}

	public List<Nyaa_si_FeedMessage> getMessages() {
		return entries;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Nyaa_si_Feed{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
				", link='" + link + '\'' +
				'}';
	}
}
