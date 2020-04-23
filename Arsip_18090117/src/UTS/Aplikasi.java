/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UTS;

import java.awt.Frame;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RamaPC
 */
public class Aplikasi extends javax.swing.JFrame {

    int idBaris = 0;
    String role;
    DefaultTableModel model;
    String filename = null;

    public Aplikasi() {
        initComponents();
        aturModelTabel();
        kategori();
        showForm(false);
        showData("");
    }

    private void aturModelTabel() {
        Object[] kolom = {"id", "Kode", "Nama", "Kategori", "lokasi", "Deskripsi", "Tanggal"};
        model = new DefaultTableModel(kolom, 0) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tabels.setModel(model);
        tabels.setRowHeight(20);
        tabels.getColumnModel().getColumn(0).setMinWidth(0);
        tabels.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    private void showForm(boolean b) {
        areaSplit.setDividerLocation(0.3);
        areaSplit.getLeftComponent().setVisible(b);
    }

    private void resetForm() {
        tabels.clearSelection();
        kodes.setText("");
        namadok.setText("");
        kategoris.setSelectedIndex(0);
        direk.setText("");
        deskripsis.setText("");
        tanggals.setText("");
        kodes.requestFocus();
    }

    private void kategori() {
        kategoris.removeAllItems();
        kategoris.addItem("Pilih Kategori");
        kategoris.addItem("Rahasia");
        kategoris.addItem("Resmi");
        kategoris.addItem("Biasa");
    }

    private void showData(String key) {
        model.getDataVector().removeAllElements();
        String where = "";
        if (!key.isEmpty()) {
            where += "WHERE kode LIKE '%" + key + "%' "
                    + "OR nama LIKE '%" + key + "%' "
                    + "OR kategori LIKE '%" + key + "%' "
                    + "OR lokasi LIKE '%" + key + "%' "
                    + "OR deskripsi LIKE '%" + key + "%'"
                    + "OR tanggal LIKE '%" + key + "%'";
        }
        String sql = "SELECT * FROM dokumen " + where;
        Connection con;
        Statement st;
        ResultSet rs;
        int baris = 0;
        try {
            con = Koneksi.MySQL();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object kode = rs.getInt(2);
                Object nama = rs.getString(3);
                Object kategori = rs.getString(4);
                Object lokasi = rs.getString(5);
                Object deskripsi = rs.getString(6);
                Object tanggal = rs.getString(7);
                Object[] data = {id, kode, nama, kategori, lokasi, deskripsi, tanggal};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tabels.revalidate();
            tabels.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): " + e.getMessage());
        }
    }

    private void resetView() {
        resetForm();
        showForm(false);
        showData("");
        hapus.setEnabled(false);
        idBaris = 0;
    }

