package MAIN;

import Manager.NyaaFeedParser;
import VO.Nyaa_si_Feed;
import VO.Nyaa_si_FeedMessage;

public class RssMain {
	public static void main(String[] args) {

		NyaaFeedParser parser = new NyaaFeedParser("https://nyaa.si/?page=rss&q=baki&c=0_4&f=0");
		Nyaa_si_Feed feed = parser.readFeed();
		for (Nyaa_si_FeedMessage message : feed.getMessages()) {
			System.out.println(message);
		}
	}
}
