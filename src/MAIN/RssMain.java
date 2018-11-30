package MAIN;

import Manager.NyaaFeedParser;
import Manager.TokyoFeedParser;
import VO.FileData;
import VO.Nyaa_si.Nyaa_si_Feed;
import VO.Nyaa_si.Nyaa_si_FeedMessage;

public class RssMain {
	public static void main(String[] args) {

		TokyoFeedParser parser1 = new TokyoFeedParser("https://www.tokyotosho.info/rss.php?terms=baki");
		NyaaFeedParser parser = new NyaaFeedParser("https://nyaa.si/?page=rss&q=baki&c=0_0&f=0");
		Nyaa_si_Feed feed = parser.readFeed();
		for (Nyaa_si_FeedMessage message : feed.getMessages()) {
			parser.saveUrlList(new FileData(message.getTitle(),message.getLink()));
			System.out.println(message);
		}
		parser.saveFile();

	}
}
