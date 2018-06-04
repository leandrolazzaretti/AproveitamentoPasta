/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableModel;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lucas_matheus_silva
 */
public abstract class GenericTableModel <T> extends AbstractTableModel{
    
    public abstract void setData (List<T> list);
    public abstract T getValueAT (int row);
    public abstract int indexOf(T entity);
    public abstract void clear ();
    public abstract void remove(T entity);
    public abstract void add(T entity);
    public abstract boolean contains (T entity);
    public abstract List<T> list();
    public abstract void updateItem (int idx, T entity);
    
}
