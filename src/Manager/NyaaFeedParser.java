package Manager;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import VO.*;

public class NyaaFeedParser {
	private List<Nyaa_si_FeedMessage> nyaaList;

	static final String TITLE = "title";   //타이틀
	static final String LINK = "link";    //토런트 파일 링크
	static final String GUID = "guid";    //해당 페이지 링크
	static final String PUBDATE = "pubDate";     //업로드 날짜
	static final String NYAA_SEEDERS = "seeders";    //배포자 숫자
	static final String NYAA_LEECHERS = "leechers";   //공유 인원
	static final String NYAA_DOWNLOADS = "downloads";  //다운로드 숫자
	static final String NYAA_INFOHASH = "infoHash";   //해쉬코드
	static final String NYAA_CATEGORYID = "categoryId"; //카테고리 아이디
	static final String NYAA_CATEGORY = "category";   //카테고리
	static final String NYAA_SIZE = "size";   //파일 용량
	static final String ITEM = "item";   //아이템


	private URL url;

	public NyaaFeedParser(String url) {
		try {
			nyaaList = new ArrayList<>();
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Nyaa_si_Feed readFeed() {
		Nyaa_si_Feed nyaaFeed = null;


		try {
			boolean isFeedHeader = true;

			String title = "";   //타이틀
			String description = "";
			String link = "";    //해당 페이지 링크
			String guid = "";    //해당 페이지 링크
			String pubDate = "";     //업로드 날짜
			String nyaa_seeders = "";    //배포자 숫자
			String nyaa_leechers = "";   //공유 인원
			String nyaa_downloads = "";  //다운로드 숫자
			String nyaa_infoHash = "";   //해쉬코드
			String nyaa_categoryId = ""; //카테고리 아이디
			String nyaa_category = "";   //카테고리
			String nyaa_size = "";   //파일 용량

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String localPart = event.asStartElement().getName().getLocalPart();
					switch (localPart) {
						case ITEM:
							if (isFeedHeader) {
								isFeedHeader = false;
								nyaaFeed = new Nyaa_si_Feed(title, description, link);
							}
							event = eventReader.nextEvent();
							break;
						case TITLE:
							title = getCharacterData(event, eventReader);
							break;
						case LINK:
							link = getCharacterData(event, eventReader);
							break;
						case GUID:
							guid = getCharacterData(event, eventReader);
							break;
						case PUBDATE:
							pubDate = getCharacterData(event, eventReader);
							break;
						case NYAA_SEEDERS:
							nyaa_seeders = getCharacterData(event, eventReader);
							break;
						case NYAA_LEECHERS:
							nyaa_leechers = getCharacterData(event, eventReader);
							break;
						case NYAA_DOWNLOADS:
							nyaa_downloads = getCharacterData(event, eventReader);
							break;
						case NYAA_INFOHASH:
							nyaa_infoHash = getCharacterData(event, eventReader);
							break;
						case NYAA_CATEGORYID:
							nyaa_categoryId = getCharacterData(event, eventReader);
							break;
						case NYAA_CATEGORY:
							nyaa_category = getCharacterData(event, eventReader);
							break;
						case NYAA_SIZE:
							nyaa_size = getCharacterData(event, eventReader);
							break;
					}
				} else if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
						Nyaa_si_FeedMessage message = new Nyaa_si_FeedMessage();
						message.setGuid(guid);
						message.setLink(link);
						message.setNyaa_category(nyaa_category);
						message.setNyaa_categoryId(nyaa_categoryId);
						message.setNyaa_downloads(nyaa_downloads);
						message.setNyaa_infoHash(nyaa_infoHash);
						message.setNyaa_leechers(nyaa_leechers);
						message.setNyaa_seeders(nyaa_seeders);
						message.setNyaa_size(nyaa_size);
						message.setPubDate(pubDate);
						message.setTitle(title);


						nyaaFeed.getMessages().add(message);
						event = eventReader.nextEvent();
						continue;
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return nyaaFeed;
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
			throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}


		return result;
	}

}
