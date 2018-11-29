package MAIN;

import Manager.NyaaFeedParser;
import Manager.TokyoFeedParser;
import VO.Nyaa_si.Nyaa_si_Feed;
import VO.Nyaa_si.Nyaa_si_FeedMessage;

public class RssMain {
	public static void main(String[] args) {

		TokyoFeedParser parser1 = new TokyoFeedParser("https://www.tokyotosho.info/rss.php?terms=baki");
		NyaaFeedParser parser = new NyaaFeedParser("http://leopard-raws.org/rss.php?terms=piece");
		Nyaa_si_Feed feed = parser.readFeed();
		for (Nyaa_si_FeedMessage message : feed.getMessages()) {
			System.out.println(message);
		}

	}
}
