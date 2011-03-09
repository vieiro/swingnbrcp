/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.nodes;

import java.awt.Image;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import net.antonioshome.nbtweeting.entities.Tweet;
import net.antonioshome.nbtweeting.propertyeditors.TimestampPropertyEditor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * TweetNode is a Node that visually represents a Tweet.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class TweetNode
  extends AbstractNode {

  private Tweet tweet;
  private InstanceContent ic;

  public static String DATE_PROPERTY = "DATE";
  public static String AUTHOR_PROPERTY = "AUTHOR";

  /**
   * Public constructor from a Tweet entity.
   * @param tweet The entity that this node represents visually.
   */
  public TweetNode(Tweet tweet) {
    this(tweet, new InstanceContent());
  }

  /**
   * Private constructor from a Tweet entity and an empty InstanceContent.
   * @param tweet The entity that this node represents visually.
   * @param ic An InstanceContent used to hold this node's abilities.
   */
  private TweetNode(Tweet tweet, InstanceContent ic) {
    // Invoke the super constructor
    super(Children.LEAF, // With the Children.LEAF constant, because we have no children
      new ProxyLookup( // With a lookup that combines...
        tweet.getLookup(), // the entitie's abilities
      new AbstractLookup(ic)) ); // and this node's own abilities
    // Keep instance variables for later use
    this.tweet = tweet;
    this.ic = ic;
    // Add the entity to the list of this node's abilities
    // (this is handy to retrieve the entity from a node
    this.ic.add(tweet);
  }

  @Override
  public String getDisplayName() {
    // Return a not-very-long string representation of the entity
    String result = tweet.getAuthor().getName() + " said " + tweet.getContent();
    return result.length() > 60? result.substring(0,55)+ "..." : result;
  }

  /**
   * This is a String to be displayed in tooltips.
   * @return a String to be displayed in tooltips and PropertySheetViews.
   */
  @Override
  public String getShortDescription() {
    // Return a simple HTML representation of this node
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<p style='font-weight: bold'>").append( tweet.getAuthor().getName() ).append("</p>");
    sb.append("<p style='font-size: small'>").append( tweet.getContent() ).append("</p>");
    sb.append("</html>");
    return sb.toString();
  }

  

  /**
   * An icon when this node is closed
   * @param type The type of icon, ignored.
   * @return The icon.
   */
  @Override
  public Image getIcon(int type) {
    return ImageUtilities.loadImage("net/antonioshome/nbtweeting/nodes/resources/tweet.png"); // NOI18N
  }

  /**
   * This method is invoked once to create a Sheet of Properties of this node.
   * @return A Sheet of Properties for this node.
   */
  @Override
  protected Sheet createSheet() {

    // Create an empty sheet
    Sheet sheet = Sheet.createDefault();
    // Create a set of properties
    Sheet.Set set = Sheet.createPropertiesSet();

    // The Date read-only property
    Property tweetDateProperty = new PropertySupport.ReadOnly<Date>( DATE_PROPERTY, Date.class, "Tweet date", "The date where the tweet was sent") {
      @Override
      public Date getValue() throws IllegalAccessException, InvocationTargetException {
        // Returns this node's entity date
        return new Date( tweet.getTimestamp() );
      }

      @Override
        public PropertyEditor getPropertyEditor()
      {
        // Returns a PropertyEditor that visually represents (and allows editing) this
        // node's property.
        return new TimestampPropertyEditor();
      }
    };
    set.put( tweetDateProperty );

    // A read-only property that represents the author (String type)
    Property tweetAuthorProperty = 
      new PropertySupport.ReadOnly<String>( AUTHOR_PROPERTY, String.class, "Author", "The author of the Tweet") {

      @Override
      public String getValue() throws IllegalAccessException, InvocationTargetException {
        return tweet.getAuthor().getName();
      }

    };
    // Add the tweetAuthorProperty to the set of properties
    set.put( tweetAuthorProperty);


    // Add the set of properties to the sheet
    sheet.put( set );

    return sheet;
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