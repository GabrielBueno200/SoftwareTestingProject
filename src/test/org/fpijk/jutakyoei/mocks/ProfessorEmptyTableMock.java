package org.fpijk.jutakyoei.mocks;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ProfessorEmptyTableMock extends JTable {
    public ProfessorEmptyTableMock() {
        this.setModel(new DefaultTableModel(
                new Object[][] {
                        { null, null },
                        { null, null },
                },
                new String[] {
                        "Registro", "Nome"
                }));
        this.setName("professores");
    }
}
