/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableModel;

import dto.ReceitaInsumoDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas_matheus_silva
 */
public class ReceitaInsumoTableModel extends GenericTableModel<ReceitaInsumoDto> {

    private final String[] colunas = {"codigoReceita", "codigoInsumo", "consumo"};

    private List<ReceitaInsumoDto> list;

    private ReceitaInsumoTableModel() {
        list = new ArrayList<>();
    }

    public ReceitaInsumoTableModel(List<ReceitaInsumoDto> list) {
        this();
        setData(list);
    }

    @Override
    public void add(ReceitaInsumoDto entity) {
        list.add(0, entity);
        super.fireTableDataChanged();
    }

    @Override
    public void clear() {
        this.list.clear();
        super.fireTableDataChanged();
    }

    @Override
    public boolean contains(ReceitaInsumoDto entity) {
        return list.contains(entity);
    }

    @Override
    public ReceitaInsumoDto getValueAT(int row) {
        return list.get(row);
    }

    @Override
    public int indexOf(ReceitaInsumoDto entity) {
        return list.indexOf(entity);
    }

    @Override
    public List<ReceitaInsumoDto> list() {
        return list;
    }

    @Override
    public void remove(ReceitaInsumoDto entity) {
        list.remove(entity);
        super.fireTableDataChanged();
    }

    @Override
    public void setData(List<ReceitaInsumoDto> list) {
        this.list.clear();
        this.list.addAll(list);
        super.fireTableDataChanged();
    }

    @Override
    public void updateItem(int idx, ReceitaInsumoDto entity) {
        list.set(idx, entity);
        super.fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        ReceitaInsumoDto ri = list.get(linha);
        switch (coluna) {
            case 0:
                return ri.getCodigoInsumo();
            case 1:
                return ri.getCodigoReceita();
            case 2:
                return ri.getConsumo();
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return colunas[col];
    }

    public String[] getColumnNames() {
        return colunas;
    }
}
