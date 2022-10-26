package org.fpij.jitakyoei.mocks;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AlunoEmptyTableMock extends JTable {
        public AlunoEmptyTableMock() {
                this.setModel(new DefaultTableModel(
                                new Object[][] {},
                                new String[] {
                                                "Resistro", "Nome", "Professor", "Entidade"
                                }));
        }
}
