/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.entities;

import java.util.ArrayList;
import java.util.List;
import net.antonioshome.nbtweeting.dao.TwitterSearchDAO;
import net.antonioshome.nbtweeting.entities.abilities.HasTextContent;
import net.antonioshome.nbtweeting.entities.abilities.Reloadable;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Query represents a query against the Twitter Search API.
 * This class has special abilities, so it implements Lookup.Provider.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class Query 
implements Lookup.Provider
{
  private String keyword;
  private List<Tweet> tweets;
  /** The lookup for Lookup.Provider */
  private Lookup lookup;
  /** The InstanceContent that keeps this entity's abilities */
  private InstanceContent instanceContent;
  /**
   * Simple constructor, not much to say here.
   */
  public Query()
  {
    this.tweets = new ArrayList<Tweet>();
    // Create an InstanceContent to hold abilities...
    this.instanceContent = new InstanceContent();
    // Create an AbstractLookup to expose InstanceContent contents...
    this.lookup = new AbstractLookup( instanceContent );
    // Add a "Reloadable" ability to this entity
    this.instanceContent.add( new Reloadable() {

      public void reload() throws Exception {
        TwitterSearchDAO dao = new TwitterSearchDAO();
        getTweets().clear();
        getTweets().addAll( dao.search( keyword ) );
      }
    } );
    // Add a "HasTextContent" ability to this entity so we may discover this entity has text content
    this.instanceContent.add( new HasTextContent() {

      public String getText() {
        return "Query term: " + keyword;
      }
    } );
  }

  /**
   * @return the keyword
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * @param keyword the keyword to set
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Override
    public String toString()
  {
    return keyword;
  }

  public Lookup getLookup() {
    return lookup;
  }

  /**
   * @return the tweets
   */
  public List<Tweet> getTweets() {
    return tweets;
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