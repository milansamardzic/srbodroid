package com.srbodroid.app.network;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainApp;
import com.srbodroid.app.model.Image;
import com.srbodroid.app.model.News;
import com.srbodroid.app.model.NewsDao;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.Utility;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by pedja on 1/22/14.
 * Class responsible for handling all parsing of server responses in xml format
 *
 * @author Predrag Čokulov
 */
public class XMLUtility
{
    private static final Pattern imgPattern = Pattern.compile("<img.+?src=\"(.+?)\".*?(/>|</.*img>)", Pattern.DOTALL);
    private static final Pattern iFrameTagPattern = Pattern.compile("<iframe src=\"(.+?)\".+?/(iframe)*>");
    private static final Pattern idPattern = Pattern.compile(".+\\?p=(\\d+)");
    private final Internet.Response serverResponse;

    private Object parseObject;

    public enum Key
    {
        content_encoded("content:encoded"), guid, wfw_commentRss("wfw:commentRss"), title,
        description, item, dc_creator("dc:creator"), pubDate, slash_comments("slash:comments"),
        feedburner_origLink("feedburner:origLink"), category, comments;

        String mValue;

        Key(String mValue)
        {
            this.mValue = mValue;
        }

        Key()
        {
        }


        @Override
        public String toString()
        {
            return mValue == null ? super.toString() : mValue;
        }
    }

    public XMLUtility(Internet.Response serverResponse)
    {
        this.serverResponse = serverResponse;
    }

    public void parseNewsRss()
    {
        if(!isResponseOk())return;
        try
        {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            NewsRssHandler saxHandler = new NewsRssHandler();
            xmlReader.setContentHandler(saxHandler);
            xmlReader.parse(new InputSource(serverResponse.responseStream));
            MainApp.getInstance().getDaoSession().getNewsDao().insertOrReplaceInTx(saxHandler.getNewsItems());
            NewsDao dao = MainApp.getInstance().getDaoSession().getNewsDao();
            QueryBuilder<News> builder = dao.queryBuilder();
            builder.orderDesc(NewsDao.Properties.PubDate);
            parseObject = builder.list();
        }
        catch (Exception e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    private boolean isResponseOk()
    {
        return serverResponse!= null && serverResponse.isResponseOk() && serverResponse.responseStream != null;
    }


    public <T> T getParseObject()
    {
        return (T) parseObject;
    }

    public <T> T getParseObject(Class<T> type)
    {
        return (T) parseObject;
    }

    public Internet.Response getServerResponse()
    {
        return serverResponse;
    }

    @Override
    public String toString()
    {
        return serverResponse != null ? serverResponse.toString() : null;
    }

    public class NewsRssHandler extends DefaultHandler
    {
        private List<News> newsItems;
        private StringBuilder builder;
        private News tempItem;

        public NewsRssHandler()
        {
            newsItems = new ArrayList<>();
        }

        public List<News> getNewsItems()
        {
            return newsItems;
        }

        // Event Handlers
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            // reset
            builder = new StringBuilder();
            if (qName.equalsIgnoreCase(Key.item.toString()))
            {
                // create a new instance of employee
                tempItem = new News();
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException
        {
            if(builder != null)builder.append(new String(ch, start, length));
        }

        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if(tempItem == null)return;
            String tempVal = builder.toString();
            if (qName.equalsIgnoreCase(Key.item.toString()))
            {
                // add it to the list
                newsItems.add(tempItem);
            }
            else if (qName.equalsIgnoreCase(Key.guid.toString()))
            {
                Matcher matcher = idPattern.matcher(tempVal);
                if(matcher.matches())tempItem.setId(Long.parseLong(matcher.group(1)));
            }
            else if (qName.equalsIgnoreCase(Key.title.toString()))
            {
                tempItem.setTitle(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.feedburner_origLink.toString()))
            {
                tempItem.setLink(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.content_encoded.toString()))
            {
                tempItem.setContent(tempVal);
                List<Image> images = new ArrayList<>();
                Matcher matcher = imgPattern.matcher(tempItem.getContent());
                while (matcher.find())
                {
                    Image image = new Image();
                    image.setNews_id(tempItem.getId());
                    image.setUrl(matcher.group(1));
                    images.add(image);
                }
                MainApp.getInstance().getDaoSession().getImageDao().insertOrReplaceInTx(images);
                tempItem.setContent(matcher.replaceAll(""));//remove all images
                Matcher iFrameMatcher = iFrameTagPattern.matcher(tempItem.getContent());
                //replace all iframes with links, since tv doesn't support iframes
                while (iFrameMatcher.find())
                {
                    String tmp = tempItem.getContent();
                    tmp = tmp.replace(iFrameMatcher.group(), "<a href=\"" + iFrameMatcher.group(1) + "\">" + iFrameMatcher.group(1) + "</a>");
                    tempItem.setContent(tmp);
                }
            }
            else if (qName.equalsIgnoreCase(Key.dc_creator.toString()))
            {
                tempItem.setAuthor(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.description.toString()))
            {
                tempItem.setDescription(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.wfw_commentRss.toString()))
            {
                tempItem.setCommentsFeedLink(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.slash_comments.toString()))
            {
                tempItem.setComment_count(Utility.parseLong(tempVal, 0));
            }
            else if (qName.equalsIgnoreCase(Key.comments.toString()))
            {
                tempItem.setCommentsLink(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.category.toString()))
            {
               if(tempItem.getCategory() == null) tempItem.setCategory(tempVal);
            }
            else if (qName.equalsIgnoreCase(Key.pubDate.toString()))
            {
                long pubDate = Utility.parseDateTime(Constants.NEWS_TIME_FORMAT_IN, tempVal).getTime();
                tempItem.setPubDate(pubDate);
            }
        }
    }
}