    private void pilihData(String n) {
        hapus.setEnabled(true);
        String sql = "SELECT * FROM dokumen WHERE id='" + n + "'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = Koneksi.MySQL();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String kode = rs.getString(2);
                String nama = rs.getString(3);
                Object kategori = rs.getString(4);
                String lokasi = rs.getString(5);
                String deskripsi = rs.getString(6);
                String tanggal = rs.getString(7);
                idBaris = id;
                kodes.setText(kode);
                namadok.setText(nama);
                kategoris.setSelectedItem(kategori);
                direk.setText(lokasi);
                deskripsis.setText(deskripsi);
                tanggals.setText(tanggal);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.err.println("pilihData(): " + e.getMessage());
        }
    }

    private void simpanData() {
        String kode = kodes.getText();
        String nama = namadok.getText();
        Object kategori = kategoris.getSelectedItem();
        String lokasi = direk.getText();
        String deskripsi = deskripsis.getText();
        String tanggal = tanggals.getText();
        if (kode.isEmpty() || nama.isEmpty() || kategori.equals("") || lokasi.isEmpty() || deskripsi.isEmpty()
                || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        } else {
            String kategoria = kategoris.getSelectedItem().toString();
            String sql
                    = "INSERT INTO dokumen (kode, nama, kategori, lokasi, deskripsi, tanggal)"
                    + "VALUES ('" + kode + "','" + nama + "','" + kategori + "','" + lokasi + "','" + deskripsi + "','" + tanggal + "')";
            Connection con;
            Statement st;
            try {
                con = Koneksi.MySQL();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this, "Data telah disimpan!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void ubahData() {
        String kode = kodes.getText();
        String nama = namadok.getText();
        Object kategori = kategoris.getSelectedItem();
        String lokasi = direk.getText();
        String deskripsi = deskripsis.getText();
        String tanggal = tanggals.getText();
        if (kode.isEmpty() || nama.isEmpty() || kategori.equals("") || lokasi.isEmpty() || deskripsi.isEmpty()
                || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        } else {
            String kategoria = kategoris.getSelectedItem().toString();
            String sql = "UPDATE dokumen "
                    + "SET kode='" + kode + "',"
                    + "nama='" + nama + "',"
                    + "kategori='" + kategori + "',"
                    + "lokasi='" + lokasi + "',"
                    + "deskripsi='" + deskripsi + "',"
                    + "tanggal='" + tanggal + "' WHERE id='" + idBaris + "'";
            Connection con;
            Statement st;
            try {
                con = Koneksi.MySQL();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this, "Data telah diubah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void hapusData(int baris) {
        Connection con;
        Statement st;
        try {
            con = Koneksi.MySQL();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM dokumen WHERE id=" + baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Data telah dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tambah = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        Caris = new java.awt.TextField();
        Cari = new javax.swing.JButton();
        areaSplit = new javax.swing.JSplitPane();
        panelKiri = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kodes = new javax.swing.JTextField();
        namadok = new javax.swing.JTextField();
        kategoris = new javax.swing.JComboBox<>();
        deskripsis = new javax.swing.JTextField();
        tutups = new javax.swing.JButton();
        simpans = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        tanggals = new javax.swing.JTextField();
        direk = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabels = new javax.swing.JTable();
        Keluars = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 51));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        tambah.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tambah.setText("Tambah Data");
        tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahActionPerformed(evt);
            }
        });

        hapus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        hapus.setText("Hapus Data");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        Caris.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CarisKeyReleased(evt);
            }
        });

        Cari.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Cari.setText("Cari");

        panelKiri.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Kode ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Nama Dokumen");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Kategori ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Lokasi directory");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Deskripsi ");

        kategoris.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tutups.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tutups.setText("Tutup");
        tutups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutupsActionPerformed(evt);
            }
        });

        simpans.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        simpans.setText("Simpan");
        simpans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpansActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Tanggal");

        tanggals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tanggalsActionPerformed(evt);
            }
        });

        direk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                direkActionPerformed(evt);
            }
        });

        jButton1.setText("Upload");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKiriLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namadok, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11))
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKiriLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(direk, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addGap(4, 4, 4))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(kategoris, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(kodes, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(tutups)
                        .addGap(18, 18, 18)
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simpans, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(deskripsis, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tanggals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kodes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namadok, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kategoris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(direk)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deskripsis))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggals))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpans)
                    .addComponent(tutups))
                .addGap(142, 142, 142))
        );

        areaSplit.setLeftComponent(panelKiri);

        tabels.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabels.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabels);

        areaSplit.setRightComponent(jScrollPane1);

        Keluars.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Keluars.setText("Keluar");
        Keluars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KeluarsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(tambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Caris, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Keluars, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
            .addComponent(areaSplit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tambah)
                        .addComponent(hapus)
                        .addComponent(Cari))
                    .addComponent(Caris, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Keluars))
                .addGap(18, 18, 18)
                .addComponent(areaSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahActionPerformed
        // TODO add your handling code here:
        role = "Tambah";
        simpans.setText("SIMPAN");
        idBaris = 0;
        resetForm();
        showForm(true);
        hapus.setEnabled(false);
    }//GEN-LAST:event_tambahActionPerformed

    private void simpansActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpansActionPerformed
        // TODO add your handling code here:
        if (role.equals("Tambah")) {
            simpanData();
        } else if (role.equals("Ubah")) {
            ubahData();
        }
    }//GEN-LAST:event_simpansActionPerformed

    private void tutupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tutupsActionPerformed
        // TODO add your handling code here:
        resetForm();
        showForm(false);
        hapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_tutupsActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        // TODO add your handling code here:
        if (idBaris == 0) {
            JOptionPane.showMessageDialog(this, "Dihapus!");
        } else {
            hapusData(idBaris);
        }
    }//GEN-LAST:event_hapusActionPerformed

    private void CarisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CarisKeyReleased
        // TODO add your handling code here:
        String key = Caris.getText();
        showData(key);
    }//GEN-LAST:event_CarisKeyReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void tanggalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tanggalsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggalsActionPerformed

    private void KeluarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KeluarsActionPerformed
        // TODO add your handling code here:
        int pilih = JOptionPane.showConfirmDialog(this,
                "Apakah anda yakin ingin keluar?",
                "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            System.exit(0);

        }
    }//GEN-LAST:event_KeluarsActionPerformed

    private void direkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_direkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_direkActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        direk.setText(filename);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabelsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelsMouseClicked
        // TODO add your handling code here:
        role = "Ubah";
        int row = tabels.getRowCount();
        if (row > 0) {
            int sel = tabels.getSelectedRow();
            if (sel != -1) {
                pilihData(tabels.getValueAt(sel, 0).toString());
                simpans.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tabelsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Aplikasi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cari;
    private java.awt.TextField Caris;
    private javax.swing.JButton Keluars;
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JTextField deskripsis;
    private javax.swing.JTextField direk;
    private javax.swing.JButton hapus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> kategoris;
    private javax.swing.JTextField kodes;
    private javax.swing.JTextField namadok;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JButton simpans;
    private javax.swing.JTable tabels;
    private javax.swing.JButton tambah;
    private javax.swing.JTextField tanggals;
    private javax.swing.JButton tutups;
    // End of variables declaration//GEN-END:variables
}
