package org.fpij.jitakyoei.utils.mocks;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EntidadeEmptyTableMock extends JTable {
    public EntidadeEmptyTableMock() {
        this.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "Nome", "CNPJ", "Telefone 1", "Telefone 2"
                }));
        this.setCellSelectionEnabled(true);
        this.setName("tblEntidades");
    }
}
