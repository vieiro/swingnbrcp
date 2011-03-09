/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.nodes;

import java.util.List;
import net.antonioshome.nbtweeting.entities.Query;
import net.antonioshome.nbtweeting.entities.Tweet;
import net.antonioshome.nbtweeting.entities.abilities.Reloadable;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 * QueryNodeChildFactory is a ChildFactory for QueryNodes, responsible
 *   for retrieving the Tweets for a given Query, and for wrapping
 *   those Tweets into TweetNodes.
 * QueryNodeChildFactory is also Reloadable.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
class QueryNodeChildFactory
  extends ChildFactory<Tweet> { // Tweet is the type of "child entity"

  private Query query;

  /**
   * Constructor from a Query entity.
   * @param query The entity whose children we have to calculate
   */
  public QueryNodeChildFactory(Query query) {
    this.query = query;
  }

  /**
   * Responsible for creating a Node that visually represents a child entity.
   * @param key The child entity (a Tweet)
   * @return  A node that visually represents a Tweet, this is, a TweetNode.
   */
  @Override
  protected Node createNodeForKey(Tweet key) {
    return new TweetNode(key);
  }

  /**
   * This method will be invoked by the NetBeans Platform, we have to
   * populate the list with the child entities of our Query.
   * @param list The list of tweets to populate
   * @return true if the list is all set, false if we need to be invoked
   *   again later to fill-in more child entities
   */
  @Override
  protected boolean createKeys(List<Tweet> list) {

    // The query node is reloadable, isn't it? Then just
    // get this ability from the lookup ...
    Reloadable r = query.getLookup().lookup(Reloadable.class);

    // ... and  use the ability
    if (r != null) {
      try {
        r.reload();
      } catch (Exception e) {
        // Empty
      }
    }

    // Now populate the list of child entities...
    list.addAll(query.getTweets());

    // And return true since we're all set
    return true;
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