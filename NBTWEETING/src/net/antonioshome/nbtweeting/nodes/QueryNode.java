/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.nodes;

import java.awt.Image;
import javax.swing.Action;
import net.antonioshome.nbtweeting.entities.Query;
import net.antonioshome.nbtweeting.nodes.abilities.ReloadableNode;
import net.antonioshome.nbtweeting.nodes.actions.ReloadAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * QueryNode is a node that represents a query. The children of this
 *   node are the results of the query.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class QueryNode
  extends AbstractNode {

  private Query query;
  private InstanceContent instanceContent;

  /**
   * Public constructor from an entity.
   * @param query The entity that this node represents visually.
   */
  public QueryNode(Query query) {
    this(query, new InstanceContent());
  }

  /**
   * Private constructor from an entity and an InstanceContent
   * @param query The entity that this node represents visually.
   * @param ic The InstanceContent object that keeps this node's abilities.
   */
  private QueryNode(Query query, InstanceContent ic) {
    // Invoke the super constructor, passing it a list of children 
    // to be retrieved with a QueryNodeChildFactory, and
    // a ProxyLookup that combines this node's abilities with the entity's
    super(Children.create(new QueryNodeChildFactory(query), true), 
      new ProxyLookup( // Combination of lookups
        query.getLookup(), // The entitie's abilities
        new AbstractLookup(ic))); // This node's abilities
    // Keep the entity and the instancecontent on member variables
    this.query = query;
    this.instanceContent = ic;
    // Add a new ability for this node to be reloaded
    this.instanceContent.add(new ReloadableNode()  {
      public void reloadChildren() throws Exception {
        // To reload this node just set a new set of children
        // using a QueryNodeChildFactory object, that retrieves
        // children asynchronously
        setChildren(Children.create(new QueryNodeChildFactory(QueryNode.this.query), true));
      }
    });
  }

  @Override
  public String getDisplayName() {
    // Simple string representation of a query
    return "Query: " + query.getKeyword();
  }

  @Override
  public Image getIcon(int type) {
    // An icon for the query
    return ImageUtilities.loadImage("net/antonioshome/nbtweeting/nodes/resources/query.png"); // NOI18N
  }

  @Override
  public Image getOpenedIcon(int type) {
    // An icon for this node when it's expanded. The same as closed icon for simplicity
    return getIcon(type);
  }

  @Override
  public Action[] getActions(boolean context) {
    // A list of actions for this node
    return new Action[]{new ReloadAction(getLookup())};
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