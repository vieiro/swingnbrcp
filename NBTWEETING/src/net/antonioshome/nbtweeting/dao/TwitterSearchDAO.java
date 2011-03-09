/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.antonioshome.nbtweeting.entities.Query;
import net.antonioshome.nbtweeting.entities.Tweet;

/**
 * TwitterSearchDAO is responsible for performing a search on Twitter
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class TwitterSearchDAO {

  private static final String ATOM_NS = "http://www.w3.org/2005/Atom";

  public TwitterSearchDAO() {
  }

  public List<Tweet> search(String query)
    throws IOException, XMLStreamException {

    // Prepare the twitter search url
    // This should be something like:
    // http://search.twitter.com/search.atom?q=%23netbeans&lang=en&rpp=100
    StringBuilder sb = new StringBuilder();
    sb.append("http://search.twitter.com/search.atom");
    sb.append("?q=").append(URLEncoder.encode(query, "UTF-8"));
    sb.append("&rpp=100");

    URL searchURL = new URL(sb.toString());

    // Open an HTTPUrlConnection to twitter...
    HttpURLConnection conn = (HttpURLConnection) searchURL.openConnection();

    conn.setDoInput(true);
    conn.connect();
    InputStream input = conn.getInputStream();

    // Parse the XML output anc reate a list of tweet entities
    List<Tweet> tweets = scan(input);

    // Clean up and return the list
    input.close();
    conn.disconnect();
    return tweets;
  }

  /**
   * Parses the XML output from Twitter (Atom format) and returns a list
   * of tweets.
   * @param input An InputSream with Atom format from Twitter Search API.
   * @return A list of Tweet entities.
   * @throws XMLStreamException If an I/O error happens.
   * @throws MalformedURLException If the ATOM namespace is wrong, shouldn't happen.
   */
  List<Tweet> scan(InputStream input) throws XMLStreamException, MalformedURLException {

    if (input == null) {
      throw new NullPointerException("Input cannot be null");
    }

    // We use Stax for parsing the XML output, only some parts of the atom feed required.
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setXMLResolver(new XMLResolver()   {

      public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
        System.out.println("Resolving entity: " + publicID + ":" + systemID + ":" + baseURI + ":" + namespace);
        return null;
      }
    });

    XMLStreamReader reader = factory.createXMLStreamReader(input);


    ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    do {
      reader = moveTo(reader, "entry");
      if (reader == null) {
        break;
      }
      tweets.add(parseTweet(reader));

    } while (reader.hasNext());


    return tweets;
  }

  /**
   * Parses a 'entry' XML element and returns a Tweet
   * @param reader The XMLStreamReader located on a entry element
   * @return The Tweet
   * @throws XMLStreamException
   * @throws MalformedURLException 
   */
  private Tweet parseTweet(XMLStreamReader reader) throws XMLStreamException, MalformedURLException {

//    <entry>
//    <id>tag:search.twitter.com,2005:43911377227755521</id>
//    <published>2011-03-05T05:51:02Z</published>
//    <link type="text/html" href="http://twitter.com/nrcpts/statuses/43911377227755521" rel="alternate"/>
//    <title>RT @netbeans: Go show some love on the #NetBeans #PHP blog if you're one of the 25% of NetBeans 6.9 users using a PHP project: http://bit.ly/fG74xF :)</title>
//    <content type="html">RT &lt;a href=&quot;http://twitter.com/netbeans&quot;&gt;@netbeans&lt;/a&gt;: Go show some love on the &lt;a href=&quot;http://search.twitter.com/search?q=%23NetBeans&quot; onclick=&quot;pageTracker._setCustomVar(2, 'result_type', 'recent', 3);pageTracker._trackPageview('/intra/hashtag/#NetBeans');&quot;&gt;&lt;b&gt;#NetBeans&lt;/b&gt;&lt;/a&gt; &lt;a href=&quot;http://search.twitter.com/search?q=%23PHP&quot; onclick=&quot;pageTracker._setCustomVar(2, 'result_type', 'recent', 3);pageTracker._trackPageview('/intra/hashtag/#PHP');&quot;&gt;#PHP&lt;/a&gt; blog if you&amp;apos;re one of the 25% of NetBeans 6.9 users using a PHP project: &lt;a href=&quot;http://bit.ly/fG74xF&quot;&gt;http://bit.ly/fG74xF&lt;/a&gt; :)</content>
//    <updated>2011-03-05T05:51:02Z</updated>
//    <link type="image/png" href="http://a3.twimg.com/profile_images/1151565712/198436690_normal.jpg" rel="image"/>
//    <twitter:geo>
//    </twitter:geo>
//    <twitter:metadata>
//      <twitter:result_type>recent</twitter:result_type>
//    </twitter:metadata>
//    <twitter:source>&lt;a href=&quot;http://www.tweetdeck.com&quot; rel=&quot;nofollow&quot;&gt;TweetDeck&lt;/a&gt;</twitter:source>
//    <twitter:lang>en</twitter:lang>
//    <author>
//      <name>nrcpts (Andri Burman)</name>
//      <uri>http://twitter.com/nrcpts</uri>
//    </author>
//  </entry>

    Tweet tweet = new Tweet();

    reader = moveTo(reader, "published");
    if (reader != null) {
      String txt = reader.getElementText();
      tweet.setTimestamp(parseTime(txt));
    } else {
      return tweet;
    }
    reader = moveTo(reader, "link");
    if (reader != null) {
      tweet.setUrl(parseURL(reader.getAttributeValue(null, "href")));
    } else {
      return tweet;
    }
    reader = moveTo(reader, "title");
    if (reader != null) {
      tweet.setContent(reader.getElementText());
    } else {
      return tweet;
    }
    reader = moveTo(reader, "author");
    if (reader != null) {
      reader = moveTo(reader, "name");
      if (reader != null) {
        tweet.getAuthor().setName(reader.getElementText());
      } else {
        return tweet;
      }
      reader = moveTo(reader, "uri");
      if (reader != null) {
        tweet.getAuthor().setTwitterURL(parseURL(reader.getElementText()));
      } else {
        return tweet;
      }
    }

    return tweet;
  }

  private static XMLStreamReader moveTo(XMLStreamReader reader, String tagName) throws XMLStreamException {
    if (reader == null) {
      return null;
    }
    boolean found = false;
    for (int event = reader.nextTag(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
      if (event == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(tagName)) {
        found = true;
        break;
      }
    }
    return found ? reader : null;
  }

  private static long parseTime(String txt) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    try {
      return sdf.parse(txt).getTime();
    } catch (ParseException ex) {
      Logger.getLogger(TwitterSearchDAO.class.getName()).log(Level.SEVERE, null, ex);
      return -1;
    }
  }

  private static URL parseURL(String txt) {
    try {
      URL url = new URL(txt);
      return url;
    } catch (MalformedURLException ex) {
      Logger.getLogger(TwitterSearchDAO.class.getName()).log(Level.SEVERE, "Wrong url: " + txt, ex);
      try {
        return new URL("http://www.twitter.com");
      } catch (MalformedURLException ex1) {
        Logger.getLogger(TwitterSearchDAO.class.getName()).log(Level.SEVERE, null, ex1);
        return null;
      }
    }
  }

  public List<Tweet> search(Query query) throws IOException, XMLStreamException {
    return search( query.toString() );
  }
}

/*
Copyright 2011-2012 Antonio Vieiro-Varela. ALl rights reserved.

Oracle and Java are registered trademarks of Oracle and/or its affiliates.
Other names may be trademarks of their respective owners.

The contents of this file are subject to the terms of either the GNU
General Public License Version 2 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://www.netbeans.org/cddl-gplv2.html
or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License file at
nbbuild/licenses/CDDL-GPL-2-CP.  Antonio designates this
particular file as subject to the "Classpath" exception as provided
by Antonio in the GPL Version 2 section of the License file that
accompanied this code. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

Contributor(s):

The Original Software is NBTWEETING. The Initial Developer of the Original
Software is Antonio Vieiro.

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 2, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 2] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 2 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 2 code and therefore, elected the GPL
Version 2 license, then the option applies only if the new code is
made subject to such option by the copyright holder.
*/