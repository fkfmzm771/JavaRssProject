package Manager;

import VO.Nyaa_si;

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

public class NyaaManager {
    private List<Nyaa_si> nyaaList;


    private URL url;

    public NyaaManager(String url) {
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

    public Nyaa_si readFeed() {
        Nyaa_si nyaaFeed = null;


        try {
            boolean isFeedHeader = true;

            String title = "";   //타이틀
            String link = "";    //토런트 파일 링크
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
                        case "item":
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                nyaaFeed = new Nyaa_si(title
                                        , link
                                        , guid
                                        , pubDate
                                        , nyaa_seeders
                                        , nyaa_leechers
                                        , nyaa_downloads
                                        , nyaa_infoHash
                                        , nyaa_categoryId
                                        , nyaa_category
                                        , nyaa_size);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case "link":
                            link = getCharacterData(event, eventReader);
                            break;


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
