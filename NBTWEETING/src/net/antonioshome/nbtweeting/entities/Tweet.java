/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.entities;

import java.net.URL;
import net.antonioshome.nbtweeting.entities.abilities.HasTextContent;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Tweet is an entity that represents a tweet.
 * This class has special abilities, so implements the Lookup.Provider interface.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class Tweet 
implements Lookup.Provider
{
  private long timestamp;
  private URL url;
  private String content;
  private TweetAuthor author;
  private Lookup lookup;
  private InstanceContent instanceContent;

  /**
   * Basic constructor.
   */
  public Tweet()
  {
    author = new TweetAuthor();
    // Create an InstanceContent to hold abilities...
    instanceContent = new InstanceContent();
    // Create an AbstractContent to expose InstanceContent contents...
    lookup = new AbstractLookup( instanceContent );
    // Add a "HasTextContent" ability, so we know this entity has text content
    instanceContent.add( new HasTextContent() {

      public String getText() {
        return content;
      }
    });
  }

  /**
   * @return the timestamp
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return the url
   */
  public URL getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(URL url) {
    this.url = url;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the author
   */
  public TweetAuthor getAuthor() {
    return author;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(TweetAuthor author) {
    this.author = author;
  }

  @Override
    public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[TWEET, author=").append( author ).append(",TS=").append( timestamp ).append( ",CONTENT=").append( content ).append("]");
    return sb.toString();
  }

  public Lookup getLookup() {
    return lookup;
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