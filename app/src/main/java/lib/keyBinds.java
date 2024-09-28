package lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class keyBinds {
  /**
   * Adds keybinding to a JComponent
   *
   * @param im         The input map
   * @param ap         The action map
   * @param keyCode    The code for the key
   * @param modifier   Modifier of the key (Shift, Alt, ...)
   * @param id         The id of the keybinding (Any relevant name)
   * @param action     The action to be performed after activation of the key
   * @param onReleased If true, keybinding triggers when key is released.
   *                   Otherwise, it triggers on press
   */
  public static void addKeyBinding(InputMap im, ActionMap ap, int keyCode, int modifier, String id,
      ActionListener action, boolean onReleased) {

    // Create keystroke binding
    im.put(KeyStroke.getKeyStroke(keyCode, modifier, onReleased), id);

    // Add action to keystroke binding
    ap.put(id, new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        action.actionPerformed(e);
      }
    });
  }

  /**
   * Adds keypressed keybinding to a JComponent
   *
   * @param jcomp    Component to add binding to
   * @param keyCode  The code for the key
   * @param modifier Modifier of the key (Shift, Alt, ...)
   * @param id       The id of the keybinding (Any relevant name)
   * @param action   The action to be performed after activation of the key
   */
  public static void addKeyBindingPressed(JComponent jcomp, int keyCode, int modifier, String id,
      ActionListener action) {
    InputMap im = jcomp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap ap = jcomp.getActionMap();
    addKeyBinding(im, ap, keyCode, modifier, id, action, false);
  }

  /**
   * Adds keyReleased keybinding to a JComponent
   *
   * @param jcomp    Component to add binding to
   * @param keyCode  The code for the key
   * @param modifier Modifier of the key (Shift, Alt, ...)
   * @param id       The id of the keybinding (Any relevant name)
   * @param action   The action to be performed after activation of the key
   */
  public static void addKeyBindingReleased(JComponent jcomp, int keyCode, int modifier, String id,
      ActionListener action) {
    InputMap im = jcomp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap ap = jcomp.getActionMap();
    addKeyBinding(im, ap, keyCode, modifier, id, action, true);
  }

  /**
   * Adds keypressed keybinding to a JComponent
   *
   * @param im         The input map
   * @param ap         The action map
   * @param keyCode  The code for the key
   * @param modifier Modifier of the key (Shift, Alt, ...)
   * @param id       The id of the keybinding (Any relevant name)
   * @param action   The action to be performed after activation of the key
   */
  public static void addKeyBindingPressed(InputMap im, ActionMap ap, int keyCode, int modifier, String id,
      ActionListener action) {
    addKeyBinding(im, ap, keyCode, modifier, id, action, false);
  }
  /**
   * Adds keypressed keybinding to a JComponent
   *
   * @param im         The input map
   * @param ap         The action map
   * @param keyCode  The code for the key
   * @param modifier Modifier of the key (Shift, Alt, ...)
   * @param id       The id of the keybinding (Any relevant name)
   * @param action   The action to be performed after activation of the key
   */
  public static void addKeyBindingReleased(InputMap im, ActionMap ap, int keyCode, int modifier, String id,
      ActionListener action) {
    addKeyBinding(im, ap, keyCode, modifier, id, action, true);
  }
}
