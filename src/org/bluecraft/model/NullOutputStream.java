/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Jacob Morgan
 */
public class NullOutputStream extends OutputStream {
  @Override
  public void write(int b) throws IOException {
  }
}

