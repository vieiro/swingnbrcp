/* (C) 2011, 2012 Antonio Vieiro (antonio@antonioshome.net). All rights reserved. */
package net.antonioshome.nbtweeting.nodes.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.antonioshome.nbtweeting.nodes.abilities.ReloadableNode;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * ReloadAction is an action for objects implementing the ReloadableNode ability.
 * This is, is an action that reloads a Node's Children.
 * @author Antonio Vieiro (antonio@antonioshome.net)
 */
public final class ReloadAction 
extends AbstractAction
{
  private ReloadableNode reloadableNode;

  /**
   * Constructor from Lookup
   * @param lookup 
   */
  public ReloadAction( Lookup lookup )
  {
    // Get the ReloadableNode ability
    reloadableNode = lookup.lookup( ReloadableNode.class );
    // Add some Action specific parameters (we could add an icon too)
    putValue( AbstractAction.NAME, "Reload");
    putValue( AbstractAction.SHORT_DESCRIPTION, "Reloads this object");
  }

  public void actionPerformed(ActionEvent e) {
    // If the "ReloadableNode" ability is non empty we just invoke
    // the "reloadChildren" method to refresh the list of children.
    if( reloadableNode!= null )
      try {
      reloadableNode.reloadChildren();
    } catch (Exception ex) {
      Exceptions.printStackTrace(ex);
    }
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