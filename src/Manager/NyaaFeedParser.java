package Manager;

import javax.print.DocFlavor;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import VO.FileData;
import VO.Nyaa_si.Nyaa_si_Feed;
import VO.Nyaa_si.Nyaa_si_FeedMessage;

public class NyaaFeedParser {

    private List<FileData> nyaaUrlList;
    private Nyaa_si_Feed nyaaFeed;


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

    //url 리스트 작성
    public void saveUrlList(FileData fileData) {
        nyaaUrlList.add(fileData);
    }

    //torrent 파일 저장
    public void saveFile() {
        OutputStream out = null;
        InputStream in = null;
        URLConnection conn = null;

        try {

            for (int i = 0; i < nyaaUrlList.size(); i++) {
                String name = nyaaUrlList.get(i).getFileName();
                URL url = new URL(nyaaUrlList.get(i).getFileUrl());

                String dirFile = "D:\\RssExam\\" + name + ".torrent";
                String path = dirFile.substring(0,dirFile.lastIndexOf(File.separator));

                File f = new File(path);
                if (!f.exists()){
                    f.mkdirs();
                }
                File filePath = new File(dirFile);

                out = new BufferedOutputStream(new FileOutputStream(filePath));
                conn = url.openConnection();
                in = conn.getInputStream();
                byte[] buffer = new byte[10240];


                int numRead;
                long numWritten = 0;


                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                }
                System.out.println(nyaaUrlList.get(i).getFileName() + "\t" + numWritten);
                System.out.println(nyaaUrlList.get(i).getFileName() + " 다운 완료");
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("파일 저장 오류");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }

    }

    public NyaaFeedParser(String url) {
        try {
            nyaaUrlList = new ArrayList<>();
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
        nyaaFeed = null;


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
